package com.tokopedia.otp.common.analytics

/**
 * Created by Ade Fulki on 2019-10-23.
 * ade.hadian@tokopedia.com
 */

object TrackingValidatorConstant{

    object Screen{
        const val SCREEN_ACCOUNT_ACTIVATION = "Account Activation Page"
    }

    object Event{
        const val EVENT_CLICK_ACTIVATION = "clickActivation"
    }

    object Category{
        const val CATEGORY_ACTIVATION_PAGE = "activation page"
    }

    object Action{
        const val ACTION_CLICK_ON_BUTTON_AKTIVASI = "click on button aktivasi"
        const val ACTION_CLICK_KIRIM_ULANG = "click kirim ulang"
        const val ACTION_CLICK_OK_KIRIM_ULANG = "click ok (kirim ulang email)"
        const val ACTION_CLICK_UBAH_EMAIL_ACTIVATION = "click ubah email"
    }

    object Label{
        const val LABEL_EMPTY = ""
        const val LABEL_CLICK = "click"
        const val LABEL_SUCCESS = "success"
        const val LABEL_FAILED = "failed - "
    }
}