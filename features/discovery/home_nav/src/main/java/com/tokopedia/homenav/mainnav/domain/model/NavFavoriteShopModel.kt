package com.tokopedia.homenav.mainnav.domain.model

data class NavFavoriteShopModel(
    val id: String = "",
    val name: String = "",
    val imageUrl: String = "",
    val location: String = "",
    val badgeImageUrl: String = "",
    var fullWidth: Boolean = false
)
