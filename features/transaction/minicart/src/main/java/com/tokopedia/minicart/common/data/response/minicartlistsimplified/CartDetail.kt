package com.tokopedia.minicart.common.data.response.minicartlistsimplified

import com.google.gson.annotations.SerializedName

data class CartDetail(
        @SerializedName("cart_id")
        val cartId: String = "",
        @SerializedName("product")
        val product: Product = Product()
)