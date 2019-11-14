package com.tokopedia.otp.common.analytics

/**
 * Created by Ade Fulki on 2019-10-23.
 * ade.hadian@tokopedia.com
 */

object TrackingValidatorConstant{

    object Screen{
        val SCREEN_ACCOUNT_ACTIVATION = "Account Activation Page"
    }

    object Event{
        val EVENT_CLICK_ACTIVATION = "clickActivation"
    }

    object Category{
        val CATEGORY_ACTIVATION_PAGE = "activation page"
    }

    object Action{
        val ACTION_CLICK_ON_BUTTON_AKTIVASI = "click on button aktivasi"
        val ACTION_CLICK_KIRIM_ULANG = "click kirim ulang"
        val ACTION_CLICK_OK_KIRIM_ULANG = "click ok (kirim ulang email)"
        val ACTION_CLICK_UBAH_EMAIL_ACTIVATION = "click ubah email"
    }

    object Label{
        val LABEL_EMPTY = ""
        val LABEL_CLICK = "click"
        val LABEL_SUCCESS = "success"
        val LABEL_FAILED = "failed - "
    }
}