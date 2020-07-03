package com.tokopedia.otp.common.analytics

/**
 * Created by Ade Fulki on 2019-10-23.
 * ade.hadian@tokopedia.com
 */

object TrackingValidatorConstant{

    object Screen{
        const val SCREEN_COTP = "Input OTP "
        const val SCREEN_ACCOUNT_ACTIVATION = "Account Activation Page"
        const val SCREEN_VERIFICATION_METHOD = "change method"
    }

    object Event{
        const val EVENT_CLICK_ACTIVATION = "clickActivation"
        const val EVENT_CLICK_OTP = "clickOtp"
    }

    object Category{
        const val CATEGORY_ACTIVATION_PAGE = "activation page"
        const val CATEGORY_CHOOSE_OTP_PAGE = "choose otp page"
    }

    object Action{
        const val ACTION_CLICK_ON_BUTTON_AKTIVASI = "click on button aktivasi"
        const val ACTION_CLICK_KIRIM_ULANG = "click kirim ulang"
        const val ACTION_CLICK_OK_KIRIM_ULANG = "click ok (kirim ulang email)"
        const val ACTION_CLICK_UBAH_EMAIL_ACTIVATION = "click ubah email"
        const val ACTION_CLICK_ON_OTP_METHOD = "click on otp method"
        const val ACTION_CLICK_ON_BUTTON_INACTIVE_PHONE_NUMBER = "click on button nomor saya sudah tidak aktif"
    }

    object Label{
        const val LABEL_EMPTY = ""
        const val LABEL_CLICK = "click"
        const val LABEL_SUCCESS = "success"
        const val LABEL_FAILED = "failed - "
    }
}