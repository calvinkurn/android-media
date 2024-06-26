package com.tokopedia.loginregister.common.analytics

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Patterns
import com.tokopedia.analytics.TrackAnalytics
import com.tokopedia.analytics.firebase.FirebaseEvent
import com.tokopedia.analytics.firebase.FirebaseParams
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.iris.util.KEY_SESSION_IRIS
import com.tokopedia.linker.LinkerConstants
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.model.UserData
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.sessioncommon.util.LoginSdkUtils.getClientLabelIfAvailable
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject

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

    var clientName: String = ""
    
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
                        "click - login" + getClientLabelIfAvailable(clientName)
                )

                if(!hashMap.containsKey(KEY_SESSION_IRIS)){
                    hashMap[KEY_SESSION_IRIS] = irisSession.getSessionId()
                }
            }
            Patterns.PHONE.matcher(inputText).matches() -> hashMap = TrackAppUtils.gtmData(
                    EVENT_CLICK_LOGIN,
                    CATEGORY_LOGIN_PAGE,
                    ACTION_CLICK_ON_LOGIN_WITH_PHONE,
                    "click - login" + getClientLabelIfAvailable(clientName)
            )
            else -> {
                hashMap = TrackAppUtils.gtmData(
                        EVENT_CLICK_LOGIN,
                        CATEGORY_LOGIN_PAGE,
                        String.format("click on button selanjutnya - %s", "unknown"),
                        "click" + getClientLabelIfAvailable(clientName)
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
                        String.format(Locale.getDefault(), "click on button selanjutnya - %s", "email"),
                        String.format(Locale.getDefault(), "failed - %s", errorMessage) + getClientLabelIfAvailable(clientName)
                )


            }
            Patterns.PHONE.matcher(inputText).matches() -> hashMap = TrackAppUtils.gtmData(
                    EVENT_CLICK_LOGIN,
                    CATEGORY_LOGIN_PAGE,
                    "enter login phone number",
                    String.format(Locale.getDefault(), "failed - %s", errorMessage) + getClientLabelIfAvailable(clientName)
            )
            else -> {
                hashMap = TrackAppUtils.gtmData(
                        EVENT_CLICK_LOGIN,
                        CATEGORY_LOGIN_PAGE,
                        String.format(Locale.getDefault(), "click on button selanjutnya - %s", "unknown"),
                        String.format(Locale.getDefault(), "failed - %s", errorMessage) + getClientLabelIfAvailable(clientName)
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
                        String.format(Locale.getDefault(), "click on button selanjutnya - %s", "email"),
                        "success" + getClientLabelIfAvailable(clientName)
                )

                if(!hashMap.containsKey(KEY_SESSION_IRIS)){
                    hashMap[KEY_SESSION_IRIS] = irisSession.getSessionId()
                }
            }
            Patterns.PHONE.matcher(inputText).matches() -> hashMap = TrackAppUtils.gtmData(
                    EVENT_CLICK_LOGIN,
                    CATEGORY_LOGIN_PAGE,
                    "enter login phone number",
                    "success" + getClientLabelIfAvailable(clientName)
            )
            else -> {
                hashMap = TrackAppUtils.gtmData(
                        EVENT_CLICK_LOGIN,
                        CATEGORY_LOGIN_PAGE,
                        String.format(Locale.getDefault(), "click on button selanjutnya - %s", "unknown"),
                        "success" + getClientLabelIfAvailable(clientName)
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
                "click" + getClientLabelIfAvailable(clientName)
        ))
    }

    //#5
    fun trackLoginPhoneNumberSuccess() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                ACTION_CLICK_ON_LOGIN_WITH_PHONE,
                "success - login" + getClientLabelIfAvailable(clientName)
        ))
    }

    //#5
    fun trackLoginPhoneNumberFailed(errorMessage: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "enter login phone number",
                String.format(Locale.getDefault(), "failed - %s", errorMessage) + getClientLabelIfAvailable(clientName)
        ))
    }

    //#5
    fun trackChangeButtonClicked() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click on button ubah",
                "${getClientLabelIfAvailable(clientName, removeDash = true)}"
        ))
    }

    //#5 Cannot Implement
    fun eventClickPasswordHide() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click on hide kata sandi",
                getClientLabelIfAvailable(clientName, true)
        ))
    }

    //#6 Cannot Implement
    fun eventClickPasswordShow() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click on unhide kata sandi",
                getClientLabelIfAvailable(clientName, true)
        ))
    }

    //#11
    fun eventClickLoginEmailButton(applicationContext: Context) {
        val hashMap = TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click on button masuk",
                "click" + getClientLabelIfAvailable(clientName)
        )

        if(!hashMap.containsKey(KEY_SESSION_IRIS)){
            hashMap[KEY_SESSION_IRIS] = irisSession.getSessionId()
        }

        TrackApp.getInstance().gtm.sendGeneralEvent(hashMap)

        val map = HashMap<String, Any>()
        TrackAnalytics.sendEvent(FirebaseEvent.Home.LOGIN_PAGE_CLICK_LOGIN,
                map, applicationContext)

    }

    //#11
    fun trackClickOnLoginButtonSuccess(isWithSq: Boolean) {
        var label = if(isWithSq) "success - login - sq" else "success - login - non sq"
        label+=getClientLabelIfAvailable(clientName)

        val hashMap = TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click on masuk dengan email",
                label + getClientLabelIfAvailable(clientName)
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
                "click" + getClientLabelIfAvailable(clientName)
        ))

        val map = HashMap<String, Any>()
        map[FirebaseParams.Home.LANDING_SCREEN_NAME] = "GoogleSignInActivity"
        TrackAnalytics.sendEvent(FirebaseEvent.Home.LOGIN_PAGE_CLICK_LOGIN_GOOGLE,
                map, applicationContext)
    }

    //#15
    fun trackOnBackPressed() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click back",
                getClientLabelIfAvailable(clientName, true)
        ))
    }


    fun eventClickRegisterFromLogin() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_REGISTER_LOGIN,
                CATEGORY_LOGIN,
                ACTION_REGISTER,
                LABEL_REGISTER + getClientLabelIfAvailable(clientName)
        ))
    }

    fun eventLoginErrorPassword() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_LOGIN_ERROR,
                CATEGORY_LOGIN,
                ACTION_LOGIN_ERROR,
                LABEL_PASSWORD + getClientLabelIfAvailable(clientName)
        ))
    }

    fun eventLoginErrorEmail() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_LOGIN_ERROR,
                CATEGORY_LOGIN,
                ACTION_LOGIN_ERROR,
                LABEL_EMAIL + getClientLabelIfAvailable(clientName)
        ))
    }

    fun eventSuccessLoginEmail() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_LOGIN_SUCCESS,
                CATEGORY_LOGIN,
                ACTION_LOGIN_SUCCESS,
                LABEL_EMAIL + getClientLabelIfAvailable(clientName)
        ))
    }

    fun eventClickLoginPhoneNumber(applicationContext: Context) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_LOGIN_CLICK,
                CATEGORY_LOGIN_PAGE,
                "click on button phone number",
                getClientLabelIfAvailable(clientName, true)
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

    fun eventClickBackEmailActivation() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_BACK,
                CATEGORY_ACTIVATION_PAGE,
                "click back button",
                getClientLabelIfAvailable(clientName, true)
        ))
    }

    fun eventClickActivateEmail() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_CONFIRM,
                CATEGORY_ACTIVATION_PAGE,
                "click on aktivasi",
                getClientLabelIfAvailable(clientName, true)
        ))
    }

    fun eventClickResendActivationEmail() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_ACTIVATION_PAGE,
                "click on kirim ulang",
                getClientLabelIfAvailable(clientName, true)
        ))
    }

    fun eventClickRegisterEmail() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER,
                ACTION_CLICK_CHANNEL,
                LABEL_EMAIL + getClientLabelIfAvailable(clientName)
        ))

    }

    fun eventClickOnLoginFromRegister() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_REGISTER_PAGE,
                "click on masuk",
                getClientLabelIfAvailable(clientName, true)
        ))
    }

    fun eventClickRegisterGoogle(applicationContext: Context) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER,
                ACTION_CLICK_CHANNEL,
                LABEL_GPLUS + getClientLabelIfAvailable(clientName)
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
                name + getClientLabelIfAvailable(clientName)
        ))
    }

    fun eventProceedEmailAlreadyRegistered() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_CONFIRM,
                CATEGORY_REGISTER_PAGE,
                "click on pop up box register (ya, masuk)",
                getClientLabelIfAvailable(clientName, true)
        ))
    }

    fun eventCancelEmailAlreadyRegistered() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_CONFIRM,
                CATEGORY_REGISTER_PAGE,
                "click on pop up box register (ubah)",
                "email${getClientLabelIfAvailable(clientName)}"
        ))
    }

    fun eventProceedRegisterWithPhoneNumber() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_CONFIRM,
                CATEGORY_REGISTER_PAGE,
                "click on pop up box register (ya, benar)",
                getClientLabelIfAvailable(clientName, true)
        ))
    }

    fun eventCancelRegisterWithPhoneNumber() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_CONFIRM,
                CATEGORY_REGISTER_PAGE,
                "click on pop up box register (ubah)",
                "phone number${getClientLabelIfAvailable(clientName)}"
        ))
    }

    fun eventClickBackRegisterWithEmail() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_BACK,
                CATEGORY_REGISTER_PAGE,
                "click back (daftar dengan email)",
                getClientLabelIfAvailable(clientName, true)
        ))
    }

    fun eventRegisterWithEmail() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                "click on daftar (daftar dengan email)",
                getClientLabelIfAvailable(clientName, true)
        ))
    }

    fun eventSuccessRegisterEmail(context: Context?, userId: String, name: String, email: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_REGISTER_SUCCESS,
                CATEGORY_REGISTER,
                ACTION_REGISTER_SUCCESS,
                LABEL_EMAIL + getClientLabelIfAvailable(clientName)
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
                "$methodName${getClientLabelIfAvailable(clientName)}"
        ))
    }

    fun eventContinueFromWelcomePage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_HOME_PAGE,
                CATEGORY_WELCOME_PAGE,
                "click on lanjut",
                getClientLabelIfAvailable(clientName, true)
        ))
    }

    fun eventClickProfileCompletionFromWelcomePage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_USER_PROFILE,
                CATEGORY_WELCOME_PAGE,
                "click on lengkapi profil",
                getClientLabelIfAvailable(clientName, true)
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
        }
    }

    private fun onSuccessLoginWithPhone() {

        trackLoginPhoneNumberSuccess()

        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                UserSessionInterface.LOGIN_METHOD_PHONE,
                "success${getClientLabelIfAvailable(clientName)}"
        ))
    }

    private fun onSuccessLoginWithPhoneSmartRegister() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_ON_BUTTON_DAFTAR_PHONE,
                LABEL_LOGIN_SUCCESS + getClientLabelIfAvailable(clientName)
        ))
    }

    //#12
    private fun onSuccessLoginWithGoogle() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                ACTION_LOGIN_GOOGLE,
                "success${getClientLabelIfAvailable(clientName)}"
        ))
    }

    private fun onSuccessLoginWithGoogleSmartRegister() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_LOGIN_GOOGLE,
                LABEL_LOGIN_SUCCESS + getClientLabelIfAvailable(clientName)
        ))
    }

    private fun onSuccessLoginWithEmail(isWithSq: Boolean = false) {
        trackClickOnLoginButtonSuccess(isWithSq)
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_LOGIN_SUCCESS,
                CATEGORY_LOGIN,
                ACTION_LOGIN_SUCCESS,
                LABEL_EMAIL + getClientLabelIfAvailable(clientName)
        ))
    }

    private fun onSuccessLoginWithEmailSmartRegister() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_ON_BUTTON_DAFTAR_EMAIL,
                LABEL_LOGIN_SUCCESS + getClientLabelIfAvailable(clientName)
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
            UserSessionInterface.LOGIN_METHOD_GOOGLE -> {
                if (isFromRegister) {
                    onErrorLoginWithGoogleSmartRegister(errorMessage)
                } else {
                    onErrorLoginWithGoogle(errorMessage)
                }
            }
        }
    }

    private fun onErrorLoginWithEmailSmartRegister(errorMessage: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_ON_BUTTON_DAFTAR_EMAIL,
                LABEL_FAILED + errorMessage + getClientLabelIfAvailable(clientName)
        ))
    }

    private fun onErrorLoginWithPhoneSmartRegister(errorMessage: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_ON_BUTTON_DAFTAR_PHONE,
                LABEL_FAILED + errorMessage + getClientLabelIfAvailable(clientName)
        ))
    }

    private fun onErrorLoginWithGoogleSmartRegister(errorMessage: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                "click on button $GOOGLE",
                LABEL_FAILED + errorMessage + getClientLabelIfAvailable(clientName)
        ))
    }

    private fun onErrorLoginWithPhone(errorMessage: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                ACTION_CLICK_ON_LOGIN_WITH_PHONE,
                LABEL_FAILED + errorMessage + getClientLabelIfAvailable(clientName)
        ))
    }

    //#12
    private fun onErrorLoginWithGoogle(errorMessage: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                "click on masuk dengan $GOOGLE",
                String.format(Locale.getDefault(), "failed - %s", errorMessage) + getClientLabelIfAvailable(clientName)
        ))
    }

    private fun onErrorLoginWithEmail(errorMessage: String?) {
        val hashMap = TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                ACTION_CLICK_ON_LOGIN_WITH_EMAIL,
                String.format(Locale.getDefault(), "failed - %s", errorMessage) + getClientLabelIfAvailable(clientName)
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
                getClientLabelIfAvailable(clientName, true)
        ))
    }

    fun eventClickLinkTicker(link: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                ACTION_LINK_TICKER_LOGIN,
                link + getClientLabelIfAvailable(clientName)
        ))
    }

    fun eventClickCloseTicker() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                ACTION_CLOSE_TICKER_LOGIN,
                getClientLabelIfAvailable(clientName, true)
        ))
    }

    fun eventClickSocmedButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                ACTION_CLICK_ON_BUTTON_SOCMED,
                getClientLabelIfAvailable(clientName, true)
        ))
    }

    fun eventClickCloseSocmedButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                ACTION_CLICK_ON_BUTTON_CLOSE_SOCMED,
                getClientLabelIfAvailable(clientName, true)
        ))
    }

    fun eventClickYesSmartLoginDialogButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                ACTION_CLICK_ON_BUTTON_POPUP_SMART_LOGIN,
                LABEL_YES + "email" + getClientLabelIfAvailable(clientName)
        ))
    }

    fun eventClickNoSmartLoginDialogButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                ACTION_CLICK_ON_BUTTON_POPUP_SMART_LOGIN,
                LABEL_NO + "email" + getClientLabelIfAvailable(clientName)
        ))
    }

    fun eventViewBanner(label: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_VIEW_LOGIN_IRIS,
                CATEGORY_LOGIN_PAGE,
                ACTION_VIEW_BANNER,
                label + getClientLabelIfAvailable(clientName)
        ))
    }

    fun getLoginMethodMoengage(loginMethod: String?): String? {
        return when (loginMethod) {
            UserSessionInterface.LOGIN_METHOD_EMAIL -> "Email"
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
                "click - register" + getClientLabelIfAvailable(clientName)
        ))
    }

    fun trackerOnEmailNotExist() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_LOGIN_CLICK,
                CATEGORY_LOGIN_PAGE,
                ACTION_CLICK_ON_LOGIN_WITH_EMAIL,
                "click - register" + getClientLabelIfAvailable(clientName)
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
        }
    }

    private fun trackerSuccessRegisterSmartLoginPhone() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_LOGIN_CLICK,
                CATEGORY_LOGIN_PAGE,
                ACTION_CLICK_ON_LOGIN_WITH_PHONE,
                "success - register" + getClientLabelIfAvailable(clientName)
        ))
    }
    private fun trackerSuccessRegisterSmartLoginEmail() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_LOGIN_CLICK,
                CATEGORY_LOGIN_PAGE,
                ACTION_CLICK_ON_LOGIN_WITH_EMAIL,
                "success - register" + getClientLabelIfAvailable(clientName)
        ))
    }
    private fun trackerSuccessRegisterSmartLoginGoogle() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_LOGIN_CLICK,
                CATEGORY_LOGIN_PAGE,
                ACTION_LOGIN_GOOGLE,
                "success" + getClientLabelIfAvailable(clientName)
        ))
    }

    fun eventSuccessLoginFromChooseAccount(actionLoginMethod: String, isFromRegister: Boolean) {
        // old tracker from choose account flow
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
            EVENT_CLICK_LOGIN,
            Category.LOGIN_WITH_PHONE,
            Action.LOGIN_SUCCESS,
            Label.TOKOCASH + getClientLabelIfAvailable(clientName)
        ))
    }

    /* Tracker no.9 */
    fun trackClickBiometricLoginBtn(){
        val data = TrackAppUtils.gtmData(
            EVENT_CLICK_LOGIN,
            CATEGORY_LOGIN_PAGE,
            ACTION_CLICK_LOGIN_FINGERPRINT,
            "click" + getClientLabelIfAvailable(clientName))

        data[KEY_BUSINESS_UNIT] = BUSSINESS_UNIT
        data[KEY_CURRENT_SITE] = CURRENT_SITE
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    /* Tracker no.5 */
    fun trackOnLoginFingerprintSuccess(){
        val data = TrackAppUtils.gtmData(
            EVENT_CLICK_LOGIN,
            CATEGORY_LOGIN_PAGE,
            ACTION_CLICK_LOGIN_FINGERPRINT,
            LABEL_SUCCESS + getClientLabelIfAvailable(clientName))

        data[KEY_BUSINESS_UNIT] = BUSSINESS_UNIT
        data[KEY_CURRENT_SITE] = CURRENT_SITE
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    /* Tracker no.1 - Failed */
    fun trackOnLoginFingerprintFailed(errMsg: String){
        val data = TrackAppUtils.gtmData(
            EVENT_CLICK_LOGIN,
            CATEGORY_LOGIN_PAGE,
            ACTION_CLICK_LOGIN_FINGERPRINT,
            "$LABEL_FAILED - $errMsg" + getClientLabelIfAvailable(clientName))

        data[KEY_BUSINESS_UNIT] = BUSSINESS_UNIT
        data[KEY_CURRENT_SITE] = CURRENT_SITE
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    companion object {

        private const val KEY_BUSINESS_UNIT = "businessUnit"
        private const val KEY_CURRENT_SITE = "currentSite"

        private const val BUSSINESS_UNIT = "user platform"
        private const val CURRENT_SITE = "tokopediamarketplace"


        val SCREEN_LOGIN = "Login page"
        val SCREEN_ACCOUNT_ACTIVATION = "Account Activation Page"
        val SCREEN_REGISTER_EMAIL = "Register with Email Page"

        private val EVENT_CLICK_LOGIN = "clickLogin"
        private val EVENT_REGISTER_LOGIN = "registerLogin"
        private val EVENT_LOGIN_ERROR = "loginError"
        private val EVENT_LOGIN_SUCCESS = "loginSuccess"
        private val EVENT_LOGIN_CLICK = "clickLogin"
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

        const val ACTION_CLICK_LOGIN_FINGERPRINT = "click on masuk dengan fingerprint"

        private val ACTION_REGISTER = "Register"
        private val ACTION_LOGIN_ERROR = "Login Error"
        private val ACTION_LOGIN_SUCCESS = "Login Success"
        private val ACTION_SUCCESS = "Success"
        private val ACTION_CLICK_CHANNEL = "Click Channel"
        private val ACTION_REGISTER_SUCCESS = "Register Success"
        private val ACTION_LOGIN_EMAIL = "click on button masuk"
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
        private val LABEL_SAVE_PASSWORD = "Save Password"
        private val LABEL_NEVER_SAVE_PASSWORD = "Never"
        val LABEL_GMAIL = "Gmail"
        private val LABEL_YES = "yes - "
        private val LABEL_NO = "no - "
        private val LABEL_BEBAS_ONGKIR = "bebas ongkir"
        private val LABEL_LOGIN_SUCCESS = "login success"
        private val LABEL_FAILED = "failed - "
        private val LABEL_SUCCESS = "success"

        val GOOGLE = "google"

        object Event {
            const val CLICK_REGISTER = "clickRegister"
        }

        object Category {
            const val LOGIN_WITH_PHONE = "login with phone"
            const val REGISTER_PAGE = "register page"
        }

        object Action {
            const val LOGIN_SUCCESS = "login success"
        }

        object Label {
            const val TOKOCASH = "Tokocash"
            const val REGISTER_SUCCESS = "register success"
        }
    }
}
