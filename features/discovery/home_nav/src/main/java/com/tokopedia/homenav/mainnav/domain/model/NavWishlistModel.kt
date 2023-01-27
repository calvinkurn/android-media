package com.tokopedia.homenav.mainnav.domain.model

data class NavWishlistModel(
    val id: String = "",
    val name: String = "",
    val totalItem: Int = 0,
    val itemText: String = "",
    val images: List<String> = listOf(),
    var fullWidth: Boolean = false
)
