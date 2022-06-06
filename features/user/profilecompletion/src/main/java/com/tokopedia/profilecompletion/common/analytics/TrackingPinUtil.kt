package com.tokopedia.profilecompletion.common.analytics

import com.tokopedia.profilecompletion.common.analytics.TrackingPinConstant.Action.ACTION_CLICK_ON_BUTTON_BACK_PIN_TOKOPEDIA
import com.tokopedia.profilecompletion.common.analytics.TrackingPinConstant.Action.ACTION_CLICK_ON_BUTTON_CREATE_PIN_TOKOPEDIA
import com.tokopedia.profilecompletion.common.analytics.TrackingPinConstant.Action.ACTION_CLICK_ON_BUTTON_NANTI_SAJA_PIN_TOKOPEDIA
import com.tokopedia.profilecompletion.common.analytics.TrackingPinConstant.Action.ACTION_CLICK_ON_BUTTON_SELESAI
import com.tokopedia.profilecompletion.common.analytics.TrackingPinConstant.Action.ACTION_INPUT_CONFIRMATION_PIN_TOKOPEDIA
import com.tokopedia.profilecompletion.common.analytics.TrackingPinConstant.Action.ACTION_INPUT_CREATE_PIN_TOKOPEDIA
import com.tokopedia.profilecompletion.common.analytics.TrackingPinConstant.Category.CATEGORY_PIN_TOKOPEDIA
import com.tokopedia.profilecompletion.common.analytics.TrackingPinConstant.Event.EVENT_CLICK_PIN
import com.tokopedia.profilecompletion.common.analytics.TrackingPinConstant.Label.LABEL_EMPTY
import com.tokopedia.profilecompletion.common.analytics.TrackingPinConstant.Label.LABEL_FAILED
import com.tokopedia.profilecompletion.common.analytics.TrackingPinConstant.Label.LABEL_SUCCESS
import com.tokopedia.track.TrackApp

/**
 * Created by Ade Fulki on 2019-10-14.
 * ade.hadian@tokopedia.com
 */

class TrackingPinUtil {

    fun trackScreen(screenName: String) {
	TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName)
    }

    fun trackClickCreateButton() {
	TrackApp.getInstance().gtm.sendGeneralEvent(
	    EVENT_CLICK_PIN,
	    CATEGORY_PIN_TOKOPEDIA,
	    ACTION_CLICK_ON_BUTTON_CREATE_PIN_TOKOPEDIA,
	    LABEL_EMPTY
	)
    }

    fun trackClickLaterButton() {
	TrackApp.getInstance().gtm.sendGeneralEvent(
	    EVENT_CLICK_PIN,
	    CATEGORY_PIN_TOKOPEDIA,
	    ACTION_CLICK_ON_BUTTON_NANTI_SAJA_PIN_TOKOPEDIA,
	    LABEL_EMPTY
	)
    }

    fun trackClickBackButtonWelcome() {
	TrackApp.getInstance().gtm.sendGeneralEvent(
	    EVENT_CLICK_PIN,
	    CATEGORY_PIN_TOKOPEDIA,
	    ACTION_CLICK_ON_BUTTON_BACK_PIN_TOKOPEDIA,
	    1.toString()
	)
    }

    fun trackClickBackButtonInput() {
	TrackApp.getInstance().gtm.sendGeneralEvent(
	    EVENT_CLICK_PIN,
	    CATEGORY_PIN_TOKOPEDIA,
	    ACTION_CLICK_ON_BUTTON_BACK_PIN_TOKOPEDIA,
	    2.toString()
	)
    }

    fun trackClickBackButtonConfirmation() {
	TrackApp.getInstance().gtm.sendGeneralEvent(
	    EVENT_CLICK_PIN,
	    CATEGORY_PIN_TOKOPEDIA,
	    ACTION_CLICK_ON_BUTTON_BACK_PIN_TOKOPEDIA,
	    3.toString()
	)
    }

    fun trackClickBackButtonSuccess() {
	TrackApp.getInstance().gtm.sendGeneralEvent(
	    EVENT_CLICK_PIN,
	    CATEGORY_PIN_TOKOPEDIA,
	    ACTION_CLICK_ON_BUTTON_BACK_PIN_TOKOPEDIA,
	    4.toString()
	)
    }

    fun trackSuccessInputCreatePin() {
	TrackApp.getInstance().gtm.sendGeneralEvent(
	    EVENT_CLICK_PIN,
	    CATEGORY_PIN_TOKOPEDIA,
	    ACTION_INPUT_CREATE_PIN_TOKOPEDIA,
	    LABEL_SUCCESS
	)
    }

    fun trackFailedInputCreatePin(message: String) {
	TrackApp.getInstance().gtm.sendGeneralEvent(
	    EVENT_CLICK_PIN,
	    CATEGORY_PIN_TOKOPEDIA,
	    ACTION_INPUT_CREATE_PIN_TOKOPEDIA,
	    LABEL_FAILED + message
	)
    }

    fun trackSuccessInputConfirmationPin() {
	TrackApp.getInstance().gtm.sendGeneralEvent(
	    EVENT_CLICK_PIN,
	    CATEGORY_PIN_TOKOPEDIA,
	    ACTION_INPUT_CONFIRMATION_PIN_TOKOPEDIA,
	    LABEL_SUCCESS
	)
    }

    fun trackFailedInputConfirmationPin(message: String) {
	TrackApp.getInstance().gtm.sendGeneralEvent(
	    EVENT_CLICK_PIN,
	    CATEGORY_PIN_TOKOPEDIA,
	    ACTION_INPUT_CONFIRMATION_PIN_TOKOPEDIA,
	    LABEL_FAILED + message
	)
    }

    fun trackClickFinishButton() {
	TrackApp.getInstance().gtm.sendGeneralEvent(
	    EVENT_CLICK_PIN,
	    CATEGORY_PIN_TOKOPEDIA,
	    ACTION_CLICK_ON_BUTTON_SELESAI,
	    LABEL_EMPTY
	)
    }
}