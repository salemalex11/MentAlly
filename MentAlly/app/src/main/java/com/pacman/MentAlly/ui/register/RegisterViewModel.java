package com.pacman.MentAlly.ui.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.pacman.MentAlly.data.RegisterRepository;
import com.pacman.MentAlly.data.Result;
import com.pacman.MentAlly.data.model.RegisteredUser;
import com.pacman.MentAlly.R;

import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class RegisterViewModel extends ViewModel {

    private MutableLiveData<RegisterFormState> registerFormState = new MutableLiveData<>();
    private MutableLiveData<RegisterResult> registerResult = new MutableLiveData<>();
    private RegisterRepository registerRepository;

    RegisterViewModel(RegisterRepository registerRepository) {
        this.registerRepository = registerRepository;
    }

    LiveData<RegisterFormState> getRegisterFormState() {
        return registerFormState;
    }

    LiveData<RegisterResult> getRegisterResult() {
        return registerResult;
    }

    public void register(String username, String password, String name, String DOB, String gender, String country) {
        // can be launched in a separate asynchronous job
        Result<RegisteredUser> result = registerRepository.register(username, password, name, DOB, gender, country);

        if (result instanceof Result.Success) {
            RegisteredUser data = ((Result.Success<RegisteredUser>) result).getData();
            registerResult.setValue(new RegisterResult(new RegisteredUserView(data.getDisplayName())));
        } else {
            registerResult.setValue(new RegisterResult(R.string.registration_failed));
        }
    }

    public void registerDataChanged(String username, String password, String name,
                                    String DOB,  String gender, String country) {
        if (!isUserNameValid(username)) {
            registerFormState.setValue(new RegisterFormState(R.string.invalid_username, null, null, null, null, null));
        } else if (!isPasswordValid(password)) {
            registerFormState.setValue(new RegisterFormState(null, R.string.invalid_password, null, null, null, null));
        } else if (!isDOBValid((DOB))) {
            registerFormState.setValue(new RegisterFormState(null, null, null, R.string.invalid_DOB, null, null));
        } else if (!isGenderValid(gender)) {
            registerFormState.setValue(new RegisterFormState(null, null, null, null, R.string.invalid_gender, null));
        } else {
            registerFormState.setValue(new RegisterFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    // A placeholder DOB validation check
    private boolean isDOBValid(String DOB) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(DOB.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

    private boolean isGenderValid(String gender) {
        if (gender.equals("Select")) {
            return false;
        }
        return true;
    }


}

