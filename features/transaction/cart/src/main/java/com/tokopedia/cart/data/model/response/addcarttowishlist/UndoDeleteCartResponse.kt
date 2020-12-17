package com.tokopedia.cart.data.model.response.addcarttowishlist

import com.google.gson.annotations.SerializedName

data class AddCartToWishlistGqlResponse(
        @SerializedName("add_to_wishlist_v2")
        val addCartToWishlistDataResponse: AddCartToWishlistDataResponse = AddCartToWishlistDataResponse()
)

data class AddCartToWishlistDataResponse(
        @SerializedName("status")
        val status: String = "",
        @SerializedName("error_message")
        val errorMessage: List<String> = emptyList(),
        @SerializedName("data")
        val data: Data = Data()
)

data class Data(
        @SerializedName("success")
        val success: Int = 0,
        @SerializedName("messages")
        val message: List<String> = emptyList()
)
