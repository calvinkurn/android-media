package com.tokopedia.officialstore.common

import com.tokopedia.officialstore.DynamicChannelIdentifiers
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Channel

interface OfficialStoreConstant {
    interface TopAdsComponent {
        companion object {
            const val OS_RECOM_TOP_ADS = "official_store_product_recommendation_topads"
            const val OS_MIX_LEFT_TOP_ADS = "official_store_mix_left_topads"
            const val OS_MIX_TOP_TOP_ADS = "official_store_mix_top_topads"

            fun getProductCardTopAdsComponentName(channel: Channel): String {
                return when (channel.layout) {
                    DynamicChannelIdentifiers.LAYOUT_MIX_TOP -> OS_MIX_TOP_TOP_ADS
                    DynamicChannelIdentifiers.LAYOUT_MIX_LEFT -> OS_MIX_LEFT_TOP_ADS
                    else -> ""
                }
            }
        }
    }
}