package com.tokopedia.sellerappwidget.analytics

/**
 * Created By @ilhamsuaib on 11/12/20
 */

object TrackingConstant {

    object Key {
        const val KEY_CURRENT_SITE = "currentSite"
        const val KEY_BUSINESS_UNIT = "businessUnit"
        const val KEY_USER_ID = "userId"
    }

    object Event {
        const val CLICK_CHAT_WIDGET = "clickChatWidget"
        const val VIEW_CHAT_WIDGET = "viewChatWidgetIris"
    }

    object Category {
        const val SA = "sa"
        const val CHAT_WIDGET = "$SA - chat widget"
        const val ORDER_WIDGET = "$SA - order widget"
    }

    object Action {
        const val CLICK_SELLER_ICON = "click seller icon"
        const val CLICK_REFRESH_BUTTON = "click refresh button"
        const val CLICK_CHAT_LINE = "click chat line"
        const val CLICK_CHECK_NOW = "click check now"
        const val CLICK_LOGIN_NOW = "click login now"
        const val CLICK_SHOP_NAME_AND_CHAT = "click shop name and chat"
        const val CLICK_SHOP_NAME_AND_ORDER = "click shop name and order"
        const val CLICK_ORDER_LINE = "click order line"
        const val CLICK_BUTTON_STATUS = "click button status"
        const val CLICK_NEW_ORDER_SMALL_ORDER = "click active 4x2 order widget - pesanan baru"
        const val CLICK_READY_TO_SHIP_SMALL_ORDER = "click active 4x2 order widget - siap dikirim"
        const val IMPRESSION_ACTIVE_STATE = "impression active state"
        const val IMPRESSION_EMPTY_STATE = "impression empty state"
        const val IMPRESSION_LOADING_STATE = "impression loading state"
        const val IMPRESSION_NO_LOGIN_STATE = "impression no login state"
        const val IMPRESSION_NO_CONNECTION_STATE = "impression no connection state"
        const val IMPRESSION_ACTIVE_NEW_ORDER = "impression active new order"
        const val IMPRESSION_ACTIVE_READY_SHIPPING = "impression active ready shipping"
        const val IMPRESSION_NEW_ORDER_SMALL_ORDER = "impression active 4x2 order widget - pesanan baru"
    }

    object Value {
        const val PHYSICAL_GOODS = "physical goods"
        const val TOKOPEDIASELLER = "tokopediaseller"
    }
}