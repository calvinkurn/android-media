package com.tokopedia.topupbills.common.analytics

/**
 * @author by resakemal on 05/07/19.
 */

interface DigitalTopupEventTracking {

    interface Additional {
        companion object {
            const val CURRENT_SITE = "currentSite"
            const val CURRENT_SITE_RECHARGE = "tokopediadigitalRecharge"
            const val BUSINESS_UNIT = "businessUnit"
            const val BUSINESS_UNIT_RECHARGE = "top up and tagihan"
            const val USER_ID = "userId"
            const val DIGITAL_SCREEN_NAME = "/digital/"
            const val IS_LOGIN_STATUS = "isLoggedInStatus"
            const val CATEGORY = "category"
            const val CATEGORY_ID = "digitalCategoryId"
        }
    }

    interface Event {
        companion object {
            val CLICK_HOMEPAGE = "clickHomepage"
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
            val PRODUCT_CARD_IMPRESSION = "impression on product cluster"
            val CLICK_PRODUCT_CARD = "click product cluster"
            val CLICK_BACK_BUTTON = "user click back button"
            val CLICK_SEE_MORE = "user click see more"
            val CLOSE_DETAIL_PRODUCT = "click x on detail product"
            val RECENT_ICON_IMPRESSION = "impression recent icon"
            val CLICK_RECENT_ICON = "click recent icon"
            val PROMO_DIGITAL_IMPRESSION = "impression promo digital"
            val COPY_PROMO_DIGITAL = "click salin promo digital"
            val CLICK_PROMO_DIGITAL = "click promo digital"
            val CLICK_TELCO_CATEGORY = "click on telco category"
            val CLICK_CHECK_TAGIHAN = "click cek tagihan"
            val CLICK_TAB_PROMO = "click tab promo"
            val CLICK_TAB_RECENT = "click tab recent"
            val CLICK_DETAIL_CLUSTER = "click pilih detail cluster"
            val CLICK_DOTS_MENU = "click 3 dots"
            val CLICK_QUICK_FILTER = "click quick filter"
            val CLICK_SAVE_QUICK_FILTER = "click Simpan"
            val CLICK_RESET_QUICK_FILTER = "click Reset filter bottom sheet"
            val CLICK_RESET_FILTER_CLUSTER = "click reset cluster"
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
            internal var QUANTITY = "quantity"
            internal var VARIANT = "variant"
        }
    }
}
