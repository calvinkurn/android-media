package com.tokopedia.cart.data.model.response.addcarttowishlist

import com.google.gson.annotations.SerializedName

data class AddCartToWishlistGqlResponse(
        @SerializedName("add_cart_to_wishlist")
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
        @SerializedName("message")
        val message: List<String> = emptyList()
)
