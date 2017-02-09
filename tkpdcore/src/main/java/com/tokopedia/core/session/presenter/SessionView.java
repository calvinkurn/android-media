package com.tokopedia.core.session.presenter;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.google.android.gms.plus.model.people.Person;
import com.tokopedia.core.session.model.CreatePasswordModel;

import java.util.List;

/**
 * Created by m.normansyah on 04/11/2015.
 * presenter
 */
public interface SessionView {
    String TAG = "MNORMANSYAH";
    String messageTAG = "SessionView : ";

    String LOGIN_FRAGMENT_TAG = "LOGIN";
    String REGISTER_FRAGMENT_TAG = "REGISTER";
    String SECURITY_QUESTION_TAG = "SECURITY_QUESTION";
    String REGISTER_NEXT_TAG = "REGISTER_NEXT";
    String ACTIVATION_RESEND_TAG = "ACTIVATION_RESEND";
    String FORGOT_PASSWORD_TAG = "FORGOT_PASSWORD";
    String REGISTER_THIRD = "REGISTER_THIRD";
    String REGISTER_INITIAL = "REGISTER_INITIAL";

    String WHICH_FRAGMENT_KEY = "WHICH_FRAGMENT_KEY";
    String MOVE_TO_CART_KEY = "MOVE_TO_CART_KEY";
    int INVALID_MOVE_TYPE = -1;
    int MOVE_TO_CART_TYPE = 1;
    int HOME = 2;
    int SELLER_HOME = 3;

    boolean isFragmentCreated(String tag);

    void moveToFragmentSecurityQuestion(int security1, int security2, int userId);

    String getGenderFromGoogle(Person user);

    String getBirthdayFromGoogle(Person user);

    void moveToNewRegisterNext(String name, String email, String password, boolean isAutoVerify);

    void moveToRegisterPassPhone(CreatePasswordModel model, List<String> createPasswordList, Bundle data);

    void moveToActivationResend(String email, Bundle bundle);

    void moveToRegister();

    void moveToForgotPassword();

    void updateUI(boolean isSignedIn);

    void moveToFragment(Fragment fragment, boolean isAddtoBackStack, String TAG, int type);

    void setToolbarTitle();

    void sendDataFromInternet(int type, Bundle data);

    void moveTo(int type);

    void destroy();

    void prevFragment();

    void showError(String text);

    void moveToRegisterInitial();

    void moveToLogin();

    void verifyTruecaller();
}
