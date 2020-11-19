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
        const val ORDER_ITEM = "item"
        const val ORDER_ITEMS = "items"
        const val ORDER_STATUS_ID = "order_status_id"
    }

    object Action {
        const val REFRESH = "action_refresh"
        const val ITEM_CLICK = "action_item_click"
        const val SWITCH_ORDER = "action_switch_order"
    }

    object Method {
        const val SET_VISIBILITY = "setVisibility"
        const val SET_IMAGE_RESOURCE = "setImageResource"
    }
}