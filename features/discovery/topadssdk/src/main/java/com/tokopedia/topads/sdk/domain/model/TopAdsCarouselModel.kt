package com.tokopedia.topads.sdk.domain.model

import com.tokopedia.kotlin.model.ImpressHolder

const val KEY_SHOP_IS_GOLD = 1

data class TopAdsCarouselModel(
        val title: String = "",
        val items: List<TopAdsCarouselItem> = listOf()
) {
    data class TopAdsCarouselItem(
            val imageUrlOne: String = "",
            val imageUrlTwo: String = "",
            val brandIcon: String = "",
            val brandBadge: String = "",
            val brandName: String = "",
            val isOfficial: Boolean = false,
            val isPMPro: Boolean = false,
            val goldShop: Int = 0,
            val position: Int,
            val impressHolder: ImpressHolder? = null,

            ) {
        val isGoldShop = goldShop == KEY_SHOP_IS_GOLD
    }
}
