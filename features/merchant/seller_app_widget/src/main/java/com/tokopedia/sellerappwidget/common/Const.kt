package com.tokopedia.sellerappwidget.common

/**
 * Created By @ilhamsuaib on 17/11/20
 */

object Const {

    const val SHARED_PREF_NAME = "seller_app_widget_preferences"

    object OrderStatusId {
        const val NEW_ORDER = 220
        const val READY_TO_SHIP = 400
    }

    object OrderStatusStr {
        const val NEW_ORDER = "new_order"
        const val READY_TO_SHIP = "confirm_shipping"
    }

    object OrderListSortBy {
        const val SORT_BY_PAYMENT_DATE_ASCENDING = 0
        const val SORT_BY_PAYMENT_DATE_DESCENDING = 2
    }

    object Extra {
        const val BUNDLE = "extra_bundle"
        const val SELLER_ORDER = "seller_order"
        const val ORDER_ITEM = "order"
        const val ORDER_ITEMS = "orders"
        const val ORDER_STATUS_ID = "order_status_id"
        const val CHAT_ITEM = "chat"
        const val CHAT_ITEMS = "chats"
        const val WIDGET_SIZE = "widget_size"
    }

    object Action {
        const val REFRESH = "com.tokopedia.sellerappwidget.REFRESH"
        const val ITEM_CLICK = "com.tokopedia.sellerappwidget.ITEM_CLICK"
        const val SWITCH_ORDER = "com.tokopedia.sellerappwidget.SWITCH_ORDER"
        const val OPEN_APPLINK = "com.tokopedia.sellerappwidget.OPEN_APPLINK"
    }

    object Method {
        const val SET_VISIBILITY = "setVisibility"
        const val SET_IMAGE_RESOURCE = "setImageResource"
    }

    object Images {
        const val ORDER_ON_EMPTY = "https://ecs7.tokopedia.net/android/others/saw_order_on_empty.png"
        const val COMMON_ON_EMPTY = "https://ecs7.tokopedia.net/android/others/saw_empty_state.webp"
        const val COMMON_ON_ERROR = "https://ecs7.tokopedia.net/android/others/saw_no_internet.webp"
        const val COMMON_NO_LOGIN = "https://ecs7.tokopedia.net/android/others/saw_no_login.webp"
    }

    object SharedPrefKey {
        const val ORDER_WIDGET_ENABLED = "order_widget_enabled"
        const val CHAT_WIDGET_ENABLED = "chat_widget_enabled"
        const val ORDER_LAST_UPDATED = "saw_order_last_updated"
        const val CHAT_LAST_UPDATED = "saw_chat_last_updated"
        const val LAST_PRUNE_WORK = "saw_last_prune_work"
        const val LAST_SELECTED_ORDER_TYPE = "saw_last_selected_order_type"
    }
}