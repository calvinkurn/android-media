package com.tokopedia.epharmacy.utils

interface EventKeys {
    companion object {
        const val KEY_EVENT = "event"
        const val KEY_EVENT_CATEGORY = "eventCategory"
        const val KEY_EVENT_ACTION = "eventAction"
        const val KEY_EVENT_LABEL = "eventLabel"
        const val KEY_USER_ID = "userId"
        const val SCREEN_NAME = "screenName"
        const val IS_LOGGED_IN = "isLoggedInStatus"
        const val OPEN_SCREEN = "openScreen"
        const val VIEW_CONTENT_IRIS = "viewContentIris"
        const val CLICK_CONTENT = "clickContent"
        const val TRACKER_ID = "trackerId"
        const val PAGE_PATH = "pagePath"

        const val BUSINESS_UNIT_VALUE= "Physical Goods"
        const val CURRENT_SITE_VALUE = "tokopediamarketplace"

        const val EVENT_VALUE_CLICK = "clickAffiliate"
        const val EVENT_VALUE_VIEW = "viewAffiliateIris"
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

interface LabelKeys {
    companion object {
        const val PRODUCT_INACTIVE = "product inactive"
    }
}