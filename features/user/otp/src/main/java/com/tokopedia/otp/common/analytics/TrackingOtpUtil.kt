package com.tokopedia.otp.common.analytics

import android.os.Build
import com.tokopedia.otp.common.analytics.TrackingOtpConstant.Action
import com.tokopedia.otp.common.analytics.TrackingOtpConstant.BusinessUnit.USER_PLATFORM_UNIT
import com.tokopedia.otp.common.analytics.TrackingOtpConstant.Category
import com.tokopedia.otp.common.analytics.TrackingOtpConstant.CurrentSite.TOKOPEDIA_MARKETPLACE_SITE
import com.tokopedia.otp.common.analytics.TrackingOtpConstant.EVENT_BUSINESS_UNIT
import com.tokopedia.otp.common.analytics.TrackingOtpConstant.EVENT_CURRENT_SITE
import com.tokopedia.otp.common.analytics.TrackingOtpConstant.EVENT_USER_ID
import com.tokopedia.otp.common.analytics.TrackingOtpConstant.Event
import com.tokopedia.otp.common.analytics.TrackingOtpConstant.Label
import com.tokopedia.otp.verification.data.OtpData
import com.tokopedia.otp.verification.domain.data.OtpConstant
import com.tokopedia.otp.verification.domain.pojo.ModeListData
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.Analytics
import com.tokopedia.user.session.UserSessionInterface
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ade Fulki on 2019-10-23.
 * ade.hadian@tokopedia.com
 */

class TrackingOtpUtil @Inject constructor(val userSession: UserSessionInterface) {

    fun trackScreen(screenName: String) {
        Timber.w("""P2screenName = $screenName | ${Build.FINGERPRINT} | ${Build.MANUFACTURER} | ${Build.BRAND} | ${Build.DEVICE} | ${Build.PRODUCT} | ${Build.MODEL} | ${Build.TAGS}""")
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName)
    }

    fun trackClickMethodOtpButton(otpType: Int, modeName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_OTP_PAGE,
                Action.ACTION_CLICK_METHOD_OTP,
                String.format("click - %s - %s", otpType.toString(), modeName)))
    }

    fun trackErrorLimitOtpSilentVerif(otpType: Int, modeName: String, reason: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
            Event.EVENT_CLICK_OTP,
            Category.CATEGORY_OTP_PAGE,
            Action.ACTION_CLICK_METHOD_OTP,
            String.format("fail - %s - %s - %s", reason, otpType.toString(), modeName)))
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

    fun trackClickUseOtherMethod(otpData: OtpData, modeListData: ModeListData) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_OTP_PAGE,
                Action.ACTION_CLICK_ON_GUNAKAN_METODE_LAIN,
                "${otpData.otpType} - ${modeListData.modeText}"
        ))
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

    fun trackClickBackRegisterEmailOtp() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_ACTIVATION,
                Category.CATEGORY_ACTIVATION_PAGE,
                Action.ACTION_CLICK_ON_BUTTON_BACK,
                Label.LABEL_EMPTY
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

    fun trackClickBackOtpPage(otpData: OtpData, modeListData: ModeListData) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_OTP_PAGE,
                Action.ACTION_CLICK_BACK_BUTTON,
                "${otpData.otpType} - ${modeListData.modeText}"
        ))
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

    fun trackClickBackReceiveSuccess() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_PUSH_NOTIF_RECEIVE_SUCCESS_PAGE,
                Action.ACTION_CLICK_ON_BUTTON_CLOSE,
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

    fun trackClickChangePasswordSettingButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_PUSH_NOTIF_SETTING_PAGE,
                Action.ACTION_CLICK_ATUR_ULANG_KATA_SANDI,
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

    fun trackClickInactivePhoneLink() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_CHOOSE_OTP_PAGE,
                Action.ACTION_CLICK_ON_INACTIVE_PHONE,
                Label.LABEL_EMPTY
        ))
    }

    fun trackViewApprovalPage() {
        val analytics: Analytics = TrackApp.getInstance().gtm
        val map = TrackAppUtils.gtmData(
                Event.EVENT_VIEW_LOGIN_IRIS,
                Category.CATEGORY_LOGIN_WITH_QR_CODE,
                Action.ACTION_SUCCESSFULLY_SCANNING_QR_CODE,
                Label.LABEL_EMPTY
        )
        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE
        map[EVENT_USER_ID] = userSession.userId
        analytics.sendGeneralEvent(map)
    }

    fun trackClickApprovedApprovalPage() {
        val analytics: Analytics = TrackApp.getInstance().gtm
        val map = TrackAppUtils.gtmData(
                Event.EVENT_CLICK_LOGIN,
                Category.CATEGORY_LOGIN_WITH_QR_CODE,
                Action.ACTION_APPROVAL_CLICK_ON_BUTTON_APPROVED,
                Label.LABEL_EMPTY
        )
        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE
        map[EVENT_USER_ID] = userSession.userId
        analytics.sendGeneralEvent(map)
    }

    fun trackClickRejectedApprovalPage() {
        val analytics: Analytics = TrackApp.getInstance().gtm
        val map = TrackAppUtils.gtmData(
                Event.EVENT_CLICK_LOGIN,
                Category.CATEGORY_LOGIN_WITH_QR_CODE,
                Action.ACTION_APPROVAL_CLICK_ON_BUTTON_REJECTED,
                Label.LABEL_EMPTY
        )
        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE
        map[EVENT_USER_ID] = userSession.userId
        analytics.sendGeneralEvent(map)
    }

    fun trackClickBackApprovalPage() {
        val analytics: Analytics = TrackApp.getInstance().gtm
        val map = TrackAppUtils.gtmData(
                Event.EVENT_CLICK_LOGIN,
                Category.CATEGORY_LOGIN_WITH_QR_CODE,
                Action.ACTION_APPROVAL_CLICK_ON_BUTTON_BACK,
                Label.LABEL_EMPTY
        )
        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE
        map[EVENT_USER_ID] = userSession.userId
        analytics.sendGeneralEvent(map)
    }

    fun trackViewApprovalApprovedPage() {
        val analytics: Analytics = TrackApp.getInstance().gtm
        val map = TrackAppUtils.gtmData(
                Event.EVENT_VIEW_LOGIN_IRIS,
                Category.CATEGORY_LOGIN_WITH_QR_CODE,
                Action.ACTION_APPROVAL_APPROVED,
                Label.LABEL_EMPTY
        )
        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE
        map[EVENT_USER_ID] = userSession.userId
        analytics.sendGeneralEvent(map)
    }

    fun trackClickTutupApprovalApprovedPage() {
        val analytics: Analytics = TrackApp.getInstance().gtm
        val map = TrackAppUtils.gtmData(
                Event.EVENT_CLICK_LOGIN,
                Category.CATEGORY_LOGIN_WITH_QR_CODE,
                Action.ACTION_SUCCESS_CLICK_ON_BUTTON_TUTUP,
                Label.LABEL_EMPTY
        )
        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE
        map[EVENT_USER_ID] = userSession.userId
        analytics.sendGeneralEvent(map)
    }

    fun trackClickCloseApprovalApprovedPage() {
        val analytics: Analytics = TrackApp.getInstance().gtm
        val map = TrackAppUtils.gtmData(
                Event.EVENT_CLICK_LOGIN,
                Category.CATEGORY_LOGIN_WITH_QR_CODE,
                Action.ACTION_SUCCESS_CLICK_ON_BUTTON_CLOSE,
                Label.LABEL_EMPTY
        )
        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE
        map[EVENT_USER_ID] = userSession.userId
        analytics.sendGeneralEvent(map)
    }

    fun trackViewApprovalExpiredPage() {
        val analytics: Analytics = TrackApp.getInstance().gtm
        val map = TrackAppUtils.gtmData(
                Event.EVENT_VIEW_LOGIN_IRIS,
                Category.CATEGORY_LOGIN_WITH_QR_CODE,
                Action.ACTION_APPROVAL_EXPIRED,
                Label.LABEL_EMPTY
        )
        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE
        map[EVENT_USER_ID] = userSession.userId
        analytics.sendGeneralEvent(map)
    }

    fun trackClickScanApprovalExpiredPage() {
        val analytics: Analytics = TrackApp.getInstance().gtm
        val map = TrackAppUtils.gtmData(
                Event.EVENT_CLICK_LOGIN,
                Category.CATEGORY_LOGIN_WITH_QR_CODE,
                Action.ACTION_EXPIRED_CLICK_ON_BUTTON_SCAN_KEMBALI,
                Label.LABEL_EMPTY
        )
        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE
        map[EVENT_USER_ID] = userSession.userId
        analytics.sendGeneralEvent(map)
    }

    fun trackClickCloseApprovalExpiredPage() {
        val analytics: Analytics = TrackApp.getInstance().gtm
        val map = TrackAppUtils.gtmData(
                Event.EVENT_CLICK_LOGIN,
                Category.CATEGORY_LOGIN_WITH_QR_CODE,
                Action.ACTION_EXPIRED_CLICK_ON_BUTTON_CLOSE,
                Label.LABEL_EMPTY
        )
        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE
        map[EVENT_USER_ID] = userSession.userId
        analytics.sendGeneralEvent(map)
    }

    /* GOOGLE AUTH */
    fun trackViewVerificationGoogleAuth(otpType: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_VIEW_OTP,
                Category.CATEGORY_OTP_PAGE,
                Action.ACTION_VIEW_CHOOSE_OTP_PAGE,
                otpType
        ))
    }

    fun trackAutoSubmitVerificationGoogleAuth(otpType: String, isSuccess: Boolean, message: String = "") {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_VIEW_OTP,
                Category.CATEGORY_OTP_PAGE,
                Action.ACTION_VIEW_CHOOSE_OTP_PAGE,
                "$otpType - " + if (isSuccess) "success" else "fail - $message"
        ))
    }

    /* Generate Otp */
    fun trackGenerateOtp(otpData: OtpData, modeListData: ModeListData, isSuccess: Boolean, message: String = "") {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_OTP_PAGE,
                Action.ACTION_CLICK_METHOD_OTP,
                if (isSuccess) { "success" } else { "fail - $message" }
                        + " - ${otpData.otpType} - ${modeListData.modeText}"
                        + if (modeListData.modeText == OtpConstant.OtpMode.MISCALL) " - autoread" else ""
        ))
    }

    fun trackResendOtp(otpData: OtpData, modeListData: ModeListData, isSuccess: Boolean, message: String = "") {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_OTP_PAGE,
                Action.ACTION_CLICK_RESEND_OTP,
                if (isSuccess) { "success" } else { "fail - $message" }
                        + " - ${otpData.otpType} - ${modeListData.modeText}"
                        + if (modeListData.modeText == OtpConstant.OtpMode.MISCALL) " - autoread" else ""
        ))
    }

    fun trackClickResendOtp(otpData: OtpData, modeListData: ModeListData) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_OTP_PAGE,
                Action.ACTION_CLICK_RESEND_OTP,
                "click - ${otpData.otpType} - ${modeListData.modeText}"
        ))
    }

    /* Auto Submit Tracker */
    fun trackAutoSubmitVerification(otpData: OtpData, modeListData: ModeListData, isSuccess: Boolean, message: String = "") {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_OTP_PAGE,
                Action.ACTION_AUTO_SUBMIT_OTP,
                if (isSuccess) { "success" } else { "fail - $message" }
                        + " - ${otpData.otpType} - ${modeListData.modeText}"
                        + if (modeListData.modeText == OtpConstant.OtpMode.MISCALL) " - autoread" else ""
        ))
    }

    /* Auto Submit Silent Verif */
    fun trackAutoSubmitSilentVerificationEvUrl(otpData: OtpData, modeListData: ModeListData, isSuccess: Boolean, correlationId: String, message: String = "") {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
            Event.EVENT_CLICK_OTP,
            Category.CATEGORY_OTP_PAGE,
            Action.ACTION_AUTO_SUBMIT_OTP,
            if (isSuccess) { "success" } else { "fail - $message" }
                    + " - ${otpData.otpType} - ${modeListData.modeText}"
                    + " - evURL - $correlationId"
        ))
    }

    /* Auto Submit Silent Verif - Otp Validate */
    fun trackAutoSubmitSilentVerificationOtpValidate(otpData: OtpData, modeListData: ModeListData, isSuccess: Boolean, correlationId: String, message: String = "") {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
            Event.EVENT_CLICK_OTP,
            Category.CATEGORY_OTP_PAGE,
            Action.ACTION_AUTO_SUBMIT_OTP,
            if (isSuccess) { "success" } else { "fail - $message" }
                    + " - ${otpData.otpType} - ${modeListData.modeText}"
                    + " - $correlationId"
        ))
    }

    fun trackCellularDialogButton(label: String) {
        val analytics: Analytics = TrackApp.getInstance().gtm
        val map = TrackAppUtils.gtmData(
            Event.EVENT_CLICK_OTP,
            Category.CATEGORY_SILENT_VERIF_REMINDER,
            Action.ACION_CLICK_SILENT_VERIF,
            label
        )
        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE
        analytics.sendGeneralEvent(map)
    }

    fun trackSilentVerificationResult(label: String) {
        val analytics: Analytics = TrackApp.getInstance().gtm
        val map = TrackAppUtils.gtmData(
            Event.EVENT_CLICK_OTP,
            Category.CATEGORY_SILENT_VERIF_OTP_PAGE,
            Action.ACION_CLICK_SILENT_VERIF,
            label
        )
        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE
        analytics.sendGeneralEvent(map)
    }

    fun trackSilentVerificationRequestSuccess(otpType: Int, modeName: String, correlationId: String) {
        val map = TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_OTP_PAGE,
                Action.ACTION_CLICK_METHOD_OTP,
                String.format("success - %s - %s -%s", otpType.toString(), modeName, correlationId))

        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun trackSilentVerificationRequestFailed(reason: String, otpType: Int, modeName: String) {
        val map = TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_OTP_PAGE,
                Action.ACTION_CLICK_METHOD_OTP,
                String.format("failed - %s - %s - %s", reason, otpType.toString(), modeName))
        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun trackSilentVerifTryAgainSuccess(otpType: Int, modeName: String, correlationId: String) {
        val map = TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_SILENT_VERIF_OTP_PAGE,
                Action.ACION_CLICK_TRY_AGAIN,
                String.format("success - %s - %s -%s", otpType.toString(), modeName, correlationId))

        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE

        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun trackSilentVerifTryAgainClick(otpType: Int, modeName: String) {
        val map = TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_SILENT_VERIF_OTP_PAGE,
                Action.ACION_CLICK_TRY_AGAIN,
                String.format("click - %s - %s", otpType.toString(), modeName))

        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE

        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun trackSilentVerifTryAgainFailed(reason: String, otpType: Int, modeName: String) {
        val map = TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_SILENT_VERIF_OTP_PAGE,
                Action.ACION_CLICK_TRY_AGAIN,
                String.format("failed - %s - %s - %s", reason, otpType.toString(), modeName))

        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun trackChooseOtherMethod(otpType: Int, modeName: String) {
        val map = TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_OTP_PAGE,
                Action.ACION_CLICK_CHOOSE_OTHER_METHOD,
                String.format("%s - %s", otpType.toString(), modeName))

        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE

        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    /**
     * For Inactive Phone
     **/
    fun trackClickRequestChangePhoneNumberOnModeList() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            Event.EVENT_CLICK_OTP,
            Category.CATEGORY_OTP_PAGE,
            Action.ACTION_CLICK_ON_REQUEST_CHANGE_PHONE_NUMBER,
            Label.LABEL_MODE_LIST
        )
    }

    fun trackClickRequestChangePhoneNumberOnPin() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            Event.EVENT_CLICK_OTP,
            Category.CATEGORY_OTP_PAGE,
            Action.ACTION_CLICK_ON_REQUEST_CHANGE_PHONE_NUMBER,
            Label.LABEL_OTP_PAGE
        )
    }
}