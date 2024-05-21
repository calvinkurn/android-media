package com.tokopedia.analytics.byteio.topads

/**
 * @see https://bytedance.sg.larkoffice.com/sheets/WW7UsCotphSRlyt3Z6VlRqL2gof?sheet=EaMOLF
 */
object AdsLogConst {

    const val EVENT = "event"
    const val TAG = "tag"
    const val REFER = "refer"

    const val EVENT_V3 = "event_v3"
    object Event {
        const val SHOW = "show"
        const val SHOW_OVER = "show_over"
        const val REALTIME_CLICK = "realtime_click"
    }
    object Tag {
        const val TOKO_MALL_AD = "toko_mall_ad"
        const val TOKO_RESULT_MALL_AD = "toko_result_mall_ad"
    }
    object Refer {
        const val COVER = "cover"
        const val AREA = "area"
        const val SELLER_NAME = "seller_name"
    }
    object Param {
        const val SYSTEM_START_TIMESTAMP = "system_start_timestamp"
        const val PRODUCT_ID = "product_id"
        const val MALL_CARD_TYPE = "mall_card_type"

        const val SIZE_PERCENT = "size_percent"
        const val CHANNEL = "channel"
        const val PRODUCT_NAME = "product_name"
        const val ENTER_FROM = "enter_from"

        const val VALUE = "value"
        const val AD_EXTRA_DATA = "ad_extra_data"
        const val LOG_EXTRA = "log_extra"
        const val CATEGORY = "category"
        const val GROUP_ID = "group_id"
        const val IS_AD_EVENT = "is_ad_event"
        const val NT = "nt"
    }
    object AdCardStyle {
        const val PRODUCT_CARD = "product card"
    }
    object Channel {
        const val PRODUCT_SEARCH = "product search"
        const val PDP_SEARCH = "pdp search"
        const val STORE_SEARCH = "store search"
        const val DISCOVERY_SEARCH = "discovery search"
        const val FIND_SEARCH = "find search"
    }
    object EnterFrom {
        const val MALL = "mall"
        const val OTHER = "others"
    }
}
