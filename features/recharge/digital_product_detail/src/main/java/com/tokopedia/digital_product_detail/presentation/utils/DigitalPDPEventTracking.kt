package com.tokopedia.digital_product_detail.presentation.utils

object DigitalPDPEventTracking {

    object Additional {
        const val USER_ID = "userId"
        const val CURRENT_SITE = "currentSite"
        const val CURRENT_SITE_DIGITAL_RECHARGE = "tokopediadigitalRecharge"
        const val BUSINESS_UNIT = "businessUnit"
        const val BUSINESS_UNIT_RECHARGE = "recharge"
        const val ITEMS = "items"
        const val ITEM_LIST = "item_list"
        const val PROMOTIONS = "promotions"
        const val IS_LOGGEDIN_STATUS = "isLoggedInStatus"
        const val SCREEN_NAME = "screenName"
        const val TRACKER_ID = "trackerId"

        const val INDEX = "index"
        const val ITEM_BRAND = "item_brand"
        const val ITEM_CATEGORY = "item_category"
        const val ITEM_ID = "item_id"
        const val ITEM_NAME = "item_name"
        const val ITEM_VARIANT = "item_variant"
        const val PRICE = "price"
        const val CREATIVE_NAME = "creative_name"
        const val CREATIVE_SLOT = "creative_slot"

        const val EMPTY_DISCOUNT_PRICE = "Rp0"
        const val MCCM = "mccm"
        const val FLASH_SALE = "flash sale"
        const val REGULAR = "regular"

        const val CATEGORY_ID = "category_id"
        const val DIMENSION45 = "dimension45"
        const val DIMENSION40 = "dimension40"
        const val QUANTITY = "quantity"
        const val SHOP_ID = "shop_id"
        const val SHOP_NAME = "shop_name"
        const val SHOP_TYPE = "shop_type"
    }

    object Event {
        const val VIEW_DIGITAL_IRIS = "viewDigitalIris"
        const val VIEW_ITEM_LIST = "view_item_list"
        const val VIEW_ITEM = "view_item"
        const val CLICK_DIGITAL = "clickDigital"
        const val SELECT_CONTENT = "select_content"
        const val ADD_TO_CART = "add_to_cart"
        const val OPEN_SCREEN = "openScreen"
    }

    object Category {
        const val DIGITAL_HOMEPAGE = "digital - homepage"
        const val DIGITAL_NATIVE = "digital - native"
    }

    object Action {
        const val INPUT_MANUAL_NUMBER = "input manual number"
        const val INPUT_FROM_CONTACT = "input from contact"
        const val INPUT_FROM_FAVORITE_NUMBER = "input from favorite number"
        const val VIEW_FAVORITE_NUMBER_CHIP = "view favorite number chip"
        const val VIEW_FAVORITE_CONTACT_CHIP = "view favorite contact chip"
        const val VIEW_PROMO_CARD = "view promo card"
        const val IMPRESSION_PROMO_CLUSTER = "impression promo cluster"
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
        const val CLICK_PROMO_CLUSTER = "click promo cluster"
        const val CLICK_PROMO_CARD = "click promo card"

        const val IMPRESSION_PRODUCT_CLUSTER = "impression product cluster"
        const val IMPRESSION_LAST_TRANSACTION_ICON = "impression last transaction icon"
        const val IMPRESSION_PDP_BANNER = "impression pdp banner"
        const val IMPRESSION_FILTER_CHIP = "impression filter chip"
        const val IMPRESSION_OMNI_CHANNEL_WIDGET = "impression omni channel widget"
        const val CLICK_FILTER_CHIP = "click filter chip"
        const val CLICK_CHEVRON_IN_PRODUCT_CLUSTER = "click chevron in product cluster"
        const val CLICK_CHEVRON_IN_PROMO_CARD = "click chevron in promo card"
        const val CLICK_TRANSACTION_HISTORY_ICON = "click transaction history icon"
        const val CLICK_SCAN_BARCODE = "click scan barcode"
        const val CLICK_TRANSACTION_DETAIL_INFO = "click transaction detail info"
        const val CLICK_LIST_FAVORITE_NUMBER = "click list favorite number"
        const val CLICK_LANJUT_BAYAR = "click beli"
        const val CLICK_OMNI_CHANNEL_WIDGET = "click omni channel widget"
        const val VIEW_PDP_PAGE = "view pdp page"
        const val CLICK_CHEVRON_SHOW_MORE = "click chevron show more"
        const val CLICK_CLOSE_BUTTON_PRODUCT_DESC = "click close button"
        const val IMPRESS_PRODUCT_DESC = "impression product detail"
    }

    object TrackerId {
        const val CLICK_OMNI_CHANNEL_WIDGET = "42519"
        const val IMPRESSION_OMNI_CHANNEL_WIDGET = "42730"
        const val CLICK_CHEVRON_PROMOTION = "43960"
        const val CLICK_PRODUCT_PROMOTION = "43968"
        const val CLICK_SHOW_MORE_PROMOTION = "43965"
        const val CLICK_CLOSE_PRODUCT_DESC = "43963"
        const val IMPRESS_BOTTOM_SHEET_PRODUCT_DESC = "43961"
        const val IMPRESS_PROMO_CLUSTER_PRODUCT = "43964"
        const val IMPRESS_OLD_MCCM = "26157"
        const val CLICK_OLD_MCCM = "26163"
    }
}
