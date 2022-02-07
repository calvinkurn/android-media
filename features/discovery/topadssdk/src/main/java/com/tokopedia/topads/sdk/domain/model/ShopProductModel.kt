package com.tokopedia.topads.sdk.domain.model

import com.tokopedia.kotlin.model.ImpressHolder

private const val KEY_SHOP_ISGOLD = 1

data class ShopProductModel(
        val title: String = "",
        val items: List<ShopProductModelItem> = listOf()
) {
    data class ShopProductModelItem(
            val imageUrl: String = "",
            val shopIcon: String = "",
            val shopBadge: String = "",
            val shopName: String = "",
            val ratingCount: String = "",
            val ratingAverage: String = "",
            val isOfficial: Boolean = false,
            val isPMPro: Boolean = false,
            val goldShop: Int = 0,
            val position: Int,
            val impressHolder: ImpressHolder? = null,

            ) {
        val isGoldShop = goldShop == KEY_SHOP_ISGOLD
    }
}
