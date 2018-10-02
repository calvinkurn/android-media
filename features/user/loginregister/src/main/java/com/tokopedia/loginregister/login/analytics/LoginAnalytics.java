package com.tokopedia.loginregister.login.analytics;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

/**
 * @author by nisie on 10/2/18.
 */
public class LoginAnalytics {

    public static final String SCREEN_LOGIN = "Login page";

    private static final String EVENT_CLICK_LOGIN = "clickLogin";
    private static final String EVENT_REGISTER_LOGIN = "registerLogin";
    private static final String EVENT_LOGIN_ERROR = "loginError";
    private static final String EVENT_LOGIN_SUCCESS = "loginSuccess";
    private static final String EVENT_LOGIN_CLICK = "clickLogin";

    private static final String CATEGORY_LOGIN = "Login";

    private static final String ACTION_CLICK = "Click";
    private static final String ACTION_REGISTER = "Register";
    private static final String ACTION_LOGIN_ERROR = "Login Error";
    private static final String ACTION_LOGIN_SUCCESS = "Login Success";


    private static final String LABEL_CTA = "CTA";
    private static final String LABEL_REGISTER = "Register";
    private static final String LABEL_PASSWORD = "Kata Sandi";
    private static final String LABEL_EMAIL = "Email";
    private static final String LABEL_PHONE_NUMBER = "Phone Number";
    private static final String LABEL_GPLUS = "Google Plus";
    private static final String LABEL_FACEBOOK = "Facebook";


    AnalyticTracker analyticTracker;

    public LoginAnalytics(AnalyticTracker analyticTracker) {
        this.analyticTracker = analyticTracker;
    }

    public void trackScreen(Activity activity, String screenName) {
        analyticTracker.sendScreen(activity, screenName);
    }

    public void eventClickLoginButton() {
        analyticTracker.sendEventTracking(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN,
                ACTION_CLICK,
                LABEL_CTA
        );
    }

    public void eventClickRegisterFromLogin() {
        analyticTracker.sendEventTracking(
                EVENT_REGISTER_LOGIN,
                CATEGORY_LOGIN,
                ACTION_REGISTER,
                LABEL_REGISTER
        );
    }

    public void eventLoginErrorPassword() {
        analyticTracker.sendEventTracking(
                EVENT_LOGIN_ERROR,
                CATEGORY_LOGIN,
                ACTION_LOGIN_ERROR,
                LABEL_PASSWORD
        );
    }

    public void eventLoginErrorEmail() {
        analyticTracker.sendEventTracking(
                EVENT_LOGIN_ERROR,
                CATEGORY_LOGIN,
                ACTION_LOGIN_ERROR,
                LABEL_EMAIL
        );
    }

    public void eventSuccessLoginEmail() {
        analyticTracker.sendEventTracking(
                EVENT_LOGIN_SUCCESS,
                CATEGORY_LOGIN,
                ACTION_LOGIN_SUCCESS,
                LABEL_EMAIL
        );
    }

    public void eventSuccessLoginSosmed(String loginMethod) {
        analyticTracker.sendEventTracking(
                EVENT_REGISTER_LOGIN,
                CATEGORY_LOGIN,
                ACTION_REGISTER,
                loginMethod
        );
    }

    public void eventClickLoginWebview(String name) {
        analyticTracker.sendEventTracking(
                EVENT_LOGIN_CLICK,
                CATEGORY_LOGIN,
                ACTION_CLICK,
                name
        );
    }

    public void eventClickLoginPhoneNumber() {
        analyticTracker.sendEventTracking(
                EVENT_LOGIN_CLICK,
                CATEGORY_LOGIN,
                ACTION_CLICK,
                LABEL_PHONE_NUMBER
        );
    }

    public void eventClickLoginGoogle() {
        analyticTracker.sendEventTracking(
                EVENT_LOGIN_CLICK,
                CATEGORY_LOGIN,
                ACTION_CLICK,
                LABEL_GPLUS
        );
    }

    public void eventClickLoginFacebook() {
        analyticTracker.sendEventTracking(
                EVENT_LOGIN_CLICK,
                CATEGORY_LOGIN,
                ACTION_CLICK,
                LABEL_FACEBOOK
        );
    }
}
