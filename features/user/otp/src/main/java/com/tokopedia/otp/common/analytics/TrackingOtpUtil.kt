package com.tokopedia.otp.common.analytics

import android.os.Build
import com.tokopedia.otp.common.analytics.TrackingOtpConstant.Action
import com.tokopedia.otp.common.analytics.TrackingOtpConstant.Category
import com.tokopedia.otp.common.analytics.TrackingOtpConstant.Event
import com.tokopedia.otp.common.analytics.TrackingOtpConstant.Label
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ade Fulki on 2019-10-23.
 * ade.hadian@tokopedia.com
 */

class TrackingOtpUtil @Inject constructor() {

    fun trackScreen(screenName: String) {
        Timber.w("""P2screenName = $screenName | ${Build.FINGERPRINT} | ${Build.MANUFACTURER} | ${Build.BRAND} | ${Build.DEVICE} | ${Build.PRODUCT} | ${Build.MODEL} | ${Build.TAGS}""")
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName)
    }

    fun trackClickMethodOtpButton(otpType: Int, modeName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_CHOOSE_OTP_PAGE,
                Action.ACTION_CLICK_ON_OTP_METHOD,
                String.format("%s - %s", otpType.toString(), modeName)))
    }

    fun trackClickInactivePhoneNumber(otpType: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                otpType,
                Action.ACTION_CLICK_ON_BUTTON_INACTIVE_PHONE_NUMBER,
                Label.LABEL_EMPTY
        ))
    }

    fun trackClickVerificationButton(otpType: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_CONFIRM,
                Category.CATEGORY_INPUT_OTP_PAGE,
                Action.ACTION_CLICK_ON_VERIFIKASI,
                otpType.toString()))
    }

    fun trackClickVerificationRegisterPhoneButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_REGISTER,
                Category.CATEGORY_REGISTER_WITH_PHONE_NUMBER_OTP,
                Action.ACTION_CLICK_ON_BUTTON_VERIFIKASI,
                Label.LABEL_CLICK
        ))
    }

    fun trackSuccessClickVerificationRegisterPhoneButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_REGISTER,
                Category.CATEGORY_REGISTER_WITH_PHONE_NUMBER_OTP,
                Action.ACTION_CLICK_ON_BUTTON_VERIFIKASI,
                Label.LABEL_SUCCESS
        ))
    }

    fun trackFailedClickVerificationRegisterPhoneButton(failedMessage: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_REGISTER,
                Category.CATEGORY_REGISTER_WITH_PHONE_NUMBER_OTP,
                Action.ACTION_CLICK_ON_BUTTON_VERIFIKASI,
                Label.LABEL_FAILED + failedMessage
        ))
    }

    fun trackClickVerificationRegisterEmailButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_ACTIVATION,
                Category.CATEGORY_ACTIVATION_PAGE,
                Action.ACTION_CLICK_ON_BUTTON_AKTIVASI,
                Label.LABEL_CLICK
        ))
    }

    fun trackSuccessClickVerificationRegisterEmailButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_ACTIVATION,
                Category.CATEGORY_ACTIVATION_PAGE,
                Action.ACTION_CLICK_ON_BUTTON_AKTIVASI,
                Label.LABEL_SUCCESS
        ))
    }

    fun trackFailedClickVerificationRegisterEmailButton(failedMessage: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_ACTIVATION,
                Category.CATEGORY_ACTIVATION_PAGE,
                Action.ACTION_CLICK_ON_BUTTON_AKTIVASI,
                Label.LABEL_FAILED + failedMessage
        ))
    }

    fun trackClickUseOtherMethod(otpType: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_INPUT_OTP_PAGE,
                Action.ACTION_CLICK_ON_GUNAKAN_METODE_LAIN,
                otpType.toString()
        ))
    }

    fun trackClickResendOtpButton(otpType: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_INPUT_OTP_PAGE,
                Action.ACTION_CLICK_ON_KIRIM_ULANG,
                otpType.toString()))
    }

    fun trackClickResendRegisterPhoneOtpButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_REGISTER,
                Category.CATEGORY_REGISTER_WITH_PHONE_NUMBER_OTP,
                Action.ACTION_CLICK_KIRIM_ULANG,
                Label.LABEL_CLICK
        ))
    }

    fun trackSuccessClickResendRegisterPhoneOtpButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_REGISTER,
                Category.CATEGORY_REGISTER_WITH_PHONE_NUMBER_OTP,
                Action.ACTION_CLICK_KIRIM_ULANG,
                Label.LABEL_SUCCESS
        ))
    }

    fun trackFailedClickResendRegisterPhoneOtpButton(failedMessage: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_REGISTER,
                Category.CATEGORY_REGISTER_WITH_PHONE_NUMBER_OTP,
                Action.ACTION_CLICK_KIRIM_ULANG,
                Label.LABEL_FAILED + failedMessage
        ))
    }

    fun trackClickResendRegisterEmailOtpButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_ACTIVATION,
                Category.CATEGORY_ACTIVATION_PAGE,
                Action.ACTION_CLICK_KIRIM_ULANG,
                Label.LABEL_CLICK
        ))
    }

    fun trackSuccessClickResendRegisterEmailOtpButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_ACTIVATION,
                Category.CATEGORY_ACTIVATION_PAGE,
                Action.ACTION_CLICK_KIRIM_ULANG,
                Label.LABEL_SUCCESS
        ))
    }

    fun trackFailedClickResendRegisterEmailOtpButton(failedMessage: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_ACTIVATION,
                Category.CATEGORY_ACTIVATION_PAGE,
                Action.ACTION_CLICK_KIRIM_ULANG,
                Label.LABEL_FAILED + failedMessage
        ))
    }

    fun trackClickBackOtpPage(otpType: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_BACK,
                Category.CATEGORY_INPUT_OTP_PAGE,
                Action.ACTION_CLICK_BACK_BUTTON,
                otpType.toString()))
    }

    fun trackClickBackRegisterPhoneOtp() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_REGISTER,
                Category.CATEGORY_REGISTER_WITH_PHONE_NUMBER_OTP,
                Action.ACTION_CLICK_ON_BUTTON_BACK,
                Label.LABEL_EMPTY
        ))
    }

    fun trackViewOtpPushNotifReceivePage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_VIEW_PUSH_NOTIF_IRIS,
                Category.CATEGORY_PUSH_NOTIF_RECEIVE_PAGE,
                Action.ACTION_VIEW_OTP_PUSH_NOTIF_RECEIVE_PAGE,
                Label.LABEL_EMPTY
        ))
    }

    fun trackClickBackReceiver() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_PUSH_NOTIF_RECEIVE_PAGE,
                Action.ACTION_CLICK_ON_BUTTON_BACK,
                Label.LABEL_EMPTY
        ))
    }

    fun trackClickRejectAccesssButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_PUSH_NOTIF_RECEIVE_PAGE,
                Action.ACTION_CLICK_ON_BUTTON_TOLAK_AKSES,
                Label.LABEL_EMPTY
        ))
    }

    fun trackClickAcceptAccesssButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_PUSH_NOTIF_RECEIVE_PAGE,
                Action.ACTION_CLICK_ON_BUTTON_TERIMA_AKSES,
                Label.LABEL_EMPTY
        ))
    }

    fun trackViewOtpPushNotifSuccessPage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_VIEW_PUSH_NOTIF_IRIS,
                Category.CATEGORY_PUSH_NOTIF_RECEIVE_SUCCESS_PAGE,
                Action.ACTION_VIEW_OTP_PUSH_NOTIF_RECEIVE_SUCCESS_PAGE,
                Label.LABEL_EMPTY
        ))
    }

    fun trackClickCloseReceiveSuccess() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_PUSH_NOTIF_RECEIVE_SUCCESS_PAGE,
                Action.ACTION_CLICK_ON_BUTTON_TUTUP,
                Label.LABEL_EMPTY
        ))
    }

    fun trackViewOtpPushNotifFailedNoPinPage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_VIEW_PUSH_NOTIF_IRIS,
                Category.CATEGORY_PUSH_NOTIF_RECEIVE_FAILED_NO_PIN_PAGE,
                Action.ACTION_VIEW_OTP_PUSH_NOTIF_RECEIVE_FAILED_NO_PIN_PAGE,
                Label.LABEL_EMPTY
        ))
    }

    fun trackClickBackReceiveFailedNoPinButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_PUSH_NOTIF_RECEIVE_FAILED_NO_PIN_PAGE,
                Action.ACTION_CLICK_ON_BUTTON_CLOSE,
                Label.LABEL_EMPTY
        ))
    }

    fun trackClickCloseReceiveFailedNoPinButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_PUSH_NOTIF_RECEIVE_FAILED_NO_PIN_PAGE,
                Action.ACTION_CLICK_ON_BUTTON_TUTUP,
                Label.LABEL_EMPTY
        ))
    }

    fun trackViewOtpPushNotifFailedWithPinPage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_VIEW_PUSH_NOTIF_IRIS,
                Category.CATEGORY_PUSH_NOTIF_RECEIVE_FAILED_WITH_PIN_PAGE,
                Action.ACTION_VIEW_OTP_PUSH_NOTIF_RECEIVE_FAILED_WITH_PIN_PAGE,
                Label.LABEL_EMPTY
        ))
    }

    fun trackClickCloseReceiveFailedWithPinButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_PUSH_NOTIF_RECEIVE_FAILED_WITH_PIN_PAGE,
                Action.ACTION_CLICK_ON_BUTTON_CLOSE,
                Label.LABEL_EMPTY
        ))
    }

    fun trackClickChangePinReceiveFailedWithPinButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_PUSH_NOTIF_RECEIVE_FAILED_WITH_PIN_PAGE,
                Action.ACTION_CLICK_ON_BUTTON_UBAH_PIN,
                Label.LABEL_EMPTY
        ))
    }

    fun trackViewOtpPushNotifFailedWithPasswordPage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_VIEW_PUSH_NOTIF_IRIS,
                Category.CATEGORY_PUSH_NOTIF_RECEIVE_FAILED_WITH_PASSWORD_PAGE,
                Action.ACTION_VIEW_OTP_PUSH_NOTIF_RECEIVE_FAILED_WITH_PASSWORD_PAGE,
                Label.LABEL_EMPTY
        ))
    }

    fun trackClickCloseReceiveFailedWithPasswordButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_PUSH_NOTIF_RECEIVE_FAILED_WITH_PASSWORD_PAGE,
                Action.ACTION_CLICK_ON_BUTTON_CLOSE,
                Label.LABEL_EMPTY
        ))
    }

    fun trackClickChangePasswordReceiveFailedWithPasswordButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_PUSH_NOTIF_RECEIVE_FAILED_WITH_PASSWORD_PAGE,
                Action.ACTION_CLICK_ON_BUTTON_UBAH_KATA_SANDI,
                Label.LABEL_EMPTY
        ))
    }

    fun trackViewOtpPushNotifFailedFromOtherDevicePage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_VIEW_PUSH_NOTIF_IRIS,
                Category.CATEGORY_PUSH_NOTIF_RECEIVE_FAILED_FROM_OTHER_DEVICE_PAGE,
                Action.ACTION_VIEW_OTP_PUSH_NOTIF_RECEIVE_FAILED_FROM_OTHER_DEVICE_PAGE,
                Label.LABEL_EMPTY
        ))
    }

    fun trackClickCloseReceiveFailedFromOtherDeviceButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_PUSH_NOTIF_RECEIVE_FAILED_FROM_OTHER_DEVICE_PAGE,
                Action.ACTION_CLICK_ON_BUTTON_CLOSE,
                Label.LABEL_EMPTY
        ))
    }

    fun trackClickBottomCloseReceiveFailedFromOtherDeviceButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_PUSH_NOTIF_RECEIVE_FAILED_FROM_OTHER_DEVICE_PAGE,
                Action.ACTION_CLICK_ON_BUTTON_TUTUP,
                Label.LABEL_EMPTY
        ))
    }

    fun trackViewOtpPushNotifFailedOtpExpiredPage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_VIEW_PUSH_NOTIF_IRIS,
                Category.CATEGORY_PUSH_NOTIF_RECEIVE_FAILED_OTP_EXPIRED_PAGE,
                Action.ACTION_VIEW_OTP_PUSH_NOTIF_RECEIVE_FAILED_OTP_EXPIRED_PAGE,
                Label.LABEL_EMPTY
        ))
    }

    fun trackClickCloseReceiveFailedOtpExpiredButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_PUSH_NOTIF_RECEIVE_FAILED_OTP_EXPIRED_PAGE,
                Action.ACTION_CLICK_ON_BUTTON_CLOSE,
                Label.LABEL_EMPTY
        ))
    }

    fun trackClickBottomCloseReceiveFailedOtpExpiredButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_PUSH_NOTIF_RECEIVE_FAILED_OTP_EXPIRED_PAGE,
                Action.ACTION_CLICK_ON_BUTTON_TUTUP,
                Label.LABEL_EMPTY
        ))
    }

    fun trackViewOtpPushNotifSettingPage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_VIEW_PUSH_NOTIF_IRIS,
                Category.CATEGORY_PUSH_NOTIF_SETTING_PAGE,
                Action.ACTION_VIEW_OTP_PUSH_NOTIF_RECEIVE_SETTING_PAGE,
                Label.LABEL_EMPTY
        ))
    }

    fun trackClickPushNotifSettingButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_PUSH_NOTIF_SETTING_PAGE,
                Action.ACTION_CLICK_PUSH_NOTIF,
                Label.LABEL_EMPTY
        ))
    }

    fun trackClickSignInFromNotifSettingButton(status: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_PUSH_NOTIF_SETTING_PAGE,
                Action.ACTION_CLICK_ON_BUTTON_AKTIFKAN_MASUK_LEWAT_NOTIF,
                status
        ))
    }
}