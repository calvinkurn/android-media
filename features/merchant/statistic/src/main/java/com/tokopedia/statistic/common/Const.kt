package com.tokopedia.statistic.common

/**
 * Created By @ilhamsuaib on 16/09/20
 */

object Const {

    const val DAY_0 = 0
    const val DAY_1 = 1
    const val DAYS_6 = 6
    const val DAYS_7 = 7
    const val DAYS_29 = 29
    const val DAYS_30 = 30
    const val DAYS_91 = 91
    const val DAYS_365 = 365

    const val WHITE_LIST_KEY_OPERATIONAL_STATISTIC = "statistic-operational"

    const val SHOW_PRODUCT_INSIGHT_COACH_MARK_KEY = "show_product_insight_coachmark"
    const val HAS_SHOWN_OPERATIONAL_INSIGHT_COACH_MARK_KEY = "has_shown_operational_insight_coac_hmark"

    object PageSource {
        const val SHOP_INSIGHT = "shop-insight"
        const val BUYER_INSIGHT = "buyer-insight"
        const val PRODUCT_INSIGHT = "product-landing-insight"
        const val OPERATIONAL_INSIGHT = "operational-insight"
    }

    object TickerPageName {
        const val SHOP_INSIGHT = "seller-statistic"
        const val BUYER_INSIGHT = "wawasan-pembeli"
        const val PRODUCT_INSIGHT = "seller-statistic#wawasan-produk"
        const val OPERATIONAL_INSIGHT = "seller-statistic#wawasan-operasional"
    }

    object BottomSheet {
        const val TAG_MONTH_PICKER = "MonthPickerBottomSheet"
    }

    object Url {
        const val SHOP_LEARN_MORE = "https://www.tokopedia.com/help/article/apa-itu-statistik-toko?source=sapp-wawasan-toko"
        const val SHOP_GIVE_SUGGESTIONS = "https://docs.google.com/forms/d/1t-KeapZJwOeYOBnbXDEmzRJiUqMBicE9cQIauc40qMU"
        const val BUYER_LEARN_MORE = "https://www.tokopedia.com/help/article/apa-itu-wawasan-pembeli?source=sapp-wawasan-pembeli"
        const val BUYER_GIVE_SUGGESTIONS = "https://docs.google.com/forms/d/1g16aH6t8n6k-jMqOZpDK4QVgaxIXNodclNpwhS9KdkU/edit"
        const val PRODUCT_LEARN_MORE = "https://www.tokopedia.com/help/article/apa-itu-wawasan-produk?source=sapp-wawasan-produk"
        const val PRODUCT_GIVE_SUGGESTIONS = "https://docs.google.com/forms/d/1CTCW5iOwIxQcmq4osZS284_E1IsPGPYcwe44yrEK_Cc/edit"
        const val OPERATIONAL_GIVE_SUGGESTIONS = "https://docs.google.com/forms/d/1i-bm4ceByIVkYegd52WiSTKWkJ0VQsv4dHWYWMhWH4I"
        const val OPERATIONAL_LEARN_MORE = "https://www.tokopedia.com/help/article/apa-itu-wawasan-operasional?source=sapp-wawasan-operasional"
    }
}