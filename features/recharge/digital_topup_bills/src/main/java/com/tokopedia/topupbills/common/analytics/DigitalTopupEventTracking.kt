package com.tokopedia.topupbills.common.analytics

/**
 * @author by resakemal on 05/07/19.
 */

interface DigitalTopupEventTracking {

    interface Additional {
        companion object {
            const val CURRENT_SITE = "currentSite"
            const val CURRENT_SITE_RECHARGE = "tokopediadigital"
            const val CURRENT_SITE_DIGITAL_RECHARGE = "tokopediadigitalRecharge"
            const val BUSINESS_UNIT = "businessUnit"
            const val BUSINESS_UNIT_RECHARGE = "recharge"
            const val USER_ID = "userId"
            const val CATEGORY = "category"
        }
    }

    interface Event {
        companion object {
            const val CLICK_HOMEPAGE = "clickHomepage"
            const val PROMO_VIEW = "promoView"
            const val PROMO_CLICK = "promoClick"
            const val PRODUCT_VIEW = "productView"
            const val PRODUCT_CLICK = "productClick"

            const val DIGITAL_GENERAL_EVENT = "digitalGeneralEvent"
            const val DIGITAL_GENERAL_EVENT_IRIS = "digitalGeneralEventIris"
        }
    }

    interface Category {
        companion object {
            const val DIGITAL_HOMEPAGE = "digital - homepage"
        }
    }

    interface Action {
        companion object {
            const val INPUT_MANUAL_NUMBER = "input manual number"
            const val INPUT_FROM_CONTACT = "input from contact"
            const val CLICK_ON_CONTACT = "click on contact number"
            const val INPUT_FROM_FAVORITE_NUMBER = "input from favorite number"
            const val CLEAR_INPUT_NUMBER = "click x on input number"
            const val CLICK_BACK_BUTTON = "user click back button"
            const val RECENT_ICON_IMPRESSION = "impression recent icon"
            const val CLICK_RECENT_ICON = "click recent icon"
            const val PROMO_DIGITAL_IMPRESSION = "impression promo digital"
            const val COPY_PROMO_DIGITAL = "click salin promo digital"
            const val CLICK_PROMO_DIGITAL = "click promo digital"
            const val CLICK_CHECK_TAGIHAN = "click cek tagihan"
            const val CLICK_TAB_PROMO = "click tab promo"
            const val CLICK_TAB_RECENT = "click tab recent"
            const val CLICK_DOTS_MENU = "click 3 dots"
            const val VIEW_FAVORITE_NUMBER_CHIP = "view favorite number chip"
            const val VIEW_FAVORITE_CONTACT_CHIP = "view favorite contact chip"
            const val CLICK_FAVORITE_NUMBER_CHIP = "click favorite number chip"
            const val CLICK_FAVORITE_CONTACT_CHIP = "click favorite contact chip"
            const val CLICK_AUTOCOMPLETE_FAVORITE_NUMBER = "click autocomplete fav number"
            const val CLICK_AUTOCOMPLETE_FAVORITE_CONTACT = "click autocomplete fav contact"
        }
    }

    interface EnhanceEccomerce {
        companion object {
            const val NAME = "name"
            const val ID = "id"
            const val PRICE = "price"
            const val BRAND = "brand"
            const val CATEGORY = "category"
            const val LIST = "list"
            const val POSITION = "position"
            const val CREATIVE = "creative"
            const val CREATIVE_URL = "creative_url"
            const val PROMO_ID = "promo_id"
            const val PROMO_CODE = "promo_code"
        }
    }
}
