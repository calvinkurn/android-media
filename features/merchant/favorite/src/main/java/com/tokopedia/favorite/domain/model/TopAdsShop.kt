package com.tokopedia.favorite.domain.model

/**
 * @author Kulomady on 1/19/17.
 */
data class TopAdsShop(
        var message: String? = null,
        var isDataValid: Boolean = false,
        var isNetworkError: Boolean = false,
        var topAdsShopItemList: List<TopAdsShopItem>? = null
)
