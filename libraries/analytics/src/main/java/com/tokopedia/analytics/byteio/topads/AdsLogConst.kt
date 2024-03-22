package com.tokopedia.analytics.byteio.topads

/**
 * @see https://bytedance.sg.larkoffice.com/sheets/WW7UsCotphSRlyt3Z6VlRqL2gof?sheet=EaMOLF
 */
object AdsLogConst {

    const val EVENT = "event"
    const val RIT = "rit"
    const val TAG = "tag"
    const val REFER = "refer"
    const val PARAMS = "params"
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
        const val SESSION_INFO = "session_info"
        const val SIZE_PERCENT = "size_percent"
        const val CHANNEL = "channel"
        const val ENTER_FROM = "enter_from"
        const val TIME_INTERVAL_BETWEEN_CURRENT_N_CLICK = "time_interval_between_current_and_click"
    }
    object AdCardStyle {
        const val PRODUCT_CARD = "product card"
        const val LIVE_CARD = "live card"
        const val VIDEO_CARD = "video card"
    }
    object Channel {
        const val PRODUCT_SEARCH = "product search"
        const val PDP_SEARCH = "pdp search"
        const val STORE_SEARCH = "store search"
    }
    object EnterFrom {
        const val MALL = "mall"
        const val OTHER = "others"
    }

    object Rit {
        const val SEARCH_IN_PDP = "583733534"
        const val RECOM_HOMEPAGE_BEST_SELLER_PRODUCT = "583733151"
        const val RECOM_HOMEPAGE_TARGETING_CONTEXT = "583733152"
        const val RECOM_HOMEPAGE_FOR_YOU = "583733153"
        const val MISSION_WIDGET_ALL_ADS = "583733154"
        const val MISSION_WIDGET_BEST_SELLER = "583733155"
        const val MISSION_WIDGET_SIMILIAR_PRODUCT = "583733156"
        const val MISSION_WIDGET_RETARGETING_CONTEXT = "583733157"

    }
}
