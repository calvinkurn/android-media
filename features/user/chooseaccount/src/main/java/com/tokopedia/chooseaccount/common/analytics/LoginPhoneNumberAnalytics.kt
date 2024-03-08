package com.tokopedia.chooseaccount.common.analytics

import android.app.Activity
import android.os.Build
import com.tokopedia.logger.ServerLogger.log
import com.tokopedia.logger.utils.Priority
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import javax.inject.Inject

/**
 * @author by nisie on 1/5/18.
 */
class LoginPhoneNumberAnalytics @Inject constructor() {
    object Screen {
        const val SCREEN_LOGIN_PHONE_NUMBER = "Login by Phone Number"
        const val SCREEN_CHOOSE_TOKOCASH_ACCOUNT = "choose account"
        const val SCREEN_NOT_CONNECTED_TO_TOKOCASH = "Login Tokocash - Not Connected"
        const val SCREEN_REGISTER_WITH_PHONE_NUMBER = "Register with Phone Number Page"
    }

    object Event {
        const val EVENT_CLICK_LOGIN = "clickLogin"
        const val CLICK_REGISTER = "clickRegister"
    }

    object Category {
        const val LOGIN_WITH_PHONE = "login with phone"
        const val PHONE_VERIFICATION = "phone verification"
        const val REGISTER_PAGE = "register page"
    }

    object Action {
        const val CLICK_ON_NEXT = "click on selanjutnya"
        const val CLICK_ON_VERIFICATION = "click on verifikasi"
        const val CLICK_ON_RESEND_VERIFICATION = "click on kirim ulang"
        const val LOGIN_SUCCESS = "login success"
        const val CLICK_ON_BUTTON_DAFTAR_PHONE = "click on button daftar - phone number"
        const val CHANGE_METHOD = "change method"
    }

    object Label {
        const val SMS = "sms"
        const val PHONE = "phone"
        const val TOKOCASH = "Tokocash"
        const val LOGIN_SUCCESS = "login success"
        const val REGISTER_SUCCESS = "success"
    }

    fun sendScreen(activity: Activity?, screenName: String) {
        val screenNameMessage =
            (screenName + " | " + Build.FINGERPRINT + " | " + Build.MANUFACTURER + " | "
                    + Build.BRAND + " | " + Build.DEVICE + " | " + Build.PRODUCT + " | " + Build.MODEL
                    + " | " + Build.TAGS)
        val screenNameMap: MutableMap<String, String> = HashMap()
        screenNameMap["screenName"] = screenNameMessage
        log(Priority.P2, "FINGERPRINT", screenNameMap)
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName)
    }

    fun trackLoginWithPhone() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            TrackAppUtils.gtmData(
                Event.EVENT_CLICK_LOGIN,
                Category.LOGIN_WITH_PHONE,
                Action.CLICK_ON_NEXT,
                ""
            )
        )
    }

    fun eventSuccessLoginPhoneNumber() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            TrackAppUtils.gtmData(
                Event.EVENT_CLICK_LOGIN,
                Category.LOGIN_WITH_PHONE,
                Action.LOGIN_SUCCESS,
                Label.TOKOCASH
            )
        )
    }

    fun eventSuccessLoginPhoneNumberSmartRegister() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            TrackAppUtils.gtmData(
                Event.CLICK_REGISTER,
                Category.REGISTER_PAGE,
                Action.CLICK_ON_BUTTON_DAFTAR_PHONE,
                Label.LOGIN_SUCCESS
            )
        )
    }

    fun trackVerifyOtpClick(mode: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            TrackAppUtils.gtmData(
                Event.EVENT_CLICK_LOGIN,
                Category.PHONE_VERIFICATION,
                Action.CLICK_ON_VERIFICATION,
                mode
            )
        )
    }

    fun trackResendVerification(mode: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            TrackAppUtils.gtmData(
                Event.EVENT_CLICK_LOGIN,
                Category.PHONE_VERIFICATION,
                Action.CLICK_ON_RESEND_VERIFICATION,
                mode
            )
        )
    }

    fun eventChooseVerificationMethodTracking(modeName: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            TrackAppUtils.gtmData(
                Event.EVENT_CLICK_LOGIN,
                Category.PHONE_VERIFICATION,
                Action.CHANGE_METHOD,
                modeName
            )
        )
    }

    fun eventClickBackRegisterVerificationPage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            TrackAppUtils.gtmData(
                Event.CLICK_REGISTER,
                "register with phone number otp",
                "click on button back",
                ""
            )
        )
    }
}