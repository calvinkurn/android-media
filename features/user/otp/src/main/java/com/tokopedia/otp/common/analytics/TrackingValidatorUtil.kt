package com.tokopedia.otp.common.analytics

import android.os.Build
import com.tokopedia.otp.common.analytics.TrackingValidatorConstant.Action.ACTION_CLICK_BACK_BUTTON
import com.tokopedia.otp.common.analytics.TrackingValidatorConstant.Action.ACTION_CLICK_KIRIM_ULANG
import com.tokopedia.otp.common.analytics.TrackingValidatorConstant.Action.ACTION_CLICK_ON_BUTTON_AKTIVASI
import com.tokopedia.otp.common.analytics.TrackingValidatorConstant.Action.ACTION_CLICK_ON_BUTTON_BACK
import com.tokopedia.otp.common.analytics.TrackingValidatorConstant.Action.ACTION_CLICK_ON_BUTTON_INACTIVE_PHONE_NUMBER
import com.tokopedia.otp.common.analytics.TrackingValidatorConstant.Action.ACTION_CLICK_ON_BUTTON_VERIFIKASI
import com.tokopedia.otp.common.analytics.TrackingValidatorConstant.Action.ACTION_CLICK_ON_GUNAKAN_METODE_LAIN
import com.tokopedia.otp.common.analytics.TrackingValidatorConstant.Action.ACTION_CLICK_ON_KIRIM_ULANG
import com.tokopedia.otp.common.analytics.TrackingValidatorConstant.Action.ACTION_CLICK_ON_OTP_METHOD
import com.tokopedia.otp.common.analytics.TrackingValidatorConstant.Action.ACTION_CLICK_ON_VERIFIKASI
import com.tokopedia.otp.common.analytics.TrackingValidatorConstant.Category.CATEGORY_ACTIVATION_PAGE
import com.tokopedia.otp.common.analytics.TrackingValidatorConstant.Category.CATEGORY_CHOOSE_OTP_PAGE
import com.tokopedia.otp.common.analytics.TrackingValidatorConstant.Category.CATEGORY_INPUT_OTP_PAGE
import com.tokopedia.otp.common.analytics.TrackingValidatorConstant.Category.CATEGORY_REGISTER_WITH_PHONE_NUMBER_OTP
import com.tokopedia.otp.common.analytics.TrackingValidatorConstant.Event.EVENT_CLICK_ACTIVATION
import com.tokopedia.otp.common.analytics.TrackingValidatorConstant.Event.EVENT_CLICK_BACK
import com.tokopedia.otp.common.analytics.TrackingValidatorConstant.Event.EVENT_CLICK_CONFIRM
import com.tokopedia.otp.common.analytics.TrackingValidatorConstant.Event.EVENT_CLICK_OTP
import com.tokopedia.otp.common.analytics.TrackingValidatorConstant.Event.EVENT_CLICK_REGISTER
import com.tokopedia.otp.common.analytics.TrackingValidatorConstant.Label.LABEL_CLICK
import com.tokopedia.otp.common.analytics.TrackingValidatorConstant.Label.LABEL_EMPTY
import com.tokopedia.otp.common.analytics.TrackingValidatorConstant.Label.LABEL_FAILED
import com.tokopedia.otp.common.analytics.TrackingValidatorConstant.Label.LABEL_SUCCESS
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ade Fulki on 2019-10-23.
 * ade.hadian@tokopedia.com
 */

class TrackingValidatorUtil @Inject constructor(){

    fun trackScreen(screenName: String) {
        Timber.w("""P2screenName = $screenName | ${Build.FINGERPRINT} | ${Build.MANUFACTURER} | ${Build.BRAND} | ${Build.DEVICE} | ${Build.PRODUCT} | ${Build.MODEL} | ${Build.TAGS}""")
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName)
    }

    fun trackClickMethodOtpButton(otpType: Int, modeName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_OTP,
                CATEGORY_CHOOSE_OTP_PAGE,
                ACTION_CLICK_ON_OTP_METHOD,
                String.format("%s - %s", otpType.toString(), modeName)))
    }

    fun trackClickInactivePhoneNumber(otpType: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_OTP,
                otpType,
                ACTION_CLICK_ON_BUTTON_INACTIVE_PHONE_NUMBER,
                LABEL_EMPTY
        ))
    }

    fun trackClickVerificationButton(otpType: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_CONFIRM,
                CATEGORY_INPUT_OTP_PAGE,
                ACTION_CLICK_ON_VERIFIKASI,
                otpType.toString()))
    }

    fun trackClickVerificationRegisterPhoneButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_WITH_PHONE_NUMBER_OTP,
                ACTION_CLICK_ON_BUTTON_VERIFIKASI,
                LABEL_CLICK
        ))
    }

    fun trackSuccessClickVerificationRegisterPhoneButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_WITH_PHONE_NUMBER_OTP,
                ACTION_CLICK_ON_BUTTON_VERIFIKASI,
                LABEL_SUCCESS
        ))
    }

    fun trackFailedClickVerificationRegisterPhoneButton(failedMessage: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_WITH_PHONE_NUMBER_OTP,
                ACTION_CLICK_ON_BUTTON_VERIFIKASI,
                LABEL_FAILED + failedMessage
        ))
    }

    fun trackClickVerificationRegisterEmailButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_ACTIVATION,
                CATEGORY_ACTIVATION_PAGE,
                ACTION_CLICK_ON_BUTTON_AKTIVASI,
                LABEL_CLICK
        ))
    }

    fun trackSuccessClickVerificationRegisterEmailButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_ACTIVATION,
                CATEGORY_ACTIVATION_PAGE,
                ACTION_CLICK_ON_BUTTON_AKTIVASI,
                LABEL_SUCCESS
        ))
    }

    fun trackFailedClickVerificationRegisterEmailButton(failedMessage: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_ACTIVATION,
                CATEGORY_ACTIVATION_PAGE,
                ACTION_CLICK_ON_BUTTON_AKTIVASI,
                LABEL_FAILED + failedMessage
        ))
    }

    fun trackClickUseOtherMethod(otpType: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_OTP,
                CATEGORY_INPUT_OTP_PAGE,
                ACTION_CLICK_ON_GUNAKAN_METODE_LAIN,
                otpType.toString()
        ))
    }

    fun trackClickResendOtpButton(otpType: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_OTP,
                CATEGORY_INPUT_OTP_PAGE,
                ACTION_CLICK_ON_KIRIM_ULANG,
                otpType.toString()))
    }

    fun trackClickResendRegisterPhoneOtpButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_WITH_PHONE_NUMBER_OTP,
                ACTION_CLICK_KIRIM_ULANG,
                LABEL_CLICK
        ))
    }

    fun trackSuccessClickResendRegisterPhoneOtpButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_WITH_PHONE_NUMBER_OTP,
                ACTION_CLICK_KIRIM_ULANG,
                LABEL_SUCCESS
        ))
    }

    fun trackFailedClickResendRegisterPhoneOtpButton(failedMessage: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_WITH_PHONE_NUMBER_OTP,
                ACTION_CLICK_KIRIM_ULANG,
                LABEL_FAILED + failedMessage
        ))
    }

    fun trackClickResendRegisterEmailOtpButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_ACTIVATION,
                CATEGORY_ACTIVATION_PAGE,
                ACTION_CLICK_KIRIM_ULANG,
                LABEL_CLICK
        ))
    }

    fun trackSuccessClickResendRegisterEmailOtpButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_ACTIVATION,
                CATEGORY_ACTIVATION_PAGE,
                ACTION_CLICK_KIRIM_ULANG,
                LABEL_SUCCESS
        ))
    }

    fun trackFailedClickResendRegisterEmailOtpButton(failedMessage: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_ACTIVATION,
                CATEGORY_ACTIVATION_PAGE,
                ACTION_CLICK_KIRIM_ULANG,
                LABEL_FAILED + failedMessage
        ))
    }

    fun trackClickBackOtpPage(otpType: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_BACK,
                CATEGORY_INPUT_OTP_PAGE,
                ACTION_CLICK_BACK_BUTTON,
                otpType.toString()))
    }

    fun trackClickBackRegisterPhoneOtp() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_REGISTER,
                CATEGORY_REGISTER_WITH_PHONE_NUMBER_OTP,
                ACTION_CLICK_ON_BUTTON_BACK,
                LABEL_EMPTY
        ))
    }
}