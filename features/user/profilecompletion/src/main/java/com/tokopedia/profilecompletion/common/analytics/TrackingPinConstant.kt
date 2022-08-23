package com.tokopedia.profilecompletion.common.analytics

/**
 * Created by Ade Fulki on 2019-10-13.
 * ade.hadian@tokopedia.com
 */

object TrackingPinConstant {

    object Screen {
	val SCREEN_POPUP_PIN_WELCOME = "pop-up pin welcome"
	val SCREEN_POPUP_PIN_INPUT = "pop-up pin input"
	val SCREEN_POPUP_PIN_CONFIRMATION = "pop-up pin confirmation"
	val SCREEN_POPUP_PIN_SUCCESS = "pop-up pin success"
    }

    object Event {
	val EVENT_CLICK_PIN = "clickPIN"
    }

    object Category {
	val CATEGORY_PIN_TOKOPEDIA = "pin tokopedia"
    }

    object Action {
	val ACTION_CLICK_ON_BUTTON_CREATE_PIN_TOKOPEDIA = "click on button create pin tokopedia"
	val ACTION_CLICK_ON_BUTTON_NANTI_SAJA_PIN_TOKOPEDIA =
	    "click on button nanti saja pin tokopedia"
	val ACTION_CLICK_ON_BUTTON_BACK_PIN_TOKOPEDIA = "click on button back pin tokopedia"
	val ACTION_INPUT_CREATE_PIN_TOKOPEDIA = "input create pin tokopedia"
	val ACTION_INPUT_CONFIRMATION_PIN_TOKOPEDIA = "input confirmation pin tokopedia"
	val ACTION_CLICK_ON_BUTTON_SELESAI = "click on button selesai"
    }

    object Label {
	val LABEL_EMPTY = ""
	val LABEL_SUCCESS = "success"
	val LABEL_FAILED = "failed - "
    }
}