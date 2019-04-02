package com.tokopedia.loginregister.common.analytics;

import android.app.Activity;
import android.content.Context;

import com.tokopedia.analytics.TrackAnalytics;
import com.tokopedia.analytics.firebase.FirebaseEvent;
import com.tokopedia.analytics.firebase.FirebaseParams;
import com.tokopedia.loginregister.LoginRegisterRouter;

import java.util.HashMap;
import java.util.Map;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;

/**
 * @author by nisie on 10/2/18.
 * https://docs.google.com/spreadsheets/d/1TCCs1XsXFtRZR8cO4ZQEDwOHCqCTVMXjDApzZ-fLEC8
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

    public LoginRegisterAnalytics() {
    }

    public void trackScreen(Activity activity, String screenName) {
        TrackApp.getInstance().getGTM().sendScreenAuthenticated(screenName);
    }

    //#3
    public void trackClickOnNext() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click on button selanjutnya",
                "click"
        ));
    }

    //#3
    public void trackClickOnNextFail() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click on button selanjutnya",
                "failed validasi"
        ));
    }

    //#3
    public void trackClickOnNextSuccess() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click on button selanjutnya",
                "success validasi"
        ));
    }

    //#5
    public void trackLoginPhoneNumber() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "enter login phone number",
                "click"
        ));
    }

    //#5
    public void trackLoginPhoneNumberSuccess() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "enter login phone number",
                "success"
        ));
    }

    //#5
    public void trackLoginPhoneNumberFailed() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "enter login phone number",
                "failed"
        ));
    }

    //#5
    public void trackChangeButtonClicked() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click on button ubah",
                ""
        ));
    }

    //#5
    public void eventClickPasswordHide() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click on hide kata sandi",
                ""
        ));
    }

    //#6
    public void eventClickPasswordShow() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click on unhide kata sandi",
                ""
        ));
    }

    //#7
    public void trackClickForgotPassword() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click on lupa kata sandi",
                ""
        ));
    }

    //#8
    public void trackClickRegisterOnFooter() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click on button daftar bottom",
                ""
        ));
    }

    //#9
    public void trackClickRegisterOnMenu() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click on button daftar top",
                ""
        ));
    }

    //#11
    public void eventClickLoginButton(Context applicationContext) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click on button masuk",
                "click"
        ));

        Map<String, Object> map = new HashMap<>();
        TrackAnalytics.sendEvent(FirebaseEvent.Home.LOGIN_PAGE_CLICK_LOGIN,
                map, applicationContext);

    }

    //#11
    public void trackClickOnLoginButtonSuccess() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click on button masuk",
                "success"
        ));
    }

    //#11
    public void trackClickOnLoginButtonError() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click on button masuk",
                "failed"
        ));
    }


    //#12
    public void eventClickLoginGoogle(Context applicationContext) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click on button google",
                "click"
        ));

        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, "GoogleSignInActivity");
        TrackAnalytics.sendEvent(FirebaseEvent.Home.LOGIN_PAGE_CLICK_LOGIN_GOOGLE,
                map, applicationContext);
    }

    //#13
    public void eventClickLoginFacebook(Context applicationContext) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click on button facebook",
                "click"
        ));

        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, "Facebook");
        TrackAnalytics.sendEvent(FirebaseEvent.Home.LOGIN_PAGE_CLICK_LOGIN_FACEBOOK,
                map, applicationContext);
    }

    //#14
    public void eventClickLoginWebview(String name) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click on button yahoo",
                "click"
        ));

        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_LOGIN_CLICK,
                CATEGORY_LOGIN,
                ACTION_CLICK,
                name
        ));
    }

    //#12, 13, 14
    public void trackEventSuccessLoginSosmed(String loginMethod) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click on button " + loginMethod,
                "success"
        ));
    }

    //#12, 13, 14
    public void trackEventFailedLoginSosmed(String loginMethod) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click on button " + loginMethod,
                "failed"
        ));
    }

    //#15
    public void trackOnBackPressed() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click back",
                ""
        ));
    }


    public void eventClickRegisterFromLogin() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_REGISTER_LOGIN,
                CATEGORY_LOGIN,
                ACTION_REGISTER,
                LABEL_REGISTER
        ));
    }

    public void eventLoginErrorPassword() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_LOGIN_ERROR,
                CATEGORY_LOGIN,
                ACTION_LOGIN_ERROR,
                LABEL_PASSWORD
        ));
    }

    public void eventLoginErrorEmail() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_LOGIN_ERROR,
                CATEGORY_LOGIN,
                ACTION_LOGIN_ERROR,
                LABEL_EMAIL
        ));
    }

    public void eventSuccessLoginEmail() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_LOGIN_SUCCESS,
                CATEGORY_LOGIN,
                ACTION_LOGIN_SUCCESS,
                LABEL_EMAIL
        ));
    }

    public void eventClickLoginPhoneNumber(Context applicationContext) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_LOGIN_CLICK,
                CATEGORY_LOGIN_PAGE,
                "click on button phone number",
                ""
        ));

        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, "LoginPhoneNumberActivity");
        TrackAnalytics.sendEvent(FirebaseEvent.Home.LOGIN_PAGE_CLICK_LOGIN_PHONE,
                map, applicationContext);
    }


    public void eventClickForgotPasswordFromLogin(Context applicationContext) {
        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, "ForgotPasswordActivity");
        TrackAnalytics.sendEvent(FirebaseEvent.Home.LOGIN_PAGE_CLICK_FORGOT_PASSWORD,
                map, applicationContext);

    }

    public void eventSmartLockSaveCredential() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_SUCCESS_SMART_LOCK,
                CATEGORY_SMART_LOCK,
                ACTION_SUCCESS,
                LABEL_SAVE_PASSWORD
        ));
    }

    public void eventSmartLockNeverSaveCredential() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_SUCCESS_SMART_LOCK,
                CATEGORY_SMART_LOCK,
                ACTION_SUCCESS,
                LABEL_NEVER_SAVE_PASSWORD
        ));
    }

    public void eventClickBackEmailActivation() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_BACK,
                CATEGORY_ACTIVATION_PAGE,
                "click back button",
                ""
        ));
    }

    public void eventClickActivateEmail() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_CONFIRM,
                CATEGORY_ACTIVATION_PAGE,
                "click on aktivasi",
                ""
        ));
    }

    public void eventClickResendActivationEmail() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_ACTIVATION_PAGE,
                "click on kirim ulang",
                ""
        ));
    }

    public void eventClickRegisterEmail() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER,
                ACTION_CLICK_CHANNEL,
                LABEL_EMAIL
        ));

    }

    public void eventClickOnLoginFromRegister() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_REGISTER_PAGE,
                "click on masuk",
                ""
        ));
    }

    public void eventClickRegisterFacebook(Context applicationContext) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER,
                ACTION_CLICK_CHANNEL,
                LABEL_FACEBOOK
        ));

        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, "Facebook");
        TrackAnalytics.sendEvent(FirebaseEvent.Home.SIGNUP_PAGE_CLICK_FACEBOOK,
                map, applicationContext);

    }

    public void eventClickRegisterGoogle(Context applicationContext) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER,
                ACTION_CLICK_CHANNEL,
                LABEL_GPLUS
        ));

        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, "GoogleSignInActivity");
        TrackAnalytics.sendEvent(FirebaseEvent.Home.SIGNUP_PAGE_CLICK_GOOGLE,
                map, applicationContext);
    }

    public void eventClickRegisterWebview(Context applicationContext, String name) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER,
                ACTION_CLICK_CHANNEL,
                name
        ));
    }

    public void eventProceedEmailAlreadyRegistered() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_CONFIRM,
                CATEGORY_REGISTER_PAGE,
                "click on pop up box register (ya, masuk)",
                ""
        ));
    }

    public void eventCancelEmailAlreadyRegistered() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_CONFIRM,
                CATEGORY_REGISTER_PAGE,
                "click on pop up box register (ubah)",
                "email"
        ));
    }

    public void eventProceedRegisterWithPhoneNumber() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_CONFIRM,
                CATEGORY_REGISTER_PAGE,
                "click on pop up box register (ya, benar)",
                ""
        ));
    }

    public void eventCancelRegisterWithPhoneNumber() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_CONFIRM,
                CATEGORY_REGISTER_PAGE,
                "click on pop up box register (ubah)",
                "phone number"
        ));
    }

    public void eventClickBackRegisterWithEmail() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_BACK,
                CATEGORY_REGISTER_PAGE,
                "click back (daftar dengan email)",
                ""
        ));
    }

    public void eventRegisterWithEmail() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                "click on daftar (daftar dengan email)",
                ""
        ));
    }

    public void eventSuccessRegisterEmail(Context applicationContext, int userId, String name, String email, String phone) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_REGISTER_SUCCESS,
                CATEGORY_REGISTER,
                ACTION_REGISTER_SUCCESS,
                LABEL_EMAIL
        ));

        TrackApp.getInstance().getAppsFlyer().sendAppsflyerRegisterEvent(String.valueOf(userId), "Email");
        TrackApp.getInstance().getMoEngage().sendMoengageRegisterEvent(name, phone);
        ((LoginRegisterRouter) applicationContext).sendBranchRegisterEvent(email, phone);

    }

    public void eventSuccessRegisterSosmed(String methodName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_REGISTER_SUCCESS,
                CATEGORY_REGISTER,
                ACTION_REGISTER_SUCCESS,
                methodName
        ));
    }

    public void eventContinueFromWelcomePage() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_HOME_PAGE,
                CATEGORY_WELCOME_PAGE,
                "click on lanjut",
                ""
        ));
    }

    public void eventClickProfileCompletionFromWelcomePage() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_USER_PROFILE,
                CATEGORY_WELCOME_PAGE,
                "click on lengkapi profil",
                ""
        ));
    }


    public void eventSuccessLogin(String actionLoginMethod) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                actionLoginMethod,
                "success"
        ));
    }

    public void eventFailedLogin(String actionLoginMethod) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                actionLoginMethod,
                "failed"
        ));
    }

}
