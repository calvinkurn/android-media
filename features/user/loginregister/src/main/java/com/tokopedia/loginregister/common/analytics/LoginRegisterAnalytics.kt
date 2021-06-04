package com.tokopedia.loginregister.common.analytics

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Patterns
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.analytics.TrackAnalytics
import com.tokopedia.analytics.firebase.FirebaseEvent
import com.tokopedia.analytics.firebase.FirebaseParams
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.linker.LinkerConstants
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.model.UserData
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import kotlin.collections.HashMap

import com.tokopedia.iris.util.*
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority

/**
 * @author by nisie on 10/2/18.
 * https://docs.google.com/spreadsheets/d/1TCCs1XsXFtRZR8cO4ZQEDwOHCqCTVMXjDApzZ-fLEC8
 *
 * https://docs.google.com/spreadsheets/d/1F3IQYqqG62aSxNbeFvrxyy-Pu--ZrShh8ewMKELeKj4/edit?ts=5cca711b#gid=910823048
 */
class LoginRegisterAnalytics @Inject constructor(
        val userSession: UserSessionInterface,
        val irisSession: IrisSession
) {

    fun trackScreen(activity: Activity, screenName: String) {
        val screenNameMessage =  " $screenName | ${Build.FINGERPRINT} | ${Build.MANUFACTURER} | ${Build.BRAND} | ${Build.DEVICE} | ${Build.PRODUCT} | ${Build.MODEL} | ${Build.TAGS}"
        ServerLogger.log(Priority.P2, "FINGERPRINT", mapOf("screenName" to screenNameMessage))

        val hashMap = mutableMapOf<String, String>()
        hashMap[KEY_SESSION_IRIS] = irisSession.getSessionId()
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName, hashMap)
    }

    //#3
    fun trackClickOnNext(inputText: String) {

        val hashMap: Map<String, Any>
        when {
            Patterns.EMAIL_ADDRESS.matcher(inputText).matches() -> {
                hashMap = TrackAppUtils.gtmData(
                        EVENT_CLICK_LOGIN,
                        CATEGORY_LOGIN_PAGE,
                        ACTION_CLICK_ON_LOGIN_WITH_EMAIL,
                        "click - login"
                )

                if(!hashMap.containsKey(KEY_SESSION_IRIS)){
                    hashMap[KEY_SESSION_IRIS] = irisSession.getSessionId()
                }
            }
            Patterns.PHONE.matcher(inputText).matches() -> hashMap = TrackAppUtils.gtmData(
                    EVENT_CLICK_LOGIN,
                    CATEGORY_LOGIN_PAGE,
                    ACTION_CLICK_ON_LOGIN_WITH_PHONE,
                    "click - login"
            )
            else -> {
                hashMap = TrackAppUtils.gtmData(
                        EVENT_CLICK_LOGIN,
                        CATEGORY_LOGIN_PAGE,
                        String.format("click on button selanjutnya - %s", "unknown"),
                        "click"
                )

                if(!hashMap.containsKey(KEY_SESSION_IRIS)){
                    hashMap[KEY_SESSION_IRIS] = irisSession.getSessionId()
                }
            }
        }

        hashMap["user_id"] = userSession.userId

        TrackApp.getInstance().gtm.sendGeneralEvent(hashMap)

    }

    //#3
    fun trackClickOnNextFail(inputText: String, errorMessage: String) {

        val hashMap: Map<String, Any>

        when {
            Patterns.EMAIL_ADDRESS.matcher(inputText).matches() -> {
                hashMap = TrackAppUtils.gtmData(
                        EVENT_CLICK_LOGIN,
                        CATEGORY_LOGIN_PAGE,
                        String.format("click on button selanjutnya - %s", "email"),
                        String.format("failed - %s", errorMessage)
                )


            }
            Patterns.PHONE.matcher(inputText).matches() -> hashMap = TrackAppUtils.gtmData(
                    EVENT_CLICK_LOGIN,
                    CATEGORY_LOGIN_PAGE,
                    "enter login phone number",
                    String.format("failed - %s", errorMessage)
            )
            else -> {
                hashMap = TrackAppUtils.gtmData(
                        EVENT_CLICK_LOGIN,
                        CATEGORY_LOGIN_PAGE,
                        String.format("click on button selanjutnya - %s", "unknown"),
                        String.format("failed - %s", errorMessage)
                )

                if(!hashMap.containsKey(KEY_SESSION_IRIS)){
                    hashMap[KEY_SESSION_IRIS] = irisSession.getSessionId()
                }
            }
        }

        hashMap["user_id"] = userSession.userId
        TrackApp.getInstance().gtm.sendGeneralEvent(hashMap)
    }

    //#3
    fun trackClickOnNextSuccess(inputText: String) {

        val hashMap: Map<String, Any>

        when {
            Patterns.EMAIL_ADDRESS.matcher(inputText).matches() -> {
                hashMap = TrackAppUtils.gtmData(
                        EVENT_CLICK_LOGIN,
                        CATEGORY_LOGIN_PAGE,
                        String.format("click on button selanjutnya - %s", "email"),
                        "success"
                )

                if(!hashMap.containsKey(KEY_SESSION_IRIS)){
                    hashMap[KEY_SESSION_IRIS] = irisSession.getSessionId()
                }
            }
            Patterns.PHONE.matcher(inputText).matches() -> hashMap = TrackAppUtils.gtmData(
                    EVENT_CLICK_LOGIN,
                    CATEGORY_LOGIN_PAGE,
                    "enter login phone number",
                    "success"
            )
            else -> {
                hashMap = TrackAppUtils.gtmData(
                        EVENT_CLICK_LOGIN,
                        CATEGORY_LOGIN_PAGE,
                        String.format("click on button selanjutnya - %s", "unknown"),
                        "success"
                )

                if(!hashMap.containsKey(KEY_SESSION_IRIS)){
                    hashMap[KEY_SESSION_IRIS] = irisSession.getSessionId()
                }
            }
        }
        hashMap["user_id"] = userSession.userId
        TrackApp.getInstance().gtm.sendGeneralEvent(hashMap)
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
                ACTION_CLICK_ON_LOGIN_WITH_PHONE,
                "success - login"
        ))
    }

    //#5
    fun trackLoginPhoneNumberFailed(errorMessage: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "enter login phone number",
                String.format("failed - %s", errorMessage)
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
        val hashMap = TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click on button masuk",
                "click"
        )

        if(!hashMap.containsKey(KEY_SESSION_IRIS)){
            hashMap[KEY_SESSION_IRIS] = irisSession.getSessionId()
        }

        TrackApp.getInstance().gtm.sendGeneralEvent(hashMap)

        val map = HashMap<String, Any>()
        TrackAnalytics.sendEvent(FirebaseEvent.Home.LOGIN_PAGE_CLICK_LOGIN,
                map, applicationContext)

    }


    fun eventClickSmartLock(applicationContext: Context?) {
        val hashmap = TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE_SMART_LOCK,
                "click on email login smart lock",
                "click"
        )
        hashmap.put("user_id", userSession.userId)
        TrackApp.getInstance().gtm.sendGeneralEvent(hashmap)

        val map = HashMap<String, Any>()
        TrackAnalytics.sendEvent(FirebaseEvent.Home.LOGIN_PAGE_CLICK_LOGIN,
                map, applicationContext)
    }

    //#11
    fun trackClickOnLoginButtonSuccess(isWithSq: Boolean) {
        val hashMap = TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click on masuk dengan email",
                if(isWithSq) "success - login - sq" else "success - login - non sq"
        )

        if(!hashMap.containsKey(KEY_SESSION_IRIS)){
            hashMap[KEY_SESSION_IRIS] = irisSession.getSessionId()
        }

        TrackApp.getInstance().gtm.sendGeneralEvent(hashMap)
    }

    //#12
    fun eventClickLoginGoogle(applicationContext: Context) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                ACTION_LOGIN_GOOGLE,
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
                ACTION_LOGIN_FACEBOOK,
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

    fun eventSuccessRegisterEmail(context: Context?, userId: String, name: String, email: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_REGISTER_SUCCESS,
                CATEGORY_REGISTER,
                ACTION_REGISTER_SUCCESS,
                LABEL_EMAIL
        ))

        TrackApp.getInstance().appsFlyer.sendAppsflyerRegisterEvent(userId, "Email")
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


    fun eventSuccessLogin(actionLoginMethod: String, isFromRegister: Boolean, isWithSq: Boolean) {
        when (actionLoginMethod) {
            UserSessionInterface.LOGIN_METHOD_EMAIL -> {
                if (!isFromRegister) {
                    onSuccessLoginWithEmail(isWithSq)
                } else {
                    onSuccessLoginWithEmailSmartRegister()
                }
            }
            UserSessionInterface.LOGIN_METHOD_FACEBOOK -> {
                if (!isFromRegister) {
                    onSuccessLoginWithFacebook()
                } else {
                    onSuccessLoginWithFacebookSmartRegister()
                }
            }
            UserSessionInterface.LOGIN_METHOD_GOOGLE -> {
                if (!isFromRegister) {
                    onSuccessLoginWithGoogle()
                } else {
                    onSuccessLoginWithGoogleSmartRegister()
                }
            }
            UserSessionInterface.LOGIN_METHOD_PHONE -> {
                if (!isFromRegister) {
                    onSuccessLoginWithPhone()
                } else {
                    onSuccessLoginWithPhoneSmartRegister()
                }
            }
            UserSessionInterface.LOGIN_METHOD_EMAIL_SMART_LOCK -> onSuccessLoginWithSmartLock()

        }
    }

    private fun onSuccessLoginWithSmartLock() {
        val hashmap = TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE_SMART_LOCK,
                "click on email login smart lock",
                "success"
        )
        hashmap["user_id"] = userSession.userId

        TrackApp.getInstance().gtm.sendGeneralEvent(hashmap)
    }

    private fun onSuccessLoginWithPhone() {

        trackLoginPhoneNumberSuccess()

        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                UserSessionInterface.LOGIN_METHOD_PHONE,
                "success"
        ))
    }

    private fun onSuccessLoginWithPhoneSmartRegister() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_ON_BUTTON_DAFTAR_PHONE,
                LABEL_LOGIN_SUCCESS
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

    private fun onSuccessLoginWithFacebookSmartRegister() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_LOGIN_FACEBOOK,
                LABEL_LOGIN_SUCCESS
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

    private fun onSuccessLoginWithGoogleSmartRegister() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_LOGIN_GOOGLE,
                LABEL_LOGIN_SUCCESS
        ))
    }

    private fun onSuccessLoginWithEmail(isWithSq: Boolean = false) {
        trackClickOnLoginButtonSuccess(isWithSq)
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_LOGIN_SUCCESS,
                CATEGORY_LOGIN,
                ACTION_LOGIN_SUCCESS,
                LABEL_EMAIL
        ))
    }

    private fun onSuccessLoginWithEmailSmartRegister() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_ON_BUTTON_DAFTAR_EMAIL,
                LABEL_LOGIN_SUCCESS
        ))
    }

    fun eventFailedLogin(actionLoginMethod: String, errorMessage: String?, isFromRegister: Boolean = false) {

        when (actionLoginMethod) {
            UserSessionInterface.LOGIN_METHOD_EMAIL -> {
                if (isFromRegister) {
                    onErrorLoginWithEmailSmartRegister(errorMessage)
                } else {
                    onErrorLoginWithEmail(errorMessage)
                }
            }
            UserSessionInterface.LOGIN_METHOD_PHONE -> {
                if (isFromRegister) {
                    onErrorLoginWithPhoneSmartRegister(errorMessage)
                } else {
                    onErrorLoginWithPhone(errorMessage)
                }
            }
            UserSessionInterface.LOGIN_METHOD_FACEBOOK -> {
                if (isFromRegister) {
                    onErrorLoginWithFacebookSmartRegister(errorMessage)
                } else {
                    onErrorLoginWithFacebook(errorMessage)
                }
            }
            UserSessionInterface.LOGIN_METHOD_GOOGLE -> {
                if (isFromRegister) {
                    onErrorLoginWithGoogleSmartRegister(errorMessage)
                } else {
                    onErrorLoginWithGoogle(errorMessage)
                }
            }
            UserSessionInterface.LOGIN_METHOD_EMAIL_SMART_LOCK -> {
                onErrorLoginWithSmartLock(errorMessage)
            }

        }
    }

    private fun onErrorLoginWithEmailSmartRegister(errorMessage: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_ON_BUTTON_DAFTAR_EMAIL,
                LABEL_FAILED + errorMessage
        ))
    }

    private fun onErrorLoginWithPhoneSmartRegister(errorMessage: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_ON_BUTTON_DAFTAR_PHONE,
                LABEL_FAILED + errorMessage
        ))
    }

    private fun onErrorLoginWithFacebookSmartRegister(errorMessage: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                "click on button $FACEBOOK",
                LABEL_FAILED + errorMessage
        ))
    }

    private fun onErrorLoginWithGoogleSmartRegister(errorMessage: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                "click on button $GOOGLE",
                LABEL_FAILED + errorMessage
        ))
    }

    private fun onErrorLoginWithSmartLock(errorMessage: String?) {
        val hashmap = TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE_SMART_LOCK,
                "click on email login smart lock",
                String.format("failed - %s", errorMessage)
        )
        hashmap["user_id"] = userSession.userId

        TrackApp.getInstance().gtm.sendGeneralEvent(hashmap)
    }

    private fun onErrorLoginWithPhone(errorMessage: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                ACTION_CLICK_ON_LOGIN_WITH_PHONE,
                LABEL_FAILED + errorMessage
        ))
    }

    //#13
    private fun onErrorLoginWithFacebook(errorMessage: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click on masuk dengan $FACEBOOK",
                String.format("failed - %s", errorMessage)
        ))
    }

    //#12
    private fun onErrorLoginWithGoogle(errorMessage: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click on masuk dengan $GOOGLE",
                String.format("failed - %s", errorMessage)
        ))
    }

    private fun onErrorLoginWithEmail(errorMessage: String?) {
        val hashMap = TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                ACTION_CLICK_ON_LOGIN_WITH_EMAIL,
                String.format("failed - %s", errorMessage)
        )

        if(!hashMap.containsKey(KEY_SESSION_IRIS)){
            hashMap[KEY_SESSION_IRIS] = irisSession.getSessionId()
        }

        TrackApp.getInstance().gtm.sendGeneralEvent(hashMap)
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

    fun eventClickSocmedButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                ACTION_CLICK_ON_BUTTON_SOCMED,
                ""
        ))
    }

    fun eventClickCloseSocmedButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                ACTION_CLICK_ON_BUTTON_CLOSE_SOCMED,
                ""
        ))
    }

    fun eventClickYesSmartLoginDialogButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                ACTION_CLICK_ON_BUTTON_POPUP_SMART_LOGIN,
                LABEL_YES + "email"
        ))
    }

    fun eventClickNoSmartLoginDialogButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                ACTION_CLICK_ON_BUTTON_POPUP_SMART_LOGIN,
                LABEL_NO + "email"
        ))
    }

    fun eventViewBanner(label: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_VIEW_LOGIN_IRIS,
                CATEGORY_LOGIN_PAGE,
                ACTION_VIEW_BANNER,
                label
        ))
    }

    fun logUnknownError(message: Throwable) {
        try {
            FirebaseCrashlytics.getInstance().recordException(message)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }

    }

    fun getLoginMethodMoengage(loginMethod: String?): String? {
        return when (loginMethod) {
            UserSessionInterface.LOGIN_METHOD_EMAIL_SMART_LOCK -> "Email"
            UserSessionInterface.LOGIN_METHOD_EMAIL -> "Email"
            UserSessionInterface.LOGIN_METHOD_FACEBOOK -> "Facebook"
            UserSessionInterface.LOGIN_METHOD_GOOGLE -> "Google"
            UserSessionInterface.LOGIN_METHOD_PHONE -> "Phone Number"
            else -> loginMethod
        }
    }

    fun trackerOnPhoneNumberNotExist() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_LOGIN_CLICK,
                CATEGORY_LOGIN_PAGE,
                ACTION_CLICK_ON_LOGIN_WITH_PHONE,
                "click - register"
        ))
    }

    fun trackerOnEmailNotExist() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_LOGIN_CLICK,
                CATEGORY_LOGIN_PAGE,
                ACTION_CLICK_ON_LOGIN_WITH_EMAIL,
                "click - register"
        ))
    }

    fun trackerSuccessRegisterFromLogin(loginMethod: String) {
        when(loginMethod) {
            UserSessionInterface.LOGIN_METHOD_PHONE -> {
                trackerSuccessRegisterSmartLoginPhone()
            }
            UserSessionInterface.LOGIN_METHOD_EMAIL -> {
                trackerSuccessRegisterSmartLoginEmail()
            }
            UserSessionInterface.LOGIN_METHOD_GOOGLE -> {
                trackerSuccessRegisterSmartLoginGoogle()
            }
            UserSessionInterface.LOGIN_METHOD_FACEBOOK -> {
                trackerSuccessRegisterSmartLoginFacebook()
            }
        }
    }

    private fun trackerSuccessRegisterSmartLoginPhone() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_LOGIN_CLICK,
                CATEGORY_LOGIN_PAGE,
                ACTION_CLICK_ON_LOGIN_WITH_PHONE,
                "success - register"
        ))
    }
    private fun trackerSuccessRegisterSmartLoginEmail() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_LOGIN_CLICK,
                CATEGORY_LOGIN_PAGE,
                ACTION_CLICK_ON_LOGIN_WITH_EMAIL,
                "success - register"
        ))
    }
    private fun trackerSuccessRegisterSmartLoginGoogle() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_LOGIN_CLICK,
                CATEGORY_LOGIN_PAGE,
                ACTION_LOGIN_GOOGLE,
                "success"
        ))
    }
    private fun trackerSuccessRegisterSmartLoginFacebook() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_LOGIN_CLICK,
                CATEGORY_LOGIN_PAGE,
                ACTION_LOGIN_FACEBOOK,
                "success"
        ))
    }

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
        private val EVENT_REGISTER_SUCCESS = "registerSuccess"
        private val EVENT_CLICK_HOME_PAGE = "clickHomePage"
        private val EVENT_CLICK_USER_PROFILE = "clickUserProfile"
        private val EVENT_VIEW_LOGIN_IRIS = "viewLoginIris"

        private val CATEGORY_LOGIN = "Login"
        private val CATEGORY_SMART_LOCK = "Smart Lock"
        private val CATEGORY_ACTIVATION_PAGE = "activation page"
        private val CATEGORY_REGISTER = "Register"
        private val CATEGORY_REGISTER_PAGE = "register page"
        private val CATEGORY_WELCOME_PAGE = "welcome page"
        private val CATEGORY_LOGIN_PAGE = "login page"
        private val CATEGORY_LOGIN_PAGE_SMART_LOCK = "login page smart lock"


        private val ACTION_REGISTER = "Register"
        private val ACTION_LOGIN_ERROR = "Login Error"
        private val ACTION_LOGIN_SUCCESS = "Login Success"
        private val ACTION_SUCCESS = "Success"
        private val ACTION_CLICK_CHANNEL = "Click Channel"
        private val ACTION_REGISTER_SUCCESS = "Register Success"
        private val ACTION_LOGIN_EMAIL = "click on button masuk"
        private val ACTION_LOGIN_FACEBOOK = "click on masuk dengan facebook"
        private val ACTION_LOGIN_GOOGLE = "click on masuk dengan google"
        private val ACTION_TICKER_LOGIN = "click on ticker login"
        private val ACTION_LINK_TICKER_LOGIN = "click ticker link"
        private val ACTION_CLOSE_TICKER_LOGIN = "click on button close ticker"
        private val ACTION_CLICK_ON_BUTTON_SOCMED = "click on button socmed"
        private val ACTION_CLICK_ON_BUTTON_CLOSE_SOCMED = "click on button close socmed"
        private val ACTION_CLICK_ON_BUTTON_POPUP_SMART_LOGIN = "click on button popup smart login"
        private val ACTION_VIEW_BANNER = "view banner"
        private val ACTION_CLICK_ON_BUTTON_DAFTAR_EMAIL = "click on button daftar - email"
        private val ACTION_CLICK_ON_BUTTON_DAFTAR_PHONE = "click on button daftar - phone number"
        private val ACTION_CLICK_ON_LOGIN_WITH_PHONE = "click on masuk dengan phone number"
        private val ACTION_CLICK_ON_LOGIN_WITH_EMAIL = "click on masuk dengan email"

        private val LABEL_REGISTER = "Register"
        private val LABEL_PASSWORD = "Kata Sandi"
        val LABEL_EMAIL = "Email"
        private val LABEL_GPLUS = "Google Plus"
        val LABEL_FACEBOOK = "Facebook"
        private val LABEL_SAVE_PASSWORD = "Save Password"
        private val LABEL_NEVER_SAVE_PASSWORD = "Never"
        val LABEL_GMAIL = "Gmail"
        private val LABEL_YES = "yes - "
        private val LABEL_NO = "no - "
        private val LABEL_BEBAS_ONGKIR = "bebas ongkir"
        private val LABEL_LOGIN_SUCCESS = "login success"
        private val LABEL_FAILED = "failed - "

        val GOOGLE = "google"
        val FACEBOOK = "facebook"
    }
}
