package com.tokopedia.otp.common.analytics

/**
 * Created by Ade Fulki on 2019-10-23.
 * ade.hadian@tokopedia.com
 */

object TrackingValidatorConstant{

    object Screen{
        const val SCREEN_COTP = "Input OTP "
        const val SCREEN_COTP_MISSCALL = "Input OTP miscall"
        const val SCREEN_ACCOUNT_ACTIVATION = "Account Activation Page"
        const val SCREEN_VERIFICATION_METHOD = "change method"
    }

    object Event{
        const val EVENT_CLICK_ACTIVATION = "clickActivation"
        const val EVENT_CLICK_OTP = "clickOtp"
        const val EVENT_CLICK_REGISTER = "clickRegister"
        const val EVENT_CLICK_BACK = "clickBack"
        const val EVENT_CLICK_CONFIRM = "clickConfirm";
    }

    object Category{
        const val CATEGORY_ACTIVATION_PAGE = "activation page"
        const val CATEGORY_CHOOSE_OTP_PAGE = "choose otp page"
        const val CATEGORY_INPUT_OTP_PAGE = "input otp page"
        const val CATEGORY_REGISTER_WITH_PHONE_NUMBER_OTP = "register with phone number otp"
    }

    object Action{
        const val ACTION_CLICK_ON_BUTTON_AKTIVASI = "click on button aktivasi"
        const val ACTION_CLICK_KIRIM_ULANG = "click kirim ulang"
        const val ACTION_CLICK_ON_KIRIM_ULANG = "click on kirim ulang"
        const val ACTION_CLICK_OK_KIRIM_ULANG = "click ok (kirim ulang email)"
        const val ACTION_CLICK_UBAH_EMAIL_ACTIVATION = "click ubah email"
        const val ACTION_CLICK_ON_OTP_METHOD = "click on otp method"
        const val ACTION_CLICK_ON_BUTTON_INACTIVE_PHONE_NUMBER = "click on button nomor saya sudah tidak aktif"
        const val ACTION_CLICK_ON_GUNAKAN_METODE_LAIN = "click on gunakan metode verifikasi lain"
        const val ACTION_CLICK_BACK_BUTTON = "click back button"
        const val ACTION_CLICK_ON_BUTTON_BACK = "click on button back"
        const val ACTION_CLICK_ON_VERIFIKASI  = "click on verifikasi"
        const val ACTION_CLICK_ON_BUTTON_VERIFIKASI = "click on button verifikasi"
    }

    object Label{
        const val LABEL_EMPTY = ""
        const val LABEL_CLICK = "click"
        const val LABEL_SUCCESS = "success"
        const val LABEL_FAILED = "failed - "
    }
}