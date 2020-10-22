package com.tokopedia.favorite.domain.model

/**
 * Created by erry on 31/01/17.
 */
data class FavShop(
        var isValid: Boolean = false,
        var message: String? = null,
        var favoriteShopItem: FavoriteShopItem? = null
)
