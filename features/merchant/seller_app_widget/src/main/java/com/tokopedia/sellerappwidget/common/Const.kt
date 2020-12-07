package com.tokopedia.sellerappwidget.common

/**
 * Created By @ilhamsuaib on 17/11/20
 */

object Const {

    object OrderStatusId {
        const val NEW_ORDER = 220
        const val READY_TO_SHIP = 400
    }

    object Extra {
        const val BUNDLE = "extra_bundle"
        const val ORDER_ITEM = "order"
        const val ORDER_ITEMS = "orders"
        const val ORDER_STATUS_ID = "order_status_id"
        const val CHAT_ITEM = "chat"
        const val CHAT_ITEMS = "chats"
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
    }

    object SharedPrefKey {
        const val ORDER_LAST_UPDATED = "order_last_updated"
        const val CHAT_LAST_UPDATED = "chat_last_updated"
    }
}