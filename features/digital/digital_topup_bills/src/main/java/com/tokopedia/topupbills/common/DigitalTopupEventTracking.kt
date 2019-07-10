package com.tokopedia.topupbills.common

/**
 * @author by furqan on 13/08/18.
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
            val CLICK_SEE_MORE = "click_see_more"
            val CLOSE_DETAIL_PRODUCT = "click x on detail product"
            val RECENT_ICON_IMPRESSION = "impression recent icon"
            val CLICK_RECENT_ICON = "click recent icon"
            val PROMO_DIGITAL_IMPRESSION = "impression promo digital"
            val COPY_PROMO_DIGITAL = "click salin promo digital"
            val CLICK_TELCO_CATEGORY = "click on telco category"
        }
    }

    interface Label {
        companion object {
            val DEFAULT_EMPTY_VALUE = ""
            val NO_PROMO = "no promo"
            val PROMO = "no promo"
            val SITE = "tokopediadigital"
            val PRODUCT = "Product - "
            val DIGITAL = "Digital"
        }
    }


    interface Screen {
        companion object {
            val DIGITAL_CATEGORY = "/digital/"
            val DIGITAL_CHECKOUT = "/digital/checkout"
        }
    }

    interface Misc {
        companion object {
            val ACTION_FIELD_STEP1 = "cart page loaded"
            val ACTION_FIELD_STEP2 = "click payment option button"
        }
    }
}
