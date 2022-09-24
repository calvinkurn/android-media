package com.tokopedia.epharmacy.utils

interface EventKeys {
    companion object {
        const val SCREEN_NAME = "screenName"
        const val IS_LOGGED_IN = "isLoggedInStatus"
        const val OPEN_SCREEN = "openScreen"
        const val VIEW_CONTENT_IRIS = "viewContentIris"
        const val CLICK_CONTENT = "clickContent"
        const val TRACKER_ID = "trackerId"
        const val PAGE_PATH = "pagePath"

        const val BUSINESS_UNIT_VALUE= "Physical Goods"
        const val CURRENT_SITE_VALUE = "tokopediamarketplace"
    }
}

interface CategoryKeys {
    companion object {
        const val UPLOAD_PRESCRIPTION_PAGE = "upload prescription page"
    }
}

interface ActionKeys {
    companion object {
        const val IMAGE_UPLOAD_FAILED = "upload failed - new flow"
        const val IMAGE_UPLOAD_SUCCESS = "upload success - new flow"
        const val UPLOAD_PRESCRIPTION = "upload prescription - new flow"
        const val SUBMIT_PRESCRIPTION = "submit prescription - new flow"
        const val SUBMIT_SUCCESS = "submit success - new flow"
    }
}

interface TrackerId {
    companion object {
        const val OPEN_SCREEN_ID  = "32358"
        const val IMAGE_UPLOAD_FAILED_ID = "32787"
        const val IMAGE_UPLOAD_SUCCESS_ID = "32782"
        const val UPLOAD_PRESCRIPTION_ID = "32781"
        const val SUBMIT_PRESCRIPTION_ID = "32783"
        const val SUBMIT_SUCCESS_ID = "32786"
    }
}