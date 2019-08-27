package com.tokopedia.vouchergame.common

/**
 * @author by resakemal on 27/08/19.
 */

interface VoucherGameEventTracking {

    interface Event {
        companion object {
            val CLICK_HOMEPAGE = "clickHomepage"
            val CLICK_CATEGORY = "clickCategory"
            val PROMO_VIEW = "promoView"
            val PROMO_CLICK = "promoClick"
            val PRODUCT_VIEW = "productView"
            val PRODUCT_CLICK = "productClick"
            val ADD_TO_CART = "addToCart"
        }
    }

    interface Category {
        companion object {
            val DIGITAL_HOMEPAGE = "digital - homepage"
            val DIGITAL_CATEGORY = "digital - category page"
            val DIGITAL_NATIVE = "digital - native"
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
            val CLICK_BACK_BUTTON = "click back button on operator page"
            val CLICK_SEE_MORE = "user click see more"
            val CLOSE_DETAIL_PRODUCT = "click x on detail product"
            val RECENT_ICON_IMPRESSION = "impression recent icon"
            val CLICK_RECENT_ICON = "click recent icon"
            val PROMO_DIGITAL_IMPRESSION = "impression promo digital"
            val COPY_PROMO_DIGITAL = "click salin promo digital"
            val CLICK_PROMO_DIGITAL = "click promo digital"
            val CLICK_TELCO_CATEGORY = "click on telco category"
            val CLICK_ALL_BANNER = "click lihat semua banner"
            val CLICK_SEARCH_BOX = "click search box"
            val CLICK_SEARCH_RESULT = "click search result"
            val PRODUCT_CART_RESULT_IMPRESSSION = "impression product card result"
            val CLICK_PRODUCT_CART_RESULT = "click product card result"
            val CLEAR_SEARCH_BOX = "click 'x' ticker on search box"
            val OPERATOR_CARD_IMPRESSION = "impression on operator card"
            val CLICK_OPERATOR_CARD = "click on operator card"
            val INPUT_NUMBER = "user input number"
            val CLICK_INFO_BUTTON = "click info button on operator page"
            val CLICK_BUY = "click beli"
        }
    }

    interface Screen {
        companion object {
            val VOUCHER_GAME = "/digital/voucher game"
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
