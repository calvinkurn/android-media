package com.tokopedia.otp.common.analytics

/**
 * Created by Ade Fulki on 2019-10-23.
 * ade.hadian@tokopedia.com
 */

object TrackingOtpConstant{

    object Screen{
        const val SCREEN_COTP = "Input OTP "
        const val SCREEN_COTP_MISSCALL = "Input OTP miscall"
        const val SCREEN_ACCOUNT_ACTIVATION = "Account Activation Page"
        const val SCREEN_VERIFICATION_METHOD = "change method"
        const val SCREEN_PUSH_NOTIF_RECEIVE = "OTP Push Notif - Receive page"
        const val SCREEN_PUSH_NOTIF_RECEIVE_SUCCESS = "OTP Push Notif - Receive success page"
        const val SCREEN_PUSH_NOTIF_RECEIVE_FAILED_NO_PIN = "OTP Push Notif - Receive failed no PIN page"
        const val SCREEN_PUSH_NOTIF_RECEIVE_FAILED_WITH_PIN = "OTP Push Notif - Receive failed with PIN page"
        const val SCREEN_PUSH_NOTIF_RECEIVE_FAILED_WITH_PASSWORD = "OTP Push Notif - Receive failed with password page"
        const val SCREEN_PUSH_NOTIF_RECEIVE_FAILED_FROM_OTHER_DEVICE = "OTP Push Notif - Receive failed from other device"
        const val SCREEN_PUSH_NOTIF_RECEIVE_FAILED_EXPIRED = "OTP Push Notif - Receive failed otp expired"
        const val SCREEN_PUSH_NOTIF_SETTING = "OTP Push Notif - Setting page"
    }

    object Event{
        const val EVENT_CLICK_ACTIVATION = "clickActivation"
        const val EVENT_CLICK_OTP = "clickOtp"
        const val EVENT_CLICK_REGISTER = "clickRegister"
        const val EVENT_CLICK_BACK = "clickBack"
        const val EVENT_CLICK_CONFIRM = "clickConfirm"
        const val EVENT_VIEW_PUSH_NOTIF_IRIS = "viewPushNotifIris"
    }

    object Category{
        const val CATEGORY_ACTIVATION_PAGE = "activation page"
        const val CATEGORY_CHOOSE_OTP_PAGE = "choose otp page"
        const val CATEGORY_INPUT_OTP_PAGE = "input otp page"
        const val CATEGORY_REGISTER_WITH_PHONE_NUMBER_OTP = "register with phone number otp"
        const val CATEGORY_PUSH_NOTIF_RECEIVE_PAGE = "otp push notif - receive page"
        const val CATEGORY_PUSH_NOTIF_RECEIVE_SUCCESS_PAGE = "otp push notif - receive success page"
        const val CATEGORY_PUSH_NOTIF_RECEIVE_FAILED_NO_PIN_PAGE = "otp push notif - receive failed no pin page"
        const val CATEGORY_PUSH_NOTIF_RECEIVE_FAILED_WITH_PIN_PAGE = "otp push notif - receive failed with pin page"
        const val CATEGORY_PUSH_NOTIF_RECEIVE_FAILED_WITH_PASSWORD_PAGE = "otp push notif - receive failed with password page"
        const val CATEGORY_PUSH_NOTIF_RECEIVE_FAILED_FROM_OTHER_DEVICE_PAGE = "otp push notif - receive failed from other device page"
        const val CATEGORY_PUSH_NOTIF_RECEIVE_FAILED_OTP_EXPIRED_PAGE = "otp push notif - receive failed otp expired page"
        const val CATEGORY_PUSH_NOTIF_SETTING_PAGE = "otp push notif - setting page"
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
        const val ACTION_VIEW_OTP_PUSH_NOTIF_RECEIVE_PAGE = "view otp push notif - receive page"
        const val ACTION_CLICK_ON_BUTTON_TOLAK_AKSES = "click on button tolak akses"
        const val ACTION_CLICK_ON_BUTTON_TERIMA_AKSES = "click on button terima akses"
        const val ACTION_VIEW_OTP_PUSH_NOTIF_RECEIVE_SUCCESS_PAGE = "view otp push notif - receive success page"
        const val ACTION_CLICK_ON_BUTTON_CLOSE = "click on button close"
        const val ACTION_CLICK_ON_BUTTON_TUTUP = "click on button tutup"
        const val ACTION_VIEW_OTP_PUSH_NOTIF_RECEIVE_FAILED_NO_PIN_PAGE = "view otp push notif - receive failed no pin page"
        const val ACTION_VIEW_OTP_PUSH_NOTIF_RECEIVE_FAILED_WITH_PIN_PAGE = "view otp push notif - receive failed with pin page"
        const val ACTION_CLICK_ON_BUTTON_UBAH_PIN = "click on button ubah pin"
        const val ACTION_VIEW_OTP_PUSH_NOTIF_RECEIVE_FAILED_WITH_PASSWORD_PAGE = "view otp push notif - receive failed with password page"
        const val ACTION_CLICK_ON_BUTTON_UBAH_KATA_SANDI = "click on button ubah kata sandi"
        const val ACTION_VIEW_OTP_PUSH_NOTIF_RECEIVE_FAILED_FROM_OTHER_DEVICE_PAGE = "view otp push notif - receive failed from other device page"
        const val ACTION_VIEW_OTP_PUSH_NOTIF_RECEIVE_FAILED_OTP_EXPIRED_PAGE = "view otp push notif - receive failed otp expired page"
        const val ACTION_CLICK_MASUK_LEWAT_NOTIFIKASI = "click masuk lewat notifikasi"
        const val ACTION_VIEW_OTP_PUSH_NOTIF_RECEIVE_SETTING_PAGE = "view otp push notif - setting page"
        const val ACTION_CLICK_PUSH_NOTIF = "click push notification tokopedia"
        const val ACTION_CLICK_ATUR_ULANG_KATA_SANDI = "click atur ulang kata sandi"
        const val ACTION_CLICK_ON_BUTTON_AKTIFKAN_MASUK_LEWAT_NOTIF = "click on button aktifkan masuk lewat notifikasi"
        const val ACTION_CLICK_ON_INACTIVE_PHONE = "click nomor saya sudah tidak aktif"
    }

    object Label{
        const val LABEL_EMPTY = ""
        const val LABEL_CLICK = "click"
        const val LABEL_SUCCESS = "success"
        const val LABEL_FAILED = "failed - "
        const val LABEL_ON = "on"
        const val LABEL_OFF = "off"
    }
}