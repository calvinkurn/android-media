package com.tokopedia.otp.common.analytics

import android.os.Build
import com.tokopedia.otp.common.analytics.TrackingValidatorConstant.Action.ACTION_CLICK_KIRIM_ULANG
import com.tokopedia.otp.common.analytics.TrackingValidatorConstant.Action.ACTION_CLICK_OK_KIRIM_ULANG
import com.tokopedia.otp.common.analytics.TrackingValidatorConstant.Action.ACTION_CLICK_ON_BUTTON_AKTIVASI
import com.tokopedia.otp.common.analytics.TrackingValidatorConstant.Action.ACTION_CLICK_ON_BUTTON_INACTIVE_PHONE_NUMBER
import com.tokopedia.otp.common.analytics.TrackingValidatorConstant.Action.ACTION_CLICK_ON_OTP_METHOD
import com.tokopedia.otp.common.analytics.TrackingValidatorConstant.Action.ACTION_CLICK_UBAH_EMAIL_ACTIVATION
import com.tokopedia.otp.common.analytics.TrackingValidatorConstant.Category.CATEGORY_ACTIVATION_PAGE
import com.tokopedia.otp.common.analytics.TrackingValidatorConstant.Category.CATEGORY_CHOOSE_OTP_PAGE
import com.tokopedia.otp.common.analytics.TrackingValidatorConstant.Event.EVENT_CLICK_ACTIVATION
import com.tokopedia.otp.common.analytics.TrackingValidatorConstant.Event.EVENT_CLICK_OTP
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

    fun trackClickActivationButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_ACTIVATION,
                CATEGORY_ACTIVATION_PAGE,
                ACTION_CLICK_ON_BUTTON_AKTIVASI,
                LABEL_CLICK
        ))
    }

    fun trackClickResendButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_ACTIVATION,
                CATEGORY_ACTIVATION_PAGE,
                ACTION_CLICK_KIRIM_ULANG,
                LABEL_CLICK
        ))
    }

    fun trackSuccessClickOkResendButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_ACTIVATION,
                CATEGORY_ACTIVATION_PAGE,
                ACTION_CLICK_OK_KIRIM_ULANG,
                LABEL_SUCCESS
        ))
    }

    fun trackSuccessClickResendButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_ACTIVATION,
                CATEGORY_ACTIVATION_PAGE,
                ACTION_CLICK_KIRIM_ULANG,
                LABEL_SUCCESS
        ))
    }

    fun trackFailedClickOkResendButton(failedMessage: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_ACTIVATION,
                CATEGORY_ACTIVATION_PAGE,
                ACTION_CLICK_OK_KIRIM_ULANG,
                LABEL_FAILED + failedMessage
        ))
    }

    fun trackFailedClickResendButton(failedMessage: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_ACTIVATION,
                CATEGORY_ACTIVATION_PAGE,
                ACTION_CLICK_KIRIM_ULANG,
                LABEL_FAILED + failedMessage
        ))
    }

    fun trackSuccessClickActivationButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_ACTIVATION,
                CATEGORY_ACTIVATION_PAGE,
                ACTION_CLICK_ON_BUTTON_AKTIVASI,
                LABEL_SUCCESS
        ))
    }

    fun trackFailedClickActivationButton(failedMessage: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_ACTIVATION,
                CATEGORY_ACTIVATION_PAGE,
                ACTION_CLICK_ON_BUTTON_AKTIVASI,
                LABEL_FAILED + failedMessage
        ))
    }

    fun trackClickOkResendButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_ACTIVATION,
                CATEGORY_ACTIVATION_PAGE,
                ACTION_CLICK_OK_KIRIM_ULANG,
                LABEL_CLICK
        ))
    }

    fun trackClickChangeEmail() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_ACTIVATION,
                CATEGORY_ACTIVATION_PAGE,
                ACTION_CLICK_UBAH_EMAIL_ACTIVATION,
                LABEL_EMPTY
        ))
    }
}