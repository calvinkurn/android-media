package com.tokopedia.topads.sdk.v2.shopadslayout5.uimodel

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.topads.sdk.common.constants.TopAdsConstants

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
        val location: String = "",
        val isOfficial: Boolean = false,
        val isPMPro: Boolean = false,
        val goldShop: Int = 0,
        val position: Int,
        var isFollowed: Boolean = false,
        val layoutType: Int? = TopAdsConstants.LAYOUT_5,
        val shopId: String = "",
        val impressHolder: ImpressHolder? = null

    ) {
        val isGoldShop = goldShop == KEY_SHOP_ISGOLD
    }
}
