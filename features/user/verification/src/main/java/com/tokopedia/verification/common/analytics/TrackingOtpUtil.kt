package com.tokopedia.verification.common.analytics

import android.os.Build
import com.tokopedia.sessioncommon.util.LoginSdkUtils.getClientLabelIfAvailable
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.Analytics
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.verification.common.analytics.TrackingOtpConstant.Action
import com.tokopedia.verification.common.analytics.TrackingOtpConstant.BusinessUnit.USER_PLATFORM_UNIT
import com.tokopedia.verification.common.analytics.TrackingOtpConstant.Category
import com.tokopedia.verification.common.analytics.TrackingOtpConstant.CurrentSite.TOKOPEDIA_MARKETPLACE_SITE
import com.tokopedia.verification.common.analytics.TrackingOtpConstant.EVENT_BUSINESS_UNIT
import com.tokopedia.verification.common.analytics.TrackingOtpConstant.EVENT_CURRENT_SITE
import com.tokopedia.verification.common.analytics.TrackingOtpConstant.EVENT_USER_ID
import com.tokopedia.verification.common.analytics.TrackingOtpConstant.Event
import com.tokopedia.verification.common.analytics.TrackingOtpConstant.Label
import com.tokopedia.verification.otp.data.OtpData
import com.tokopedia.verification.otp.domain.pojo.ModeListData
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * Created by Ade Fulki on 2019-10-23.
 * ade.hadian@tokopedia.com
 */

class TrackingOtpUtil @Inject constructor(val userSession: UserSessionInterface) {

    var clientName: String = ""

    fun trackScreen(screenName: String) {
        Timber.w("""P2screenName = $screenName | ${Build.FINGERPRINT} | ${Build.MANUFACTURER} | ${Build.BRAND} | ${Build.DEVICE} | ${Build.PRODUCT} | ${Build.MODEL} | ${Build.TAGS}""")
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName)
    }

    fun trackClickMethodOtpButton(otpType: Int, modeName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_OTP_PAGE,
                Action.ACTION_CLICK_METHOD_OTP,
                String.format(Locale.getDefault(), "click - %s - %s", otpType.toString(), modeName) + getClientLabelIfAvailable(clientName)
        ))
    }

    fun trackErrorLimitOtpSilentVerif(otpType: Int, modeName: String, reason: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
            Event.EVENT_CLICK_OTP,
            Category.CATEGORY_OTP_PAGE,
            Action.ACTION_CLICK_METHOD_OTP,
            String.format(Locale.getDefault(), "fail - %s - %s - %s", reason, otpType.toString(), modeName)  + getClientLabelIfAvailable(clientName)))
    }

    fun trackClickInactivePhoneNumber(otpType: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                otpType,
                Action.ACTION_CLICK_ON_BUTTON_INACTIVE_PHONE_NUMBER,
                Label.LABEL_EMPTY + getClientLabelIfAvailable(clientName)
        ))
    }

    fun trackClickVerificationButton(otpType: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_CONFIRM,
                Category.CATEGORY_INPUT_OTP_PAGE,
                Action.ACTION_CLICK_ON_VERIFIKASI,
                otpType.toString()  + getClientLabelIfAvailable(clientName)))
    }

    fun trackClickVerificationRegisterPhoneButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_REGISTER,
                Category.CATEGORY_REGISTER_WITH_PHONE_NUMBER_OTP,
                Action.ACTION_CLICK_ON_BUTTON_VERIFIKASI,
                Label.LABEL_CLICK + getClientLabelIfAvailable(clientName)
        ))
    }

    fun trackSuccessClickVerificationRegisterPhoneButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_REGISTER,
                Category.CATEGORY_REGISTER_WITH_PHONE_NUMBER_OTP,
                Action.ACTION_CLICK_ON_BUTTON_VERIFIKASI,
                Label.LABEL_SUCCESS + getClientLabelIfAvailable(clientName)
        ))
    }

    fun trackFailedClickVerificationRegisterPhoneButton(failedMessage: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_REGISTER,
                Category.CATEGORY_REGISTER_WITH_PHONE_NUMBER_OTP,
                Action.ACTION_CLICK_ON_BUTTON_VERIFIKASI,
                Label.LABEL_FAILED + failedMessage + getClientLabelIfAvailable(clientName)
        ))
    }

    fun trackClickVerificationRegisterEmailButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_ACTIVATION,
                Category.CATEGORY_ACTIVATION_PAGE,
                Action.ACTION_CLICK_ON_BUTTON_AKTIVASI,
                Label.LABEL_CLICK + getClientLabelIfAvailable(clientName)
        ))
    }

    fun trackSuccessClickVerificationRegisterEmailButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_ACTIVATION,
                Category.CATEGORY_ACTIVATION_PAGE,
                Action.ACTION_CLICK_ON_BUTTON_AKTIVASI,
                Label.LABEL_SUCCESS + getClientLabelIfAvailable(clientName)
        ))
    }

    fun trackFailedClickVerificationRegisterEmailButton(failedMessage: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_ACTIVATION,
                Category.CATEGORY_ACTIVATION_PAGE,
                Action.ACTION_CLICK_ON_BUTTON_AKTIVASI,
                Label.LABEL_FAILED + failedMessage + getClientLabelIfAvailable(clientName)
        ))
    }

    fun trackClickUseOtherMethod(otpData: OtpData, modeListData: ModeListData) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_OTP_PAGE,
                Action.ACTION_CLICK_ON_GUNAKAN_METODE_LAIN,
                "${otpData.otpType} - ${modeListData.modeText}" + getClientLabelIfAvailable(clientName)
        ))
    }

    fun trackClickResendRegisterPhoneOtpButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_REGISTER,
                Category.CATEGORY_REGISTER_WITH_PHONE_NUMBER_OTP,
                Action.ACTION_CLICK_KIRIM_ULANG,
                Label.LABEL_CLICK + getClientLabelIfAvailable(clientName)
        ))
    }

    fun trackSuccessClickResendRegisterPhoneOtpButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_REGISTER,
                Category.CATEGORY_REGISTER_WITH_PHONE_NUMBER_OTP,
                Action.ACTION_CLICK_KIRIM_ULANG,
                Label.LABEL_SUCCESS + getClientLabelIfAvailable(clientName)
        ))
    }

    fun trackFailedClickResendRegisterPhoneOtpButton(failedMessage: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_REGISTER,
                Category.CATEGORY_REGISTER_WITH_PHONE_NUMBER_OTP,
                Action.ACTION_CLICK_KIRIM_ULANG,
                Label.LABEL_FAILED + failedMessage + getClientLabelIfAvailable(clientName)
        ))
    }

    fun trackClickResendRegisterEmailOtpButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_ACTIVATION,
                Category.CATEGORY_ACTIVATION_PAGE,
                Action.ACTION_CLICK_KIRIM_ULANG,
                Label.LABEL_CLICK + getClientLabelIfAvailable(clientName)
        ))
    }

    fun trackClickBackRegisterEmailOtp() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_ACTIVATION,
                Category.CATEGORY_ACTIVATION_PAGE,
                Action.ACTION_CLICK_ON_BUTTON_BACK,
                Label.LABEL_EMPTY + getClientLabelIfAvailable(clientName)
        ))
    }

    fun trackSuccessClickResendRegisterEmailOtpButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_ACTIVATION,
                Category.CATEGORY_ACTIVATION_PAGE,
                Action.ACTION_CLICK_KIRIM_ULANG,
                Label.LABEL_SUCCESS + getClientLabelIfAvailable(clientName)
        ))
    }

    fun trackFailedClickResendRegisterEmailOtpButton(failedMessage: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_ACTIVATION,
                Category.CATEGORY_ACTIVATION_PAGE,
                Action.ACTION_CLICK_KIRIM_ULANG,
                Label.LABEL_FAILED + failedMessage + getClientLabelIfAvailable(clientName)
        ))
    }

    fun trackClickBackOtpPage(otpData: OtpData, modeListData: ModeListData) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_OTP_PAGE,
                Action.ACTION_CLICK_BACK_BUTTON,
                "${otpData.otpType} - ${modeListData.modeText}" + getClientLabelIfAvailable(clientName)
        ))
    }

    fun trackClickBackRegisterPhoneOtp() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_REGISTER,
                Category.CATEGORY_REGISTER_WITH_PHONE_NUMBER_OTP,
                Action.ACTION_CLICK_ON_BUTTON_BACK,
                Label.LABEL_EMPTY + getClientLabelIfAvailable(clientName)
        ))
    }

    fun trackViewOtpPushNotifReceivePage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_VIEW_PUSH_NOTIF_IRIS,
                Category.CATEGORY_PUSH_NOTIF_RECEIVE_PAGE,
                Action.ACTION_VIEW_OTP_PUSH_NOTIF_RECEIVE_PAGE,
                Label.LABEL_EMPTY + getClientLabelIfAvailable(clientName)
        ))
    }

    fun trackClickBackReceiver() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_PUSH_NOTIF_RECEIVE_PAGE,
                Action.ACTION_CLICK_ON_BUTTON_BACK,
                Label.LABEL_EMPTY + getClientLabelIfAvailable(clientName)
        ))
    }

    fun trackClickRejectAccesssButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_PUSH_NOTIF_RECEIVE_PAGE,
                Action.ACTION_CLICK_ON_BUTTON_TOLAK_AKSES,
                Label.LABEL_EMPTY + getClientLabelIfAvailable(clientName)
        ))
    }

    fun trackClickAcceptAccesssButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_PUSH_NOTIF_RECEIVE_PAGE,
                Action.ACTION_CLICK_ON_BUTTON_TERIMA_AKSES,
                Label.LABEL_EMPTY + getClientLabelIfAvailable(clientName)
        ))
    }

    fun trackViewOtpPushNotifSuccessPage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_VIEW_PUSH_NOTIF_IRIS,
                Category.CATEGORY_PUSH_NOTIF_RECEIVE_SUCCESS_PAGE,
                Action.ACTION_VIEW_OTP_PUSH_NOTIF_RECEIVE_SUCCESS_PAGE,
                Label.LABEL_EMPTY + getClientLabelIfAvailable(clientName)
        ))
    }

    fun trackClickBackReceiveSuccess() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_PUSH_NOTIF_RECEIVE_SUCCESS_PAGE,
                Action.ACTION_CLICK_ON_BUTTON_CLOSE,
                Label.LABEL_EMPTY + getClientLabelIfAvailable(clientName)
        ))
    }

    fun trackClickCloseReceiveSuccess() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_PUSH_NOTIF_RECEIVE_SUCCESS_PAGE,
                Action.ACTION_CLICK_ON_BUTTON_TUTUP,
                Label.LABEL_EMPTY + getClientLabelIfAvailable(clientName)
        ))
    }

    fun trackViewOtpPushNotifFailedNoPinPage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_VIEW_PUSH_NOTIF_IRIS,
                Category.CATEGORY_PUSH_NOTIF_RECEIVE_FAILED_NO_PIN_PAGE,
                Action.ACTION_VIEW_OTP_PUSH_NOTIF_RECEIVE_FAILED_NO_PIN_PAGE,
                Label.LABEL_EMPTY + getClientLabelIfAvailable(clientName)
        ))
    }

    fun trackClickBackReceiveFailedNoPinButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_PUSH_NOTIF_RECEIVE_FAILED_NO_PIN_PAGE,
                Action.ACTION_CLICK_ON_BUTTON_CLOSE,
                Label.LABEL_EMPTY + getClientLabelIfAvailable(clientName)
        ))
    }

    fun trackClickCloseReceiveFailedNoPinButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_PUSH_NOTIF_RECEIVE_FAILED_NO_PIN_PAGE,
                Action.ACTION_CLICK_ON_BUTTON_TUTUP,
                Label.LABEL_EMPTY + getClientLabelIfAvailable(clientName)
        ))
    }

    fun trackViewOtpPushNotifFailedWithPinPage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_VIEW_PUSH_NOTIF_IRIS,
                Category.CATEGORY_PUSH_NOTIF_RECEIVE_FAILED_WITH_PIN_PAGE,
                Action.ACTION_VIEW_OTP_PUSH_NOTIF_RECEIVE_FAILED_WITH_PIN_PAGE,
                Label.LABEL_EMPTY + getClientLabelIfAvailable(clientName)
        ))
    }

    fun trackClickCloseReceiveFailedWithPinButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_PUSH_NOTIF_RECEIVE_FAILED_WITH_PIN_PAGE,
                Action.ACTION_CLICK_ON_BUTTON_CLOSE,
                Label.LABEL_EMPTY + getClientLabelIfAvailable(clientName)
        ))
    }

    fun trackClickChangePinReceiveFailedWithPinButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_PUSH_NOTIF_RECEIVE_FAILED_WITH_PIN_PAGE,
                Action.ACTION_CLICK_ON_BUTTON_UBAH_PIN,
                Label.LABEL_EMPTY + getClientLabelIfAvailable(clientName)
        ))
    }

    fun trackViewOtpPushNotifFailedWithPasswordPage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_VIEW_PUSH_NOTIF_IRIS,
                Category.CATEGORY_PUSH_NOTIF_RECEIVE_FAILED_WITH_PASSWORD_PAGE,
                Action.ACTION_VIEW_OTP_PUSH_NOTIF_RECEIVE_FAILED_WITH_PASSWORD_PAGE,
                Label.LABEL_EMPTY + getClientLabelIfAvailable(clientName)
        ))
    }

    fun trackClickCloseReceiveFailedWithPasswordButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_PUSH_NOTIF_RECEIVE_FAILED_WITH_PASSWORD_PAGE,
                Action.ACTION_CLICK_ON_BUTTON_CLOSE,
                Label.LABEL_EMPTY + getClientLabelIfAvailable(clientName)
        ))
    }

    fun trackClickChangePasswordReceiveFailedWithPasswordButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_PUSH_NOTIF_RECEIVE_FAILED_WITH_PASSWORD_PAGE,
                Action.ACTION_CLICK_ON_BUTTON_UBAH_KATA_SANDI,
                Label.LABEL_EMPTY + getClientLabelIfAvailable(clientName)
        ))
    }

    fun trackViewOtpPushNotifFailedFromOtherDevicePage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_VIEW_PUSH_NOTIF_IRIS,
                Category.CATEGORY_PUSH_NOTIF_RECEIVE_FAILED_FROM_OTHER_DEVICE_PAGE,
                Action.ACTION_VIEW_OTP_PUSH_NOTIF_RECEIVE_FAILED_FROM_OTHER_DEVICE_PAGE,
                Label.LABEL_EMPTY + getClientLabelIfAvailable(clientName)
        ))
    }

    fun trackClickCloseReceiveFailedFromOtherDeviceButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_PUSH_NOTIF_RECEIVE_FAILED_FROM_OTHER_DEVICE_PAGE,
                Action.ACTION_CLICK_ON_BUTTON_CLOSE,
                Label.LABEL_EMPTY + getClientLabelIfAvailable(clientName)
        ))
    }

    fun trackClickBottomCloseReceiveFailedFromOtherDeviceButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_PUSH_NOTIF_RECEIVE_FAILED_FROM_OTHER_DEVICE_PAGE,
                Action.ACTION_CLICK_ON_BUTTON_TUTUP,
                Label.LABEL_EMPTY + getClientLabelIfAvailable(clientName)
        ))
    }

    fun trackViewOtpPushNotifFailedOtpExpiredPage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_VIEW_PUSH_NOTIF_IRIS,
                Category.CATEGORY_PUSH_NOTIF_RECEIVE_FAILED_OTP_EXPIRED_PAGE,
                Action.ACTION_VIEW_OTP_PUSH_NOTIF_RECEIVE_FAILED_OTP_EXPIRED_PAGE,
                Label.LABEL_EMPTY + getClientLabelIfAvailable(clientName)
        ))
    }

    fun trackClickCloseReceiveFailedOtpExpiredButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_PUSH_NOTIF_RECEIVE_FAILED_OTP_EXPIRED_PAGE,
                Action.ACTION_CLICK_ON_BUTTON_CLOSE,
                Label.LABEL_EMPTY + getClientLabelIfAvailable(clientName)
        ))
    }

    fun trackClickBottomCloseReceiveFailedOtpExpiredButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_PUSH_NOTIF_RECEIVE_FAILED_OTP_EXPIRED_PAGE,
                Action.ACTION_CLICK_ON_BUTTON_TUTUP,
                Label.LABEL_EMPTY + getClientLabelIfAvailable(clientName)
        ))
    }

    fun trackViewOtpPushNotifSettingPage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_VIEW_PUSH_NOTIF_IRIS,
                Category.CATEGORY_PUSH_NOTIF_SETTING_PAGE,
                Action.ACTION_VIEW_OTP_PUSH_NOTIF_RECEIVE_SETTING_PAGE,
                Label.LABEL_EMPTY + getClientLabelIfAvailable(clientName)
        ))
    }

    fun trackClickPushNotifSettingButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_PUSH_NOTIF_SETTING_PAGE,
                Action.ACTION_CLICK_PUSH_NOTIF,
                Label.LABEL_EMPTY + getClientLabelIfAvailable(clientName)
        ))
    }

    fun trackClickChangePasswordSettingButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_PUSH_NOTIF_SETTING_PAGE,
                Action.ACTION_CLICK_ATUR_ULANG_KATA_SANDI,
                Label.LABEL_EMPTY + getClientLabelIfAvailable(clientName)
        ))
    }

    fun trackClickSignInFromNotifSettingButton(status: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_PUSH_NOTIF_SETTING_PAGE,
                Action.ACTION_CLICK_ON_BUTTON_AKTIFKAN_MASUK_LEWAT_NOTIF,
                status + getClientLabelIfAvailable(clientName)
        ))
    }

    fun trackClickInactivePhoneLink() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_CHOOSE_OTP_PAGE,
                Action.ACTION_CLICK_ON_INACTIVE_PHONE,
                Label.LABEL_EMPTY + getClientLabelIfAvailable(clientName)
        ))
    }

    fun trackViewApprovalPage() {
        val analytics: Analytics = TrackApp.getInstance().gtm
        val map = TrackAppUtils.gtmData(
                Event.EVENT_VIEW_LOGIN_IRIS,
                Category.CATEGORY_LOGIN_WITH_QR_CODE,
                Action.ACTION_SUCCESSFULLY_SCANNING_QR_CODE,
                Label.LABEL_EMPTY + getClientLabelIfAvailable(clientName)
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
                Label.LABEL_EMPTY + getClientLabelIfAvailable(clientName)
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
                Label.LABEL_EMPTY + getClientLabelIfAvailable(clientName)
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
                Label.LABEL_EMPTY + getClientLabelIfAvailable(clientName)
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
                Label.LABEL_EMPTY + getClientLabelIfAvailable(clientName)
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
                Label.LABEL_EMPTY + getClientLabelIfAvailable(clientName)
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
                Label.LABEL_EMPTY + getClientLabelIfAvailable(clientName)
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
                Label.LABEL_EMPTY + getClientLabelIfAvailable(clientName)
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
                Label.LABEL_EMPTY + getClientLabelIfAvailable(clientName)
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
                Label.LABEL_EMPTY + getClientLabelIfAvailable(clientName)
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
                otpType + getClientLabelIfAvailable(clientName)
        ))
    }

    fun trackAutoSubmitVerificationGoogleAuth(otpType: String, isSuccess: Boolean, message: String = "") {
        var label = "$otpType - " + if (isSuccess) "success" else "fail - $message"
        label += getClientLabelIfAvailable(clientName)
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_VIEW_OTP,
                Category.CATEGORY_OTP_PAGE,
                Action.ACTION_VIEW_CHOOSE_OTP_PAGE,
                label
        ))
    }

    /* Generate Otp */
    fun trackGenerateOtp(otpData: OtpData, modeListData: ModeListData, isSuccess: Boolean, message: String = "") {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_OTP_PAGE,
                Action.ACTION_CLICK_METHOD_OTP,
                if (isSuccess) {
                    getLabelWithOtpMethod(TrackerLabelType.SUCCESS, otpData, modeListData)  + getClientLabelIfAvailable(clientName)
                } else {
                    getLabelWithOtpMethod(TrackerLabelType.FAIL, otpData, modeListData, message) + getClientLabelIfAvailable(clientName)
                }
        ))
    }

    fun trackResendOtp(otpData: OtpData, modeListData: ModeListData, isSuccess: Boolean, message: String = "") {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_OTP_PAGE,
                Action.ACTION_CLICK_RESEND_OTP,
                if (isSuccess) {
                    getLabelWithOtpMethod(TrackerLabelType.SUCCESS, otpData, modeListData) + getClientLabelIfAvailable(clientName)
                } else {
                    getLabelWithOtpMethod(TrackerLabelType.FAIL, otpData, modeListData, message) + getClientLabelIfAvailable(clientName)
                }
        ))
    }

    fun trackClickResendOtp(otpData: OtpData, modeListData: ModeListData) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_OTP_PAGE,
                Action.ACTION_CLICK_RESEND_OTP,
                getLabelWithOtpMethod(TrackerLabelType.CLICK, otpData, modeListData) + getClientLabelIfAvailable(clientName)
        ))
    }

    /* Auto Submit Tracker */
    fun trackAutoSubmitVerification(otpData: OtpData, modeListData: ModeListData, isSuccess: Boolean, message: String = "") {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_OTP_PAGE,
                Action.ACTION_AUTO_SUBMIT_OTP,
                if (isSuccess) {
                    getLabelWithOtpMethod(TrackerLabelType.SUCCESS, otpData, modeListData) + getClientLabelIfAvailable(clientName)
                } else {
                    getLabelWithOtpMethod(TrackerLabelType.FAIL, otpData, modeListData, message) + getClientLabelIfAvailable(clientName)
                }
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
            label + getClientLabelIfAvailable(clientName)
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
                String.format(Locale.getDefault(), "success - %s - %s - %s", otpType.toString(), modeName, correlationId))

        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun trackSilentVerificationRequestFailed(reason: String, otpType: Int, modeName: String) {
        val map = TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_OTP_PAGE,
                Action.ACTION_CLICK_METHOD_OTP,
                String.format(Locale.getDefault(), "failed - %s - %s - %s", reason, otpType.toString(), modeName))
        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun trackSilentVerifTryAgainSuccess(otpType: Int, modeName: String, correlationId: String) {
        val map = TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_SILENT_VERIF_OTP_PAGE,
                Action.ACION_CLICK_TRY_AGAIN,
                String.format(Locale.getDefault(), "success - %s - %s - %s", otpType.toString(), modeName, correlationId))

        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE

        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun trackSilentVerifTryAgainClick(otpType: Int, modeName: String) {
        val map = TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_SILENT_VERIF_OTP_PAGE,
                Action.ACION_CLICK_TRY_AGAIN,
                String.format(Locale.getDefault(), "click - %s - %s", otpType.toString(), modeName))

        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE

        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun trackSilentVerifTryAgainFailed(reason: String, otpType: Int, modeName: String) {
        val map = TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_SILENT_VERIF_OTP_PAGE,
                Action.ACION_CLICK_TRY_AGAIN,
                String.format(Locale.getDefault(), "failed - %s - %s - %s", reason, otpType.toString(), modeName))

        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun trackChooseOtherMethod(otpType: Int, modeName: String) {
        val map = TrackAppUtils.gtmData(
                Event.EVENT_CLICK_OTP,
                Category.CATEGORY_OTP_PAGE,
                Action.ACION_CLICK_CHOOSE_OTHER_METHOD,
                String.format(Locale.getDefault(), "%s - %s", otpType.toString(), modeName) + getClientLabelIfAvailable(clientName)
        )

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
            Label.LABEL_MODE_LIST + getClientLabelIfAvailable(clientName)
        )
    }

    fun trackClickRequestChangePhoneNumberOnPin() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            Event.EVENT_CLICK_OTP,
            Category.CATEGORY_OTP_PAGE,
            Action.ACTION_CLICK_ON_REQUEST_CHANGE_PHONE_NUMBER,
            Label.LABEL_OTP_PAGE + getClientLabelIfAvailable(clientName)
        )
    }

    /**
     * tracker for default otp method, when clicking footer text 'Pakai Metode SMS'
     * on [com.tokopedia.verification.otp.view.fragment.VerificationMethodFragment]
     */
    fun trackClickUseWithOtpSms() {
        val map = TrackAppUtils.gtmData(
            Event.EVENT_CLICK_ACCOUNT,
            Category.CATEGORY_MAIN_OTP_PAGE,
            Action.ACTION_CLICK_USE_OTP_SMS,
            Label.LABEL_EMPTY + getClientLabelIfAvailable(clientName)
        )

        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    /**
     * tracker for default otp method, when clicking footer text 'Pakai Metode Lain'
     * on [com.tokopedia.verification.otp.view.fragment.VerificationMethodFragment]
     */
    fun trackClickUseWithOthersMethod() {
        val map = TrackAppUtils.gtmData(
            Event.EVENT_CLICK_ACCOUNT,
            Category.CATEGORY_MAIN_OTP_PAGE,
            Action.ACION_CLICK_CHOOSE_OTHER_METHOD,
            Label.LABEL_EMPTY + getClientLabelIfAvailable(clientName)
        )

        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    private fun getLabelWithOtpMethod(labelType: TrackerLabelType, otpData: OtpData, otpModeListData: ModeListData, message: String = ""): String {
        val label = if (message.isNotEmpty()) {
            "$labelType $message"
        } else {
            labelType.toString()
        }

        return "$label - ${otpData.otpType} - ${otpModeListData.modeText}"
    }

    private enum class TrackerLabelType {
        SUCCESS,
        FAIL,
        CLICK;

        override fun toString(): String {
            return when(this) {
                SUCCESS -> {
                    Label.LABEL_SUCCESS
                }
                FAIL -> {
                    Label.LABEL_FAILED
                }
                CLICK -> {
                    Label.LABEL_CLICK
                }
            }
        }
    }
}
