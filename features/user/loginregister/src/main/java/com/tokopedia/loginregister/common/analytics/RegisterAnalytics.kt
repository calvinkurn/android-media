package com.tokopedia.loginregister.common.analytics

import android.content.Context
import com.tokopedia.analytics.TrackAnalytics
import com.tokopedia.analytics.firebase.FirebaseEvent
import com.tokopedia.analytics.firebase.FirebaseParams
import com.tokopedia.linker.LinkerConstants
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.model.UserData
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject

/**
 * @author by ade on 12/04/18
 * https://docs.google.com/spreadsheets/d/1CBXovkdWu7NMkxrHIOJihMyfuRWNZvxgJd36KxLS25I/edit#gid=471355800
 */
class RegisterAnalytics @Inject constructor() {

    val MEDIUM_EMAIL = "email"
    val MEDIUM_PHONE = "phone"
    val MEDIUM_FACEBOOK = "facebook"
    val MEDIUM_GOOGLE = "google"

    //#R1
    fun trackClickTopSignUpButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                ACTION_CLICK_DAFTAR_TOP,
                LABEL_EMPTY
        ))
    }

    //#R2
    fun trackClickBottomSignUpButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                ACTION_CLICK_DAFTAR_BOTTOM,
                LABEL_EMPTY
        ))
    }

    //#R3
    fun trackClickOnBackButtonRegister() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_ON_BUTTON_BACK,
                LABEL_EMPTY
        ))
    }

    //#R4
    fun trackClickTopSignInButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_MASUK_TOP,
                LABEL_EMPTY
        ))
    }

    //#R5
    fun trackClickSignUpButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_ON_BUTTON_DAFTAR,
                LABEL_CLICK
        ))
    }

    //#R5
    fun trackFailedClickSignUpButton(failedMessage: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_ON_BUTTON_DAFTAR,
                LABEL_FAILED + failedMessage
        ))
    }

    //#R5
    fun trackClickEmailSignUpButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_ON_BUTTON_DAFTAR_EMAIL,
                LABEL_CLICK
        ))
    }

    //#R5
    fun trackSuccessClickEmailSignUpButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_ON_BUTTON_DAFTAR_EMAIL,
                LABEL_SUCCESS
        ))
    }

    //#R5
    fun trackFailedClickEmailSignUpButton(failedMessage: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_ON_BUTTON_DAFTAR_EMAIL,
                LABEL_FAILED + failedMessage
        ))
    }

    //#R5
    fun trackFailedClickEmailSignUpButtonAlreadyRegistered() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_ON_BUTTON_DAFTAR_EMAIL,
                LABEL_FAILED_POPUP_EMAIL
        ))
    }

    //#R5
    fun trackClickPhoneSignUpButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_ON_BUTTON_DAFTAR_PHONE_NUMBER,
                LABEL_CLICK
        ))
    }

    //#R5
    fun trackFailedClickPhoneSignUpButton(failedMessage: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_ON_BUTTON_DAFTAR_PHONE_NUMBER,
                LABEL_FAILED + failedMessage
        ))
    }

    //#R5
    fun trackFailedClickPhoneSignUpButtonAlreadyRegistered() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_ON_BUTTON_DAFTAR_PHONE_NUMBER,
                LABEL_FAILED_POPUP_PHONE
        ))
    }

    //#R6
    fun trackClickGoogleButton(applicationContext: Context) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_ON_BUTTON_GOOGLE,
                LABEL_CLICK
        ))

        val map = HashMap<String, Any>()
        map[FirebaseParams.Home.LANDING_SCREEN_NAME] = "GoogleSignInActivity"
        TrackAnalytics.sendEvent(FirebaseEvent.Home.LOGIN_PAGE_CLICK_LOGIN_GOOGLE,
                map, applicationContext)
    }

    //#R7
    fun trackClickFacebookButton(applicationContext: Context) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_ON_BUTTON_FACEBOOK,
                LABEL_CLICK
        ))

        val map = HashMap<String, Any>()
        map[FirebaseParams.Home.LANDING_SCREEN_NAME] = "Facebook"
        TrackAnalytics.sendEvent(FirebaseEvent.Home.LOGIN_PAGE_CLICK_LOGIN_FACEBOOK,
                map, applicationContext)
    }

    //#R8
    fun trackClickBottomSignInButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_MASUK_BOTTOM,
                LABEL_EMPTY
        ))
    }

    //#R9
    fun trackClickYesButtonRegisteredEmailDialog() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_YA_MASUK_TERDAFTAR_EMAIL,
                LABEL_CLICK
        ))
    }

    //#R10
    fun trackClickChangeButtonRegisteredEmailDialog() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_UBAH_TERDAFTAR_EMAIL,
                LABEL_EMPTY
        ))
    }

    //#R13
    fun trackClickOnBackButtonRegisterEmail() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_WITH_EMAIL_PAGE,
                ACTION_CLICK_ON_BUTTON_BACK,
                LABEL_EMPTY
        ))
    }

    //#R14
    fun trackClickYesButtonPhoneDialog() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_YA_BENAR_PHONE,
                LABEL_CLICK
        ))
    }

    //#R15
    fun trackClickChangeButtonPhoneDialog() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_UBAH_BENAR_PHONE,
                LABEL_EMPTY
        ))
    }

    //#R16
    fun trackClickYesButtonRegisteredPhoneDialog() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_YA_MASUK_TERDAFTAR_PHONE,
                LABEL_CLICK
        ))
    }

    //#R16
    fun trackClickChangeButtonRegisteredPhoneDialog() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_UBAH_TERDAFTAR_PHONE,
                LABEL_EMPTY
        ))
    }

    //#R19
    fun trackClickSignUpButtonEmail() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_WITH_EMAIL_PAGE,
                ACTION_CLICK_ON_BUTTON_DAFTAR,
                LABEL_CLICK
        ))
    }

    //#R19
    fun trackSuccessClickSignUpButtonEmail() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_WITH_EMAIL_PAGE,
                ACTION_CLICK_ON_BUTTON_DAFTAR,
                LABEL_SUCCESS
        ))
    }

    //#R19
    fun trackFailedClickSignUpButtonEmail(failedMessage: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_WITH_EMAIL_PAGE,
                ACTION_CLICK_ON_BUTTON_DAFTAR,
                LABEL_FAILED + failedMessage
        ))
    }

    //#R20
    fun trackClickTermConditionButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_WITH_EMAIL_PAGE,
                ACTION_CLICK_SYARAT_DAN_KETENTUAN,
                LABEL_EMPTY
        ))
    }

    //#R21
    fun trackClickPrivacyPolicyButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_WITH_EMAIL_PAGE,
                ACTION_CLICK_KEBIJAKAN_PRIVASI,
                LABEL_EMPTY
        ))
    }

    //#R23
    fun trackClickOnBackButtonActivation() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_ACTIVATION,
                CATEGORY_ACTIVATION_PAGE,
                ACTION_CLICK_ON_BUTTON_BACK,
                LABEL_EMPTY
        ))
    }

    //#R24
    fun trackClickActivationButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_ACTIVATION,
                CATEGORY_ACTIVATION_PAGE,
                ACTION_CLICK_ON_BUTTON_AKTIVASI,
                LABEL_CLICK
        ))
    }

    //#R24
    fun trackSuccessClickActivationButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_ACTIVATION,
                CATEGORY_ACTIVATION_PAGE,
                ACTION_CLICK_ON_BUTTON_AKTIVASI,
                LABEL_SUCCESS
        ))
    }

    //#R25
    fun trackClickResendButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_ACTIVATION,
                CATEGORY_ACTIVATION_PAGE,
                ACTION_CLICK_KIRIM_ULANG,
                LABEL_CLICK
        ))
    }

    //#R25
    fun trackSuccessClickResendButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_ACTIVATION,
                CATEGORY_ACTIVATION_PAGE,
                ACTION_CLICK_KIRIM_ULANG,
                LABEL_SUCCESS
        ))
    }

    //#R25
    fun trackFailedClickResendButton(failedMessage: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_ACTIVATION,
                CATEGORY_ACTIVATION_PAGE,
                ACTION_CLICK_KIRIM_ULANG,
                LABEL_FAILED + failedMessage
        ))
    }

    //#R26
    fun trackClickOkResendButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_ACTIVATION,
                CATEGORY_ACTIVATION_PAGE,
                ACTION_CLICK_OK_KIRIM_ULANG,
                LABEL_CLICK
        ))
    }

    //#R26
    fun trackSuccessClickOkResendButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_ACTIVATION,
                CATEGORY_ACTIVATION_PAGE,
                ACTION_CLICK_OK_KIRIM_ULANG,
                LABEL_SUCCESS
        ))
    }

    //#R26
    fun trackFailedClickOkResendButton(failedMessage: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_ACTIVATION,
                CATEGORY_ACTIVATION_PAGE,
                ACTION_CLICK_OK_KIRIM_ULANG,
                LABEL_FAILED + failedMessage
        ))
    }

    //#R27
    fun trackClickChangeEmail() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_ACTIVATION,
                CATEGORY_ACTIVATION_PAGE,
                ACTION_CLICK_UBAH_EMAIL_ACTIVATION,
                LABEL_EMPTY
        ))
    }


    //#R24
    fun trackFailedClickActivationButton(failedMessage: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_ACTIVATION,
                CATEGORY_ACTIVATION_PAGE,
                ACTION_CLICK_ON_BUTTON_AKTIVASI,
                LABEL_FAILED + failedMessage
        ))
    }

    fun trackErrorRegister(errorMessage: String, loginMethod: String) {
        when (loginMethod) {
            UserSessionInterface.LOGIN_METHOD_EMAIL -> onErrorRegisterEmail()
            UserSessionInterface.LOGIN_METHOD_PHONE -> onErrorRegisterPhone()
            UserSessionInterface.LOGIN_METHOD_GOOGLE -> onErrorRegisterGoogle(errorMessage)
            UserSessionInterface.LOGIN_METHOD_FACEBOOK -> onErrorRegisterFacebook(errorMessage)
        }
    }

    private fun onErrorRegisterFacebook(errorMessage: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_ON_BUTTON_FACEBOOK,
                LABEL_REGISTER_FAILED + errorMessage
        ))
    }

    private fun onErrorRegisterGoogle(errorMessage: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_ON_BUTTON_GOOGLE,
                LABEL_REGISTER_FAILED + errorMessage
        ))
    }

    private fun onErrorRegisterPhone() {

    }

    private fun onErrorRegisterEmail() {

    }

    fun trackSuccessRegister(
            loginMethod: String,
            userId: String,
            name: String,
            email: String,
            phoneNumber: String,
            isGoldMerchant: Boolean,
            shopId: String,
            shopName:String
    ) {
        when (loginMethod) {
            UserSessionInterface.LOGIN_METHOD_EMAIL -> onSuccessRegisterEmail(userId.toString(), name, email)
            UserSessionInterface.LOGIN_METHOD_PHONE -> onSuccessRegisterPhone(userId.toString())
            UserSessionInterface.LOGIN_METHOD_GOOGLE -> onSuccessRegisterGoogle(userId.toString())
            UserSessionInterface.LOGIN_METHOD_FACEBOOK -> onSuccessRegisterFacebook(userId.toString())
        }
        sendSuccessRegisterToMoengage(
                userId,
                getLoginMethodMoengage(loginMethod),
                isGoldMerchant,
                shopId,
                shopName)
    }

    //#R7
    private fun onSuccessRegisterFacebook(userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_ON_BUTTON_FACEBOOK,
                LABEL_REGISTER_SUCCESS
        ))

        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_REGISTER_SUCCESS,
                CATEGORY_REGISTER,
                ACTION_REGISTER_SUCCESS,
                FACEBOOK
        ))
        sendBranchRegisterEvent(userId, MEDIUM_FACEBOOK)
    }

    //#R6
    private fun onSuccessRegisterGoogle(userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_ON_BUTTON_GOOGLE,
                LABEL_REGISTER_SUCCESS
        ))

        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_REGISTER_SUCCESS,
                CATEGORY_REGISTER,
                ACTION_REGISTER_SUCCESS,
                LoginRegisterAnalytics.GOOGLE
        ))
        sendBranchRegisterEvent(userId, MEDIUM_GOOGLE)
    }

    private fun onSuccessRegisterPhone(userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_ON_BUTTON_DAFTAR_PHONE_NUMBER,
                LABEL_SUCCESS
        ))
        sendBranchRegisterEvent(userId, MEDIUM_PHONE)
    }

    private fun onSuccessRegisterEmail(userId: String, name: String, email: String) {
        trackSuccessClickEmailSignUpButton()
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_REGISTER_SUCCESS,
                CATEGORY_REGISTER,
                ACTION_REGISTER_SUCCESS,
                LABEL_EMAIL
        ))

        TrackApp.getInstance().appsFlyer.sendAppsflyerRegisterEvent(userId, EMAIL_METHOD)
        sendBranchRegisterEvent(userId, MEDIUM_EMAIL)
    }

    private fun sendSuccessRegisterToMoengage(userId: String, loginMethod: String?, isGoldMerchant: Boolean, shopId: String,shopName:String){
        TrackApp.getInstance().moEngage.sendMoengageRegisterEvent("", userId,"", loginMethod?:"", "",  isGoldMerchant, shopId,shopName)
    }

    private fun sendBranchRegisterEvent(userId: String, medium: String) {
        val userData = UserData()
        userData.userId = userId
        userData.medium = medium
        LinkerManager.getInstance().sendEvent(LinkerUtils.createGenericRequest(LinkerConstants.EVENT_USER_REGISTRATION_VAL, userData))
    }

    fun trackClickTicker() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_ON_TICKER_LOGIN,
                ""
        ))
    }

    fun trackClickLinkTicker(link: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_TICKER_LINK,
                link
        ))
    }

    fun trackClickCloseTickerButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_ON_BUTTON_CLOSE_TICKER,
                ""
        ))
    }

    fun trackClickPhoneNumberSuggestion() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_PHONE_NUMBER_SUGGESTION,
                ""
        ))
    }

    fun trackClickSocmedButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_ON_BUTTON_SOCMED,
                ""
        ))
    }

    fun trackClickCloseSocmedButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_PAGE,
                ACTION_CLICK_ON_BUTTON_CLOSE_SOCMED,
                ""
        ))
    }

    fun eventViewBanner(label: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_VIEW_REGISTER_IRIS,
                CATEGORY_REGISTER_PAGE,
                ACTION_VIEW_BANNER,
                label
        ))
    }

    private fun getLoginMethodMoengage(loginMethod: String?): String? {
        return when (loginMethod) {
            UserSessionInterface.LOGIN_METHOD_EMAIL_SMART_LOCK -> EMAIL_METHOD
            UserSessionInterface.LOGIN_METHOD_EMAIL -> EMAIL_METHOD
            UserSessionInterface.LOGIN_METHOD_FACEBOOK -> FACEBOOK_METHOD
            UserSessionInterface.LOGIN_METHOD_GOOGLE -> GOOGLE_METHOD
            UserSessionInterface.LOGIN_METHOD_PHONE -> PHONE_NUMBER_METHOD
            else -> loginMethod
        }
    }

    companion object {

        val SCREEN_REGISTER_INITIAL = "Register page"

        private val EVENT_CLICK_LOGIN = "clickLogin"
        private val EVENT_CLICK_REGISTER = "clickRegister"
        private val EVENT_CLICK_ACTIVATION = "clickActivation"
        private val EVENT_VIEW_REGISTER_IRIS = "viewRegisterIris"
        private val EVENT_REGISTER_SUCCESS = "registerSuccess"

        private val CATEGORY_LOGIN_PAGE = "login page"
        private val CATEGORY_REGISTER_PAGE = "register page"
        private val CATEGORY_REGISTER_WITH_EMAIL_PAGE = "register with email page"
        private val CATEGORY_REGISTER_WITH_PHONE_NUMBER_OTP = "register with phone number otp"
        private val CATEGORY_REGISTER_WITH_PHONE_NUMBER_PAGE = "register with phone number page"
        private val CATEGORY_EMAIL_AKTIVASI_AKUN = "email aktivasi akun"
        private val CATEGORY_ACTIVATION_PAGE = "activation page"
        private val CATEGORY_REGISTER = "Register"

        private val ACTION_CLICK_DAFTAR_TOP = "click daftar top"
        private val ACTION_CLICK_DAFTAR_BOTTOM = "click daftar bottom"
        private val ACTION_CLICK_ON_BUTTON_BACK = "click on button back"
        private val ACTION_CLICK_MASUK_TOP = "click masuk top"
        private val ACTION_CLICK_MASUK_BOTTOM = "click masuk bottom"
        private val ACTION_CLICK_ON_BUTTON_DAFTAR = "click on button daftar"
        private val ACTION_CLICK_ON_BUTTON_DAFTAR_EMAIL = "click on button daftar - email"
        private val ACTION_CLICK_ON_BUTTON_DAFTAR_PHONE_NUMBER = "click on button daftar - phone number"
        private val ACTION_CLICK_ON_BUTTON_GOOGLE = "click on button google"
        private val ACTION_CLICK_ON_BUTTON_FACEBOOK = "click on button facebook"
        private val ACTION_CLICK_YA_MASUK_TERDAFTAR_EMAIL = "click ya, masuk terdaftar - email"
        private val ACTION_CLICK_UBAH_TERDAFTAR_EMAIL = "click ubah terdaftar - email"
        private val ACTION_CLICK_UBAH_EMAIL = "click ubah - email"
        private val ACTION_CLICK_YA_BENAR_EMAIL = "click ya, benar - email"
        private val ACTION_CLICK_YA_BENAR_PHONE = "click ya, benar - phone number"
        private val ACTION_CLICK_UBAH_BENAR_PHONE = "click ubah, benar - phone number"
        private val ACTION_CLICK_YA_MASUK_TERDAFTAR_PHONE = "click ya, masuk terdaftar - phone number"
        private val ACTION_CLICK_UBAH_TERDAFTAR_PHONE = "click ubah terdaftar - phone number"
        private val ACTION_CLICK_ON_BUTTON_BACK_PHONE = "click ubah terdaftar - phone number"
        private val ACTION_CLICK_SYARAT_DAN_KETENTUAN = "click syarat dan ketentuan"
        private val ACTION_CLICK_KEBIJAKAN_PRIVASI = "click kebijakan privasi"
        private val ACTION_CLICK_ON_AKTIFKAN_AKUN_ANDA = "click on aktifkan akun anda"
        private val ACTION_CLICK_ON_BUTTON_AKTIVASI = "click on button aktivasi"
        private val ACTION_CLICK_KIRIM_ULANG = "click kirim ulang"
        private val ACTION_CLICK_OK_KIRIM_ULANG = "click ok (kirim ulang email)"
        private val ACTION_CLICK_UBAH_EMAIL_ACTIVATION = "click ubah email"
        private val ACTION_CLICK_ON_BUTTON_VERIFIKASI = "click on button verifikasi"
        private val ACTION_CLICK_ON_BUTTON_SELESAI = "click on button selesai"
        private val ACTION_CLICK_ON_TICKER_LOGIN = "click on ticker login"
        private val ACTION_CLICK_TICKER_LINK = "click ticker link"
        private val ACTION_CLICK_ON_BUTTON_CLOSE_TICKER = "click on button close ticker"
        private val ACTION_CLICK_PHONE_NUMBER_SUGGESTION = "click phone number suggestion"
        private val ACTION_CLICK_ON_BUTTON_SOCMED = "click on button socmed"
        private val ACTION_CLICK_ON_BUTTON_CLOSE_SOCMED = "click on button close socmed"
        private val ACTION_VIEW_BANNER = "view banner"
        private val ACTION_REGISTER_SUCCESS = "Register Success"

        private val LABEL_EMPTY = ""
        private val LABEL_CLICK = "click"
        private val LABEL_SUCCESS = "success"
        private val LABEL_FAILED = "failed - "
        private val LABEL_FAILED_POPUP_PHONE = "failed - pop up phone number sudah terdaftar"
        private val LABEL_FAILED_POPUP_EMAIL = "failed - pop up email sudah terdaftar"
        private val LABEL_LOGIN_SUCCESS = "login success"
        private val LABEL_LOGIN_FAILED = "login failed - "
        private val LABEL_REGISTER_SUCCESS = "register success"
        private val LABEL_REGISTER_FAILED = "register failed - "
        val LABEL_EMAIL_EXIST = "email exist"
        val LABEL_PHONE_EXIST = "phone number exist"
        private val LABEL_BEBAS_ONGKIR = "bebas ongkir"
        private const val LABEL_EMAIL = "Email"

        val GOOGLE = "google"
        val FACEBOOK = "facebook"

        private const val EMAIL_METHOD = "Email"
        private const val FACEBOOK_METHOD = "Facebook"
        private const val GOOGLE_METHOD = "Google"
        private const val PHONE_NUMBER_METHOD = "Phone Number"
    }
}
