package com.tokopedia.topupbills.common

/**
 * @author by resakemal on 05/07/19.
 */

interface DigitalTopupEventTracking {

    interface Event {
        companion object {
            val CLICK_HOMEPAGE = "clickHomepage"
            val PROMO_VIEW = "promoView"
            val PROMO_CLICK = "promoClick"
            val PRODUCT_VIEW = "productView"
            val PRODUCT_CLICK = "productClick"
        }
    }

    interface Category {
        companion object {
            val DIGITAL_HOMEPAGE = "digital - homepage"
        }
    }

    interface Action {
        companion object {
            val INPUT_MANUAL_NUMBER = "input manual number"
            val INPUT_FROM_CONTACT = "input from contact"
            val CLICK_ON_CONTACT = "click on contact number"
            val INPUT_FROM_WIDGET = "input from widget"
            val INPUT_FROM_FAVORITE_NUMBER = "input from favorite number"
            val CLEAR_INPUT_NUMBER = "click x on input number"
            val CLICK_TELCO_TAB = "click on telco tab"
            val PRODUCT_CARD_IMPRESSION = "impression on product card"
            val CLICK_PRODUCT_CARD = "click on product card"
            val CLICK_BACK_BUTTON = "user click back button"
            val CLICK_SEE_MORE = "user click see more"
            val CLOSE_DETAIL_PRODUCT = "click x on detail product"
            val RECENT_ICON_IMPRESSION = "impression recent icon"
            val CLICK_RECENT_ICON = "click recent icon"
            val PROMO_DIGITAL_IMPRESSION = "impression promo digital"
            val COPY_PROMO_DIGITAL = "click salin promo digital"
            val CLICK_PROMO_DIGITAL = "click promo digital"
            val CLICK_TELCO_CATEGORY = "click on telco category"
        }
    }

    interface Screen {
        companion object {
            val DIGITAL_TELCO_PREPAID = "/digital/pra bayar"
            val DIGITAL_TELCO_POSTPAID = "/digital/pasca bayar"
        }
    }

    interface EnhanceEccomerce {
        companion object {
            internal var NAME = "name"
            internal var ID = "id"
            internal var PRICE = "price"
            internal var BRAND = "brand"
            internal var CATEGORY = "category"
            internal var LIST = "list"
            internal var POSITION = "position"
            internal var CREATIVE = "creative"
            internal var CREATIVE_URL = "creative_url"
            internal var PROMO_ID = "promo_id"
            internal var PROMO_CODE = "promo_code"
        }
    }
}
