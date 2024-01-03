package com.tokopedia.epharmacy.utils

interface EventKeys {
    companion object {
        const val SCREEN_NAME = "screenName"
        const val IS_LOGGED_IN = "isLoggedInStatus"
        const val OPEN_SCREEN = "openScreen"
        const val VIEW_CONTENT_IRIS = "viewContentIris"
        const val CLICK_CONTENT = "clickContent"
        const val CLICK_PG = "clickPG"
        const val VIEW_PG_IRIS = "viewPGIris"
        const val VIEW_GROCERIES_IRIS = "viewGroceriesIris"
        const val TRACKER_ID = "trackerId"
        const val PAGE_PATH = "pagePath"

        const val BUSINESS_UNIT_VALUE = "Physical Goods"
        const val CURRENT_SITE_VALUE = "tokopediamarketplace"
        const val CLICK_GROCERIES = "clickGroceries"
    }
}

interface CategoryKeys {
    companion object {
        const val UPLOAD_PRESCRIPTION_PAGE = "upload prescription page"
        const val ATTACH_PRESCRIPTION_PAGE = "epharmacy attach prescription page"
        const val EPHARMACY_CHECKOUT_PAGE = "epharmacy checkout page"
        const val EPHARMACY_QUANTITY_CHANGE_BS = "epharmacy quantity change bottom sheet"
        const val EPHARMACY_LOADING_PAGE = "epharmacy loading page"
        const val EPHARMACY_ORDER_DETAIL_PAGE = "epharmacy order detail page"
        const val EPHARMACY_CHAT_DOKTER_CHECKOUT_PAGE = "epharmacy chat dokter checkout page"
        const val EPHARMACY_ERROR_PAGE = "epharmacy error page"
    }
}

interface ActionKeys {
    companion object {
        const val IMAGE_UPLOAD_FAILED = "upload failed - new flow"
        const val IMAGE_UPLOAD_SUCCESS = "upload success - new flow"
        const val UPLOAD_PRESCRIPTION = "upload prescription - new flow"
        const val SUBMIT_PRESCRIPTION = "submit prescription - new flow"
        const val SUBMIT_SUCCESS = "submit success - new flow"

        const val CLICK_ATTACH_PRESCRIPTION_BUTTON = "click attach prescription button"
        const val VIEW_ATTACH_PRESCRIPTION_RESULT = "view attach prescription result"
        const val VIEW_CONSULTATION_WEB_VIEW_URL_GENERATED = "consultation webview url generated"
        const val VIEW_ATTACH_PRESCRIPTION_OPTION_PAGE = "view attach option page"
        const val CLICK_UPLOAD_RESEP_DOKTER = "click upload resep dokter"
        const val CLICK_CHAT_DOKTER = "click chat dokter"
        const val CLICK_LANJUT_KE_PENGIRIMAN = "click lanjut ke pengiriman"
        const val VIEW_NO_DOCTOR_PAGE = "view no doctor page"
        const val CLICK_INGATKAN_SAYA = "click ingatkan saya in error page"
    }
}

interface LabelKeys {
    companion object {
        const val IN_WORKING_HOURS = "in working hours"
        const val OUTSIDE_WORKING_HOURS = "outside working hours"
        const val SUCCESS = "Success"
        const val FAILED = "Failed"
    }
}

interface TrackerId {
    companion object {
        const val OPEN_SCREEN_ID = "32358"
        const val IMAGE_UPLOAD_FAILED_ID = "32787"
        const val IMAGE_UPLOAD_SUCCESS_ID = "32782"
        const val UPLOAD_PRESCRIPTION_ID = "32781"
        const val SUBMIT_PRESCRIPTION_ID = "32783"
        const val SUBMIT_SUCCESS_ID = "32786"

        const val OPEN_SCREEN_ID_ATTACH = "37738"
        const val ATTACH_PRESCRIPTION_BUTTON = "37739"
        const val VIEW_MINI_CONSULTATION = "37741"
        const val ATTACH_PRESCRIPTION_RESULT = "37742"
        const val ATTACH_PRESCRIPTION_OPTIONS = "37744"
        const val CLICK_UPLOAD_RESEP_DOKTER = "37745"
        const val CLICK_CHAT_DOKTER = "37746"
        const val VIEW_NO_DOCTOR = "37747"
        const val CLICK_INGATKAN_SAYA = "37748"
        const val LANJUT_BUTTON_CLICK = "38051"
        const val CHECKOUT_PAGE_EVENT = "45865"
        const val CLICK_PILIH_PEMBAYARAN_EVENT = "45866"
        const val VIEW_QUANTITY_CHANGE_BS = "45874"
        const val ADD_QTY_CHANGE = "45875"
        const val REMOVE_QTY_CHANGE = "45876"
        const val CLICK_PERBAHARUI = "45877"
        const val VIEW_ERROR_ON_QTY_BS = "45878"
        const val VIEW_CHAT_DOKTER_ORDER_DETAIL_PAGE = "45879"
        const val ORDER_DETAIL_MAIN_CTA = "45880"
        const val CLICK_LIHAT_INVOICE = "45881"
        const val VIEW_PRESC_IMAGE_WEBVIEW = "45882"
        const val CLICK_PUSAT_BANTUAN = "45885"
        const val CLICK_BACK_CHECKOUT = "45890"
        const val SUCCESS_QUANTITY_CHANGE = "49199"
        const val FAILED_ORDER_CREATION = "45871"
        const val VIEW_ONGOING_CHAT = "45872"
        const val TICKER_TRACKER_ID = "45873"
    }
}
