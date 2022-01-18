package com.tokopedia.otp.common.analytics

/**
 * Created by Ade Fulki on 2019-10-23.
 * ade.hadian@tokopedia.com
 */

object TrackingOtpConstant{

    const val EVENT_BUSINESS_UNIT = "businessUnit"
    const val EVENT_CURRENT_SITE = "currentSite"
    const val EVENT_USER_ID = "userId"

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
        const val SCREEN_LOGIN_BY_QR_APPROVAL_PAGE = "Approval Page"
        const val SCREEN_LOGIN_BY_QR_SUCCESS_PAGE = "Success page"
        const val SCREEN_LOGIN_BY_QR_EXPIRED_PAGE = "Expired page"
    }

    object Event{
        const val EVENT_CLICK_ACTIVATION = "clickActivation"
        const val EVENT_CLICK_OTP = "clickOtp"
        const val EVENT_CLICK_LOGIN = "clickLogin"
        const val EVENT_CLICK_REGISTER = "clickRegister"
        const val EVENT_CLICK_BACK = "clickBack"
        const val EVENT_CLICK_CONFIRM = "clickConfirm"
        const val EVENT_VIEW_PUSH_NOTIF_IRIS = "viewPushNotifIris"
        const val EVENT_VIEW_OTP = "viewOtp"
        const val EVENT_VIEW_LOGIN_IRIS = "viewLoginIris"
    }

    object Category{
        const val CATEGORY_ACTIVATION_PAGE = "activation page"
        const val CATEGORY_OTP_PAGE = "otp page"
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
        const val CATEGORY_LOGIN_WITH_QR_CODE = "login with qr code"
        const val CATEGORY_SILENT_VERIF_REMINDER = "silent verif reminder page"
        const val CATEGORY_SILENT_VERIF_OTP_PAGE = "silent verif otp page"
    }

    object Action{
        const val ACTION_CLICK_ON_BUTTON_AKTIVASI = "click on button aktivasi"
        const val ACTION_CLICK_KIRIM_ULANG = "click kirim ulang"
        const val ACTION_CLICK_ON_KIRIM_ULANG = "click on kirim ulang"
        const val ACTION_CLICK_RESEND_OTP = "click kirim ulang otp"
        const val ACTION_CLICK_OK_KIRIM_ULANG = "click ok (kirim ulang email)"
        const val ACTION_CLICK_UBAH_EMAIL_ACTIVATION = "click ubah email"
        const val ACTION_CLICK_METHOD_OTP = "click metode otp"
        const val ACTION_CLICK_ON_OTP_METHOD = "click on otp method"
        const val ACTION_CLICK_ON_BUTTON_INACTIVE_PHONE_NUMBER = "click on button nomor saya sudah tidak aktif"
        const val ACTION_CLICK_ON_GUNAKAN_METODE_LAIN = "click gunakan metode lain"
        const val ACTION_CLICK_BACK_BUTTON = "click button back"
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
        const val ACTION_SUCCESSFULLY_SCANNING_QR_CODE = "successfully scanning qr code"
        const val ACTION_APPROVAL_CLICK_ON_BUTTON_APPROVED = "approval - click on button approved"
        const val ACTION_APPROVAL_CLICK_ON_BUTTON_REJECTED = "approval - click on button rejected"
        const val ACTION_APPROVAL_CLICK_ON_BUTTON_BACK = "approval - click on button back"
        const val ACTION_APPROVAL_APPROVED = "approval approved"
        const val ACTION_SUCCESS_CLICK_ON_BUTTON_TUTUP = "success - click on button tutup"
        const val ACTION_SUCCESS_CLICK_ON_BUTTON_CLOSE = "success - click on button close"
        const val ACTION_APPROVAL_EXPIRED = "approval expired"
        const val ACTION_EXPIRED_CLICK_ON_BUTTON_SCAN_KEMBALI = "expired - click on button scan kembali"
        const val ACTION_EXPIRED_CLICK_ON_BUTTON_CLOSE = "expired - click on button close"
        const val ACTION_VIEW_CHOOSE_OTP_PAGE = "view choose otp page"
        const val ACTION_AUTO_SUBMIT_OTP = "auto submit otp page"
        const val ACION_CLICK_SILENT_VERIF = "click on masuk dengan silent verif"
        const val ACION_CLICK_TRY_AGAIN = "click on coba lagi"
        const val ACION_CLICK_CHOOSE_OTHER_METHOD = "click gunakan metode lain"

        /*InactivePhone*/
        const val ACTION_CLICK_ON_REQUEST_CHANGE_PHONE_NUMBER = "click ajukan perubahan nomor hp"
    }

    object Label{
        const val LABEL_EMPTY = ""
        const val LABEL_CLICK = "click"
        const val LABEL_SUCCESS = "success"
        const val LABEL_FAILED = "failed - "
        const val LABEL_ON = "on"
        const val LABEL_OFF = "off"
        const val LABEL_MENGERTI = "mengerti"
        const val LABEL_BATAL = "batal"
        const val LABEL_MODE_LIST = "mode list"
        const val LABEL_OTP_PAGE = "otp page"
    }

    object BusinessUnit {
        const val USER_PLATFORM_UNIT = "user platform"
    }

    object CurrentSite {
        const val TOKOPEDIA_MARKETPLACE_SITE = "tokopediamarketplace"
    }
}