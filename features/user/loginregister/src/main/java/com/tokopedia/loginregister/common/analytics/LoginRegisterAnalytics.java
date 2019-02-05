package com.tokopedia.loginregister.common.analytics;

import android.app.Activity;
import android.content.Context;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.analytics.TrackAnalytics;
import com.tokopedia.analytics.firebase.FirebaseEvent;
import com.tokopedia.analytics.firebase.FirebaseParams;
import com.tokopedia.loginregister.LoginRegisterRouter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author by nisie on 10/2/18.
 */
public class LoginRegisterAnalytics {

    public static final String SCREEN_LOGIN = "Login page";
    public static final String SCREEN_ACCOUNT_ACTIVATION = "Account Activation Page";
    public static final String SCREEN_REGISTER_INITIAL = "Register page";
    public static final String SCREEN_REGISTER_EMAIL = "Register with Email Page";

    private static final String EVENT_CLICK_LOGIN = "clickLogin";
    private static final String EVENT_REGISTER_LOGIN = "registerLogin";
    private static final String EVENT_LOGIN_ERROR = "loginError";
    private static final String EVENT_LOGIN_SUCCESS = "loginSuccess";
    private static final String EVENT_LOGIN_CLICK = "clickLogin";
    private static final String EVENT_SUCCESS_SMART_LOCK = "eventSuccessSmartLock";
    private static final String EVENT_CLICK_BACK = "clickBack";
    private static final String EVENT_CLICK_CONFIRM = "clickConfirm";
    private static final String EVENT_CLICK_REGISTER = "clickRegister";
    private static final String EVENT_REGISTER_SUCCESS = "registerSuccess";
    private static final String EVENT_CLICK_HOME_PAGE = "clickHomePage";
    public static final String EVENT_CLICK_USER_PROFILE = "clickUserProfile";

    private static final String CATEGORY_LOGIN = "Login";
    private static final String CATEGORY_SMART_LOCK = "Smart Lock";
    private static final String CATEGORY_ACTIVATION_PAGE = "activation page";
    private static final String CATEGORY_REGISTER = "Register";
    private static final String CATEGORY_REGISTER_PAGE = "register page";
    private static final String CATEGORY_WELCOME_PAGE = "welcome page";
    private static final String CATEGORY_LOGIN_PAGE = "login page";
    private static final String CATEGORY_LOGIN_WITH_PHONE = "login with phone";

    private static final String ACTION_CLICK = "Click";
    private static final String ACTION_REGISTER = "Register";
    private static final String ACTION_LOGIN_ERROR = "Login Error";
    private static final String ACTION_LOGIN_SUCCESS = "Login Success";
    private static final String ACTION_SUCCESS = "Success";
    private static final String ACTION_CLICK_CHANNEL = "Click Channel";
    public static final String ACTION_REGISTER_SUCCESS = "Register Success";
    public static final String ACTION_LOGIN_EMAIL = "click on button masuk";
    public static final String ACTION_LOGIN_FACEBOOK = "click on button facebook";
    public static final String ACTION_LOGIN_GOOGLE = "click on button google";
    public static final String ACTION_LOGIN_WEBVIEW = "click on button ";
    public static final String ACTION_LOGIN_PHONE = "click on masuk phone number";

    private static final String LABEL_REGISTER = "Register";
    private static final String LABEL_PASSWORD = "Kata Sandi";
    public static final String LABEL_EMAIL = "Email";
    private static final String LABEL_PHONE_NUMBER = "Phone Number";
    public static final String LABEL_GPLUS = "Google Plus";
    public static final String LABEL_FACEBOOK = "Facebook";
    private static final String LABEL_SAVE_PASSWORD = "Save Password";
    private static final String LABEL_NEVER_SAVE_PASSWORD = "Never";
    public static final String LABEL_GMAIL = "Gmail";
    public static final String LABEL_WEBVIEW = "Web View";

    public static final String WEBVIEW = "webview";
    public static final String GOOGLE = "google";
    public static final String FACEBOOK = "facebook";

    private AnalyticTracker analyticTracker;

    public LoginRegisterAnalytics(AnalyticTracker analyticTracker) {
        this.analyticTracker = analyticTracker;
    }

    public void trackScreen(Activity activity, String screenName) {
        analyticTracker.sendScreen(activity, screenName);
    }

    public void eventClickLoginButton(Context applicationContext) {

        Map<String, Object> map = new HashMap<>();
        TrackAnalytics.sendEvent(FirebaseEvent.Home.LOGIN_PAGE_CLICK_LOGIN,
                map, applicationContext);

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

    public void eventClickLoginPhoneNumber(Context applicationContext) {
        analyticTracker.sendEventTracking(
                EVENT_LOGIN_CLICK,
                CATEGORY_LOGIN_PAGE,
                "click on button phone number",
                ""
        );

        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, "LoginPhoneNumberActivity");
        TrackAnalytics.sendEvent(FirebaseEvent.Home.LOGIN_PAGE_CLICK_LOGIN_PHONE,
                map, applicationContext);
    }

    public void eventClickLoginGoogle(Context applicationContext) {

        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, "GoogleSignInActivity");
        TrackAnalytics.sendEvent(FirebaseEvent.Home.LOGIN_PAGE_CLICK_LOGIN_GOOGLE,
                map, applicationContext);
    }

    public void eventClickLoginFacebook(Context applicationContext) {

        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, "Facebook");
        TrackAnalytics.sendEvent(FirebaseEvent.Home.LOGIN_PAGE_CLICK_LOGIN_FACEBOOK,
                map, applicationContext);
    }

    public void eventClickForgotPasswordFromLogin(Context applicationContext) {
        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, "ForgotPasswordActivity");
        TrackAnalytics.sendEvent(FirebaseEvent.Home.LOGIN_PAGE_CLICK_FORGOT_PASSWORD,
                map, applicationContext);

    }

    public void eventSmartLockSaveCredential() {
        analyticTracker.sendEventTracking(
                EVENT_SUCCESS_SMART_LOCK,
                CATEGORY_SMART_LOCK,
                ACTION_SUCCESS,
                LABEL_SAVE_PASSWORD
        );
    }

    public void eventSmartLockNeverSaveCredential() {
        analyticTracker.sendEventTracking(
                EVENT_SUCCESS_SMART_LOCK,
                CATEGORY_SMART_LOCK,
                ACTION_SUCCESS,
                LABEL_NEVER_SAVE_PASSWORD
        );
    }

    public void eventClickBackEmailActivation() {
        analyticTracker.sendEventTracking(
                EVENT_CLICK_BACK,
                CATEGORY_ACTIVATION_PAGE,
                "click back button",
                ""
        );
    }

    public void eventClickActivateEmail() {
        analyticTracker.sendEventTracking(
                EVENT_CLICK_CONFIRM,
                CATEGORY_ACTIVATION_PAGE,
                "click on aktivasi",
                ""
        );
    }

    public void eventClickResendActivationEmail() {
        analyticTracker.sendEventTracking(
                EVENT_CLICK_REGISTER,
                CATEGORY_ACTIVATION_PAGE,
                "click on kirim ulang",
                ""
        );
    }

    public void eventClickRegisterEmail() {
        analyticTracker.sendEventTracking(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER,
                ACTION_CLICK_CHANNEL,
                LABEL_EMAIL
        );

    }

    public void eventClickOnLoginFromRegister() {
        analyticTracker.sendEventTracking(
                EVENT_CLICK_LOGIN,
                CATEGORY_REGISTER_PAGE,
                "click on masuk",
                ""
        );
    }

    public void eventClickRegisterFacebook(Context applicationContext) {
        analyticTracker.sendEventTracking(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER,
                ACTION_CLICK_CHANNEL,
                LABEL_FACEBOOK
        );

        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, "Facebook");
        TrackAnalytics.sendEvent(FirebaseEvent.Home.SIGNUP_PAGE_CLICK_FACEBOOK,
                map, applicationContext);

    }

    public void eventClickRegisterGoogle(Context applicationContext) {
        analyticTracker.sendEventTracking(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER,
                ACTION_CLICK_CHANNEL,
                LABEL_GPLUS
        );

        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, "GoogleSignInActivity");
        TrackAnalytics.sendEvent(FirebaseEvent.Home.SIGNUP_PAGE_CLICK_GOOGLE,
                map, applicationContext);
    }

    public void eventClickRegisterWebview(Context applicationContext, String name) {
        analyticTracker.sendEventTracking(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER,
                ACTION_CLICK_CHANNEL,
                name
        );
    }

    public void eventProceedEmailAlreadyRegistered() {
        analyticTracker.sendEventTracking(
                EVENT_CLICK_CONFIRM,
                CATEGORY_REGISTER_PAGE,
                "click on pop up box register (ya, masuk)",
                ""
        );
    }

    public void eventCancelEmailAlreadyRegistered() {
        analyticTracker.sendEventTracking(
                EVENT_CLICK_CONFIRM,
                CATEGORY_REGISTER_PAGE,
                "click on pop up box register (ubah)",
                "email"
        );
    }

    public void eventProceedRegisterWithPhoneNumber() {
        analyticTracker.sendEventTracking(
                EVENT_CLICK_CONFIRM,
                CATEGORY_REGISTER_PAGE,
                "click on pop up box register (ya, benar)",
                ""
        );
    }

    public void eventCancelRegisterWithPhoneNumber() {
        analyticTracker.sendEventTracking(
                EVENT_CLICK_CONFIRM,
                CATEGORY_REGISTER_PAGE,
                "click on pop up box register (ubah)",
                "phone number"
        );
    }

    public void eventClickBackRegisterWithEmail() {
        analyticTracker.sendEventTracking(
                EVENT_CLICK_BACK,
                CATEGORY_REGISTER_PAGE,
                "click back (daftar dengan email)",
                ""
        );
    }

    public void eventRegisterWithEmail() {
        analyticTracker.sendEventTracking(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                "click on daftar (daftar dengan email)",
                ""
        );
    }

    public void eventSuccessRegisterEmail(Context applicationContext, int userId, String name, String email, String phone) {
        analyticTracker.sendEventTracking(
                EVENT_REGISTER_SUCCESS,
                CATEGORY_REGISTER,
                ACTION_REGISTER_SUCCESS,
                LABEL_EMAIL
        );

        ((LoginRegisterRouter) applicationContext)
                .sendAFCompleteRegistrationEvent(userId, "Email");

        ((LoginRegisterRouter) applicationContext).eventMoRegister(name, phone);
        ((LoginRegisterRouter) applicationContext).sendBranchRegisterEvent(email, phone);

    }

    public void eventSuccessRegisterSosmed(String methodName) {
        analyticTracker.sendEventTracking(
                EVENT_REGISTER_SUCCESS,
                CATEGORY_REGISTER,
                ACTION_REGISTER_SUCCESS,
                methodName
        );
    }

    public void eventContinueFromWelcomePage() {
        analyticTracker.sendEventTracking(
                EVENT_CLICK_HOME_PAGE,
                CATEGORY_WELCOME_PAGE,
                "click on lanjut",
                ""
        );
    }

    public void eventClickProfileCompletionFromWelcomePage() {
        analyticTracker.sendEventTracking(
                EVENT_CLICK_USER_PROFILE,
                CATEGORY_WELCOME_PAGE,
                "click on lengkapi profil",
                ""
        );
    }


    public void eventSuccessLogin(String actionLoginMethod) {
        analyticTracker.sendEventTracking(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                actionLoginMethod,
                "success"
        );
    }

    public void eventFailedLogin(String actionLoginMethod) {
        analyticTracker.sendEventTracking(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                actionLoginMethod,
                "failed"
        );
    }

    public void trackClickOnNext() {
        analyticTracker.sendEventTracking(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click on button selanjutnya",
                "click"
        );
    }

    public void trackChangeButtonClicked() {
        analyticTracker.sendEventTracking(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click on button ubah",
                ""
        );
    }

    public void trackClickForgotPassword() {
        analyticTracker.sendEventTracking(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click on lupa kata sandi",
                ""
        );
    }

    public void trackClickRegisterOnFooter() {
        analyticTracker.sendEventTracking(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click on button daftar bottom",
                ""
        );
    }

    public void trackClickRegisterOnMenu() {
        analyticTracker.sendEventTracking(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click on button daftar top",
                ""
        );
    }

    public void trackClickOnLoginButton() {
        analyticTracker.sendEventTracking(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click on button masuk",
                ""
        );
    }

    public void trackOnBackPressed() {
        analyticTracker.sendEventTracking(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click back",
                ""
        );
    }
}
