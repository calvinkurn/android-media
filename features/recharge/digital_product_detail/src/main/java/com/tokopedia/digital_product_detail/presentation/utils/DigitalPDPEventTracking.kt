package com.tokopedia.digital_product_detail.presentation.utils

interface DigitalPDPEventTracking {

    interface Additional {
        companion object {
            const val USER_ID = "userId"
            const val CURRENT_SITE = "currentSite"
            const val CURRENT_SITE_DIGITAL_RECHARGE = "tokopediadigitalRecharge"
            const val BUSINESS_UNIT = "businessUnit"
            const val BUSINESS_UNIT_RECHARGE = "recharge"
            const val ITEMS = "items"

            const val INDEX = "index"
            const val ITEM_BRAND = "item_brand"
            const val ITEM_CATEGORY = "item_category"
            const val ITEM_ID = "item_id"
            const val ITEM_NAME = "item_name"
            const val ITEM_VARIANT = "item_variant"
            const val PRICE = "price"
        }
    }

    interface Event {
        companion object {
            const val VIEW_DIGITAL_IRIS = "viewDigitalIris"
            const val VIEW_ITEM_LIST = "view_item_list"
            const val CLICK_DIGITAL = "clickDigital"
            const val SELECT_CONTENT = "select_content"
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
            const val INPUT_FROM_FAVORITE_NUMBER = "input from favorite number"
            const val VIEW_FAVORITE_NUMBER_CHIP = "view favorite number chip"
            const val VIEW_FAVORITE_CONTACT_CHIP = "view favorite contact chip"
            const val VIEW_PROMO_CARD = "view promo card"
            const val CLICK_FAVORITE_NUMBER_CHIP = "click favorite number chip"
            const val CLICK_FAVORITE_CONTACT_CHIP = "click favorite contact chip"
            const val CLICK_AUTOCOMPLETE_FAVORITE_NUMBER = "click autocomplete fav number"
            const val CLICK_AUTOCOMPLETE_FAVORITE_CONTACT = "click autocomplete fav contact"
            const val CLEAR_INPUT_NUMBER = "click x on input number"
            const val CLICK_ON_CONTACT_ICON = "click on contact icon"
            const val CLICK_PRODUCT_CLUSTER = "click product cluster"
            const val CLICK_LAST_TRANSACTION_ICON = "click last transaction icon"
            const val CLICK_LOGIN_WIDGET = "click login widget"
            const val CLICK_CHEVRON = "click chevron on total bayar"
            const val CLICK_PROMO_CARD = "click promo card"
            const val IMPRESSION_PRODUCT_CLUSTER = "impression product cluster"
            const val IMPRESSION_LAST_TRANSACTION_ICON = "impression last transaction icon"
            const val IMPRESSION_PDP_BANNER = "impression pdp banner"
        }
    }
}