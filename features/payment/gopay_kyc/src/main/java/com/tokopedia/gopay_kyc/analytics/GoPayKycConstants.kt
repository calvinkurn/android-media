package com.tokopedia.gopay_kyc.analytics

object GoPayKycConstants {

    const val KEY_CURRENT_SITE = "currentSite"
    const val VALUE_CURRENT_SITE = "tokopediamarketplace"
    const val KEY_BUSINESS_UNIT = "businessUnit"
    const val VALUE_BUSINESS_UNIT = "Payment"
    const val KEY_USER_ID = "userId"
    const val KEY_SCREEN_NAME = "screenName"
    const val KEY_LOGGED_IN_STATUS = "isLoggedInStatus"
    const val PAGE_SOURCE = "pageSource"

    internal interface Event {
        companion object {
            const val OPEN_SCREEN= "openScreen"
            const val CLICK_PAYMENT = "clickPayment"
            const val VIEW_IMPRESSION = "viewPaymentIris"
        }
    }

    internal interface Category {
        companion object {
            const val PEMUDA_KYC_PAGE = "pemuda kyc page"
        }
    }
    internal interface Action {
        companion object {
            const val VIEW_PEMUDA_KYC = "view pemuda kyc page"
            const val CLICK_BACK_KYC = "click back kyc page"
            const val CLICK_UPGRADE_KYC = "click upgrade kyc page"
            const val CLICK_TAKE_PHOTO = "click ambil foto"
            const val CLICK_TAKE_PHOTO_AGAIN = "click foto ulang"
            const val CLICK_USE_PHOTO = "click pakai foto"
            const val CLICK_PROCEED_UPGRADE = "click lanjut upgrade"
            const val CLICK_EXIT_UPGRADE = "click keluar halaman"
            const val CLICK_SUMBIT_KYC = "click kirim halaman summary"
            const val CLICK_TNC_SUMMARY_PAGE = "click syarat dan ketentuan halaman summary"
            const val CLICK_OK_SUCCESS = "click oke halaman sukses"
            const val IMPRESSION_UPLOAD_FAILED_BOTTOMSHEET = "view proses gagal bottomsheet"
            const val CLICK_RETRY_AGAIN = "click kirim ulang"
        }
    }
    internal interface Label {
        companion object {
            const val KTP = "KTP"
            const val SELFIE_KTP = "Selfie KTP"
        }
    }

    interface ScreenNames {
        companion object {
            const val GOPAY_DASHBOARD = "Gopay Dashboard"
            const val GOPAY_KYC_BENEFIT_PAGE = "Gopay Plus Page"
            const val GOPAY_KYC_INSTRUCTION_PAGE = "GopayKYC KTP Instruction"
            const val GOPAY_KYC_REVIEW_KTP_PAGE = "GopayKYC Review KTP Page"
            const val GOPAY_KYC_SELFIE_KTP_INSTRUCTION_PAGE = "GopayKYC Selfie KTP Instruction"
            const val GOPAY_KYC_REVIEW_SELFIE_INSTRUCTION_PAGE = "GopayKYC Review Selfie KTP Page"
            const val GOPAY_KYC_SUMMARY_PAGE = "GopayKYC Summary Page"
            const val GOPAY_KYC_SUCCESS_PAGE = "GopayKYC Success Upload KYC"
        }
    }
}