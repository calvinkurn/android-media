package com.tokopedia.loginregister.common.analytics

import android.app.Activity
import android.content.Context

import com.crashlytics.android.Crashlytics
import com.tokopedia.analytics.TrackAnalytics
import com.tokopedia.analytics.firebase.FirebaseEvent
import com.tokopedia.analytics.firebase.FirebaseParams
import com.tokopedia.linker.LinkerConstants
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.model.UserData

import java.util.HashMap
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by nisie on 10/2/18.
 * https://docs.google.com/spreadsheets/d/1TCCs1XsXFtRZR8cO4ZQEDwOHCqCTVMXjDApzZ-fLEC8
 *
 * https://docs.google.com/spreadsheets/d/1F3IQYqqG62aSxNbeFvrxyy-Pu--ZrShh8ewMKELeKj4/edit?ts=5cca711b#gid=910823048
 */
class LoginRegisterAnalytics @Inject constructor() {

    companion object {

        val SCREEN_LOGIN = "Login page"
        val SCREEN_ACCOUNT_ACTIVATION = "Account Activation Page"
        val SCREEN_REGISTER_EMAIL = "Register with Email Page"

        private val EVENT_CLICK_LOGIN = "clickLogin"
        private val EVENT_REGISTER_LOGIN = "registerLogin"
        private val EVENT_LOGIN_ERROR = "loginError"
        private val EVENT_LOGIN_SUCCESS = "loginSuccess"
        private val EVENT_LOGIN_CLICK = "clickLogin"
        private val EVENT_SUCCESS_SMART_LOCK = "eventSuccessSmartLock"
        private val EVENT_CLICK_BACK = "clickBack"
        private val EVENT_CLICK_CONFIRM = "clickConfirm"
        private val EVENT_CLICK_REGISTER = "clickRegister"
        val EVENT_REGISTER_SUCCESS = "registerSuccess"
        private val EVENT_CLICK_HOME_PAGE = "clickHomePage"
        val EVENT_CLICK_USER_PROFILE = "clickUserProfile"

        private val CATEGORY_LOGIN = "Login"
        private val CATEGORY_SMART_LOCK = "Smart Lock"
        private val CATEGORY_ACTIVATION_PAGE = "activation page"
        val CATEGORY_REGISTER = "Register"
        private val CATEGORY_REGISTER_PAGE = "register page"
        private val CATEGORY_WELCOME_PAGE = "welcome page"
        private val CATEGORY_LOGIN_PAGE = "login page"

        private val ACTION_REGISTER = "Register"
        private val ACTION_LOGIN_ERROR = "Login Error"
        private val ACTION_LOGIN_SUCCESS = "Login Success"
        private val ACTION_SUCCESS = "Success"
        private val ACTION_CLICK_CHANNEL = "Click Channel"
        val ACTION_REGISTER_SUCCESS = "Register Success"
        private val ACTION_LOGIN_EMAIL = "click on button masuk"
        private val ACTION_LOGIN_FACEBOOK = "click on button facebook"
        private val ACTION_LOGIN_GOOGLE = "click on button google"
        private val ACTION_TICKER_LOGIN = "click on ticker login"
        private val ACTION_LINK_TICKER_LOGIN = "click ticker link"
        private val ACTION_CLOSE_TICKER_LOGIN = "click on button close ticker"

        private val LABEL_REGISTER = "Register"
        private val LABEL_PASSWORD = "Kata Sandi"
        val LABEL_EMAIL = "Email"
        val LABEL_GPLUS = "Google Plus"
        val LABEL_FACEBOOK = "Facebook"
        private val LABEL_SAVE_PASSWORD = "Save Password"
        private val LABEL_NEVER_SAVE_PASSWORD = "Never"
        val LABEL_GMAIL = "Gmail"

        val GOOGLE = "google"
        val FACEBOOK = "facebook"
    }

    fun trackScreen(activity: Activity, screenName: String) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName)
    }

    //#3
    fun trackClickOnNext() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click on button selanjutnya",
                "click"
        ))
    }

    //#3
    fun trackClickOnNextFail() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click on button selanjutnya",
                "failed validasi"
        ))
    }

    //#3
    fun trackClickOnNextSuccess() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click on button selanjutnya",
                "success validasi"
        ))
    }

    //#5
    fun trackLoginPhoneNumber() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "enter login phone number",
                "click"
        ))
    }

    //#5
    fun trackLoginPhoneNumberSuccess() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "enter login phone number",
                "success"
        ))
    }

    //#5
    fun trackLoginPhoneNumberFailed() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "enter login phone number",
                "failed"
        ))
    }

    //#5
    fun trackChangeButtonClicked() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click on button ubah",
                ""
        ))
    }

    //#5 Cannot Implement
    fun eventClickPasswordHide() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click on hide kata sandi",
                ""
        ))
    }

    //#6 Cannot Implement
    fun eventClickPasswordShow() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click on unhide kata sandi",
                ""
        ))
    }

    //#7
    fun trackClickForgotPassword() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click on lupa kata sandi",
                ""
        ))
    }

    //#11
    fun eventClickLoginEmailButton(applicationContext: Context) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click on button masuk",
                "click"
        ))

        val map = HashMap<String, Any>()
        TrackAnalytics.sendEvent(FirebaseEvent.Home.LOGIN_PAGE_CLICK_LOGIN,
                map, applicationContext)

    }

    //#11
    fun trackClickOnLoginButtonSuccess() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click on button masuk",
                "success"
        ))
    }

    //#11
    fun trackClickOnLoginButtonError() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click on button masuk",
                "failed"
        ))
    }


    //#12
    fun eventClickLoginGoogle(applicationContext: Context) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click on button google",
                "click"
        ))

        val map = HashMap<String, Any>()
        map[FirebaseParams.Home.LANDING_SCREEN_NAME] = "GoogleSignInActivity"
        TrackAnalytics.sendEvent(FirebaseEvent.Home.LOGIN_PAGE_CLICK_LOGIN_GOOGLE,
                map, applicationContext)
    }

    //#13
    fun eventClickLoginFacebook(applicationContext: Context) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click on button facebook",
                "click"
        ))

        val map = HashMap<String, Any>()
        map[FirebaseParams.Home.LANDING_SCREEN_NAME] = "Facebook"
        TrackAnalytics.sendEvent(FirebaseEvent.Home.LOGIN_PAGE_CLICK_LOGIN_FACEBOOK,
                map, applicationContext)
    }

    //#15
    fun trackOnBackPressed() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click back",
                ""
        ))
    }


    fun eventClickRegisterFromLogin() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_REGISTER_LOGIN,
                CATEGORY_LOGIN,
                ACTION_REGISTER,
                LABEL_REGISTER
        ))
    }

    fun eventLoginErrorPassword() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_LOGIN_ERROR,
                CATEGORY_LOGIN,
                ACTION_LOGIN_ERROR,
                LABEL_PASSWORD
        ))
    }

    fun eventLoginErrorEmail() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_LOGIN_ERROR,
                CATEGORY_LOGIN,
                ACTION_LOGIN_ERROR,
                LABEL_EMAIL
        ))
    }

    fun eventSuccessLoginEmail() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_LOGIN_SUCCESS,
                CATEGORY_LOGIN,
                ACTION_LOGIN_SUCCESS,
                LABEL_EMAIL
        ))
    }

    fun eventClickLoginPhoneNumber(applicationContext: Context) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_LOGIN_CLICK,
                CATEGORY_LOGIN_PAGE,
                "click on button phone number",
                ""
        ))

        val map = HashMap<String, Any>()
        map[FirebaseParams.Home.LANDING_SCREEN_NAME] = "LoginPhoneNumberActivity"
        TrackAnalytics.sendEvent(FirebaseEvent.Home.LOGIN_PAGE_CLICK_LOGIN_PHONE,
                map, applicationContext)
    }


    fun eventClickForgotPasswordFromLogin(applicationContext: Context) {
        val map = HashMap<String, Any>()
        map[FirebaseParams.Home.LANDING_SCREEN_NAME] = "ForgotPasswordActivity"
        TrackAnalytics.sendEvent(FirebaseEvent.Home.LOGIN_PAGE_CLICK_FORGOT_PASSWORD,
                map, applicationContext)

    }

    fun eventSmartLockSaveCredential() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_SUCCESS_SMART_LOCK,
                CATEGORY_SMART_LOCK,
                ACTION_SUCCESS,
                LABEL_SAVE_PASSWORD
        ))
    }

    fun eventSmartLockNeverSaveCredential() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_SUCCESS_SMART_LOCK,
                CATEGORY_SMART_LOCK,
                ACTION_SUCCESS,
                LABEL_NEVER_SAVE_PASSWORD
        ))
    }

    fun eventClickBackEmailActivation() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_BACK,
                CATEGORY_ACTIVATION_PAGE,
                "click back button",
                ""
        ))
    }

    fun eventClickActivateEmail() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_CONFIRM,
                CATEGORY_ACTIVATION_PAGE,
                "click on aktivasi",
                ""
        ))
    }

    fun eventClickResendActivationEmail() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_ACTIVATION_PAGE,
                "click on kirim ulang",
                ""
        ))
    }

    fun eventClickRegisterEmail() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER,
                ACTION_CLICK_CHANNEL,
                LABEL_EMAIL
        ))

    }

    fun eventClickOnLoginFromRegister() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_REGISTER_PAGE,
                "click on masuk",
                ""
        ))
    }

    fun eventClickRegisterFacebook(applicationContext: Context) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER,
                ACTION_CLICK_CHANNEL,
                LABEL_FACEBOOK
        ))

        val map = HashMap<String, Any>()
        map[FirebaseParams.Home.LANDING_SCREEN_NAME] = "Facebook"
        TrackAnalytics.sendEvent(FirebaseEvent.Home.SIGNUP_PAGE_CLICK_FACEBOOK,
                map, applicationContext)

    }

    fun eventClickRegisterGoogle(applicationContext: Context) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER,
                ACTION_CLICK_CHANNEL,
                LABEL_GPLUS
        ))

        val map = HashMap<String, Any>()
        map[FirebaseParams.Home.LANDING_SCREEN_NAME] = "GoogleSignInActivity"
        TrackAnalytics.sendEvent(FirebaseEvent.Home.SIGNUP_PAGE_CLICK_GOOGLE,
                map, applicationContext)
    }

    fun eventClickRegisterWebview(applicationContext: Context, name: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER,
                ACTION_CLICK_CHANNEL,
                name
        ))
    }

    fun eventProceedEmailAlreadyRegistered() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_CONFIRM,
                CATEGORY_REGISTER_PAGE,
                "click on pop up box register (ya, masuk)",
                ""
        ))
    }

    fun eventCancelEmailAlreadyRegistered() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_CONFIRM,
                CATEGORY_REGISTER_PAGE,
                "click on pop up box register (ubah)",
                "email"
        ))
    }

    fun eventProceedRegisterWithPhoneNumber() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_CONFIRM,
                CATEGORY_REGISTER_PAGE,
                "click on pop up box register (ya, benar)",
                ""
        ))
    }

    fun eventCancelRegisterWithPhoneNumber() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_CONFIRM,
                CATEGORY_REGISTER_PAGE,
                "click on pop up box register (ubah)",
                "phone number"
        ))
    }

    fun eventClickBackRegisterWithEmail() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_BACK,
                CATEGORY_REGISTER_PAGE,
                "click back (daftar dengan email)",
                ""
        ))
    }

    fun eventRegisterWithEmail() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                "click on daftar (daftar dengan email)",
                ""
        ))
    }

    fun eventSuccessRegisterEmail(applicationContext: Context, userId: Int, name: String, email: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_REGISTER_SUCCESS,
                CATEGORY_REGISTER,
                ACTION_REGISTER_SUCCESS,
                LABEL_EMAIL
        ))

        TrackApp.getInstance().appsFlyer.sendAppsflyerRegisterEvent(userId.toString(), "Email")
        TrackApp.getInstance().moEngage.sendMoengageRegisterEvent(name, "")
        sendBranchRegisterEvent(email)

    }

    private fun sendBranchRegisterEvent(email: String) {
        val userData = UserData()
        userData.email = email
        userData.phoneNumber = ""
        LinkerManager.getInstance().sendEvent(LinkerUtils.createGenericRequest(LinkerConstants.EVENT_USER_REGISTRATION_VAL, userData))
    }

    fun eventSuccessRegisterSosmed(methodName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_REGISTER_SUCCESS,
                CATEGORY_REGISTER,
                ACTION_REGISTER_SUCCESS,
                methodName
        ))
    }

    fun eventContinueFromWelcomePage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_HOME_PAGE,
                CATEGORY_WELCOME_PAGE,
                "click on lanjut",
                ""
        ))
    }

    fun eventClickProfileCompletionFromWelcomePage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_USER_PROFILE,
                CATEGORY_WELCOME_PAGE,
                "click on lengkapi profil",
                ""
        ))
    }


    fun eventSuccessLogin(actionLoginMethod: String, registerAnalytics: RegisterAnalytics) {
        when(actionLoginMethod){
            UserSessionInterface.LOGIN_METHOD_EMAIL -> onSuccessLoginWithEmail(registerAnalytics)
            UserSessionInterface.LOGIN_METHOD_FACEBOOK -> onSuccessLoginWithGoogle()
            UserSessionInterface.LOGIN_METHOD_GOOGLE -> onSuccessLoginWithFacebook()
            UserSessionInterface.LOGIN_METHOD_PHONE -> onSuccessLoginWithPhone(registerAnalytics)
        }
    }

    private fun onSuccessLoginWithPhone(registerAnalytics: RegisterAnalytics) {

        trackLoginPhoneNumberSuccess()

        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                UserSessionInterface.LOGIN_METHOD_PHONE,
                "success"
        ))
    }

    //#13
    private fun onSuccessLoginWithFacebook() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                ACTION_LOGIN_FACEBOOK,
                "success"
        ))
    }

    //#12
    private fun onSuccessLoginWithGoogle() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                ACTION_LOGIN_GOOGLE,
                "success"
        ))
    }

    private fun onSuccessLoginWithEmail(registerAnalytics: RegisterAnalytics) {
        trackClickOnLoginButtonSuccess()

        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                ACTION_LOGIN_EMAIL,
                "success"
        ))

        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_LOGIN_SUCCESS,
                CATEGORY_LOGIN,
                ACTION_LOGIN_SUCCESS,
                LABEL_EMAIL
        ))

    }

    fun eventFailedLogin(actionLoginMethod: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                actionLoginMethod,
                "failed"
        ))

        when(actionLoginMethod){
            UserSessionInterface.LOGIN_METHOD_EMAIL -> onErrorLoginWithEmail()
            UserSessionInterface.LOGIN_METHOD_FACEBOOK -> onErrorLoginWithGoogle()
            UserSessionInterface.LOGIN_METHOD_GOOGLE -> onErrorLoginWithFacebook()
            UserSessionInterface.LOGIN_METHOD_PHONE -> onErrorLoginWithPhone()
        }
    }

    private fun onErrorLoginWithPhone() {

    }

    //#13
    private fun onErrorLoginWithFacebook() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click on button $FACEBOOK",
                "failed"
        ))
    }

    //#12
    private fun onErrorLoginWithGoogle() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click on button $GOOGLE",
                "failed"
        ))
    }

    private fun onErrorLoginWithEmail() {

    }

    fun eventClickTicker() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                ACTION_TICKER_LOGIN,
                ""
        ))
    }

    fun eventClickLinkTicker(link: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                ACTION_LINK_TICKER_LOGIN,
                link
        ))
    }

    fun eventClickCloseTicker() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                ACTION_CLOSE_TICKER_LOGIN,
                ""
        ))
    }

    fun logUnknownError(message: Throwable) {
        try {
            Crashlytics.logException(message)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }

    }

}
