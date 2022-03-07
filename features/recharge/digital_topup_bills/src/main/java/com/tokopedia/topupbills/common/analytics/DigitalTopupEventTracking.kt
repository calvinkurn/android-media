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
            const val DIGITAL_SCREEN_NAME = "/digital/"
            const val SCREEN_NAME = "screenName"
            const val IS_LOGIN_STATUS = "isLoggedInStatus"
            const val CATEGORY = "category"
            const val CATEGORY_ID = "digitalCategoryId"
            const val VALUE_ITEMS = "items"
            const val VALUE_PROMOTIONS = "promotions"
            const val VALUE_ITEM_LIST = "item_list"

            const val REGULAR_PRODUCT = "regular product"
            const val SPECIAL_PROMO = "special product promo"
        }
    }

    interface Event {
        companion object {
            const val CLICK_HOMEPAGE = "clickHomepage"
            const val VIEW_HOMEPAGE_IRIS = "viewHomepageIris"
            const val PROMO_VIEW = "promoView"
            const val PROMO_CLICK = "promoClick"
            const val PRODUCT_VIEW = "productView"
            const val PRODUCT_CLICK = "productClick"

            const val VIEW_ITEM = "view_item"
            const val VIEW_ITEM_LIST = "view_item_list"
            const val SELECT_CONTENT = "select_content"
            const val DIGITAL_GENERAL_EVENT = "digitalGeneralEvent"
            const val DIGITAL_GENERAL_EVENT_IRIS = "digitalGeneralEventIris"
            const val ADD_TO_CART = "add_to_cart"
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
            const val INPUT_FROM_WIDGET = "input from widget"
            const val INPUT_FROM_FAVORITE_NUMBER = "input from favorite number"
            const val CLEAR_INPUT_NUMBER = "click x on input number"
            const val PRODUCT_CARD_IMPRESSION = "impression product cluster"
            const val CLICK_PRODUCT_CARD = "click product cluster"
            const val CLICK_BACK_BUTTON = "user click back button"
            const val CLICK_SEE_MORE = "user click see more"
            const val CLOSE_DETAIL_PRODUCT = "click x on detail product"
            const val RECENT_ICON_IMPRESSION = "impression recent icon"
            const val CLICK_RECENT_ICON = "click recent icon"
            const val PROMO_DIGITAL_IMPRESSION = "impression promo digital"
            const val COPY_PROMO_DIGITAL = "click salin promo digital"
            const val CLICK_PROMO_DIGITAL = "click promo digital"
            const val CLICK_TELCO_CATEGORY = "click on telco category"
            const val CLICK_CHECK_TAGIHAN = "click cek tagihan"
            const val CLICK_TAB_PROMO = "click tab promo"
            const val CLICK_TAB_RECENT = "click tab recent"
            const val CLICK_DETAIL_CLUSTER = "click pilih detail cluster"
            const val VIEW_PROMO_CARD = "view promo card"
            const val CLICK_PROMO_CARD = "click promo card"
            const val CLICK_DETAIL_IN_PROMO_CARD = "click detail in promo card"
            const val CLICK_DOTS_MENU = "click 3 dots"
            const val CLICK_QUICK_FILTER = "click quick filter"
            const val CLICK_SAVE_QUICK_FILTER = "click Simpan"
            const val CLICK_RESET_QUICK_FILTER = "click Reset filter bottom sheet"
            const val CLICK_RESET_FILTER_CLUSTER = "click reset cluster"
            const val IMPRESSION_FILTER_CLUSTER = "impression filter cluster"
            const val VIEW_FAVORITE_NUMBER_CHIP = "view favorite number chip"
            const val VIEW_FAVORITE_CONTACT_CHIP = "view favorite contact chip"
            const val CLICK_FAVORITE_NUMBER_CHIP = "click favorite number chip"
            const val CLICK_FAVORITE_CONTACT_CHIP = "click favorite contact chip"
            const val CLICK_AUTOCOMPLETE_FAVORITE_NUMBER = "click autocomplete fav number"
            const val CLICK_AUTOCOMPLETE_FAVORITE_CONTACT = "click autocomplete fav contact"
            const val VIEW_SPECIAL_PRODUCT_PROMO = "view special product promo"
            const val CLICK_SPECIAL_PRODUCT_PROMO = "click special product promo"
        }
    }

    interface EnhanceEccomerce {
        companion object {
            const val PARAM_ITEM_ID = "item_id"
            const val PARAM_ITEM_NAME = "item_name"
            const val PARAM_ITEM_BRAND = "item_brand"
            const val PARAM_ITEM_CATEGORY = "item_category"
            const val PARAM_ITEM_VARIANT = "item_variant"
            const val PARAM_QUANTITY = "quantity"
            const val PARAM_PRICE = "price"
            const val PARAM_INDEX = "index"
            const val PARAM_DIMENSION = "dimension40"

            const val NAME = "name"
            const val ID = "id"
            const val PRICE = "price"
            const val BRAND = "brand"
            const val CATEGORY = "category"
            const val LIST = "list"
            const val POSITION = "position"
            const val CREATIVE = "creative"
            const val CREATIVE_URL = "creative_url"
            const val CREATIVE_NAME = "creative_name"
            const val CREATIVE_SLOT = "creative_slot"
            const val PROMO_ID = "promo_id"
            const val PROMO_CODE = "promo_code"
            const val QUANTITY = "quantity"
            const val VARIANT = "variant"
        }
    }
}
