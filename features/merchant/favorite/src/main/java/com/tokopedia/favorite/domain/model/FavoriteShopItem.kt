package com.tokopedia.favorite.domain.model

/**
 * @author Kulomady on 1/23/17.
 */
data class FavoriteShopItem(
        var name: String? = null,
        var iconUri: String? = null,
        var coverUri: String? = null,
        var location: String? = null,
        var isFav: Boolean = false,
        var id: String? = null,
        var adKey: String? = null,
        var adR: String? = null,
        var shopClickUrl: String? = null,
        var badgeUrl: String? = null
)
