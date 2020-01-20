package com.tokopedia.otp.common.analytics

import com.tokopedia.otp.common.analytics.TrackingValidatorConstant.Action.ACTION_CLICK_KIRIM_ULANG
import com.tokopedia.otp.common.analytics.TrackingValidatorConstant.Action.ACTION_CLICK_OK_KIRIM_ULANG
import com.tokopedia.otp.common.analytics.TrackingValidatorConstant.Action.ACTION_CLICK_ON_BUTTON_AKTIVASI
import com.tokopedia.otp.common.analytics.TrackingValidatorConstant.Action.ACTION_CLICK_UBAH_EMAIL_ACTIVATION
import com.tokopedia.otp.common.analytics.TrackingValidatorConstant.Category.CATEGORY_ACTIVATION_PAGE
import com.tokopedia.otp.common.analytics.TrackingValidatorConstant.Event.EVENT_CLICK_ACTIVATION
import com.tokopedia.otp.common.analytics.TrackingValidatorConstant.Label.LABEL_CLICK
import com.tokopedia.otp.common.analytics.TrackingValidatorConstant.Label.LABEL_EMPTY
import com.tokopedia.otp.common.analytics.TrackingValidatorConstant.Label.LABEL_FAILED
import com.tokopedia.otp.common.analytics.TrackingValidatorConstant.Label.LABEL_SUCCESS
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

/**
 * Created by Ade Fulki on 2019-10-23.
 * ade.hadian@tokopedia.com
 */

class TrackingValidatorUtil{

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