package com.tokopedia.home_wishlist.model.entity

data class WishlistEntityData(
        val hasNextPage: Boolean = false,
        val items: List<WishlistItem> = listOf(),
        val totalData: Integer = Integer(0),
        val isSuccess: Boolean = true,
        val errorMessage: String = ""
)