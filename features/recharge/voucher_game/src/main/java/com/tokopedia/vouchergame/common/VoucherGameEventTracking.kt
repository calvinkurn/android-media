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
            val PRODUCT_CARD_IMPRESSION = "impression on product card"
            val CLICK_PRODUCT_CARD = "click on product card"
            val CLICK_BACK_BUTTON = "click back button on operator page"
            val BANNER_IMPRESSION = "impression banner digital"
            val CLICK_BANNER = "click banner digital"
            val CLICK_ALL_BANNER = "click lihat semua banner"
            val CLICK_SEARCH_BOX = "click search box"
            val CLICK_SEARCH_RESULT = "click search result"
            val PRODUCT_CART_RESULT_IMPRESSSION = "impression product card result"
            val CLICK_PRODUCT_CART_RESULT = "click product card result"
            val CLEAR_SEARCH_BOX = "click `x` ticker on search box"
            val OPERATOR_CARD_IMPRESSION = "impression on operator card"
            val CLICK_OPERATOR_CARD = "click on operator card"
            val INPUT_NUMBER = "user input number"
            val CLICK_INFO_BUTTON = "click info button on operator page"
            val CLICK_BUY = "click beli"
        }
    }

    interface EnhanceEccomerce {
        companion object {
            var NAME = "name"
            var ID = "id"
            var PRICE = "price"
            var BRAND = "brand"
            var CATEGORY = "category"
            var LIST = "list"
            var POSITION = "position"
            var CREATIVE = "creative"
            var CREATIVE_URL = "creative_url"
            var PROMO_ID = "promo_id"
            var PROMOTIONS = "promotions"
        }
    }
}
