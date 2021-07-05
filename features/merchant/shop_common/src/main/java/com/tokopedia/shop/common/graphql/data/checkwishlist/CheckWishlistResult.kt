package com.tokopedia.shop.common.graphql.data.checkwishlist

import com.google.gson.annotations.SerializedName

data class CheckWishlistResult(
        @SerializedName("product_id")
        val productId: String = "",
        @SerializedName("is_wishlist")
        val isWishlist: Boolean = false
){
    data class Response(
            @SerializedName("checkWishlistV2")
            val checkWishlist: List<CheckWishlistResult> = listOf()

    )
}