package com.tokopedia.minicart.common.data.response.minicartlistsimplified

import com.google.gson.annotations.SerializedName

data class Product(
        @SerializedName("parent_id")
        val parentId: String = "",
        @SerializedName("product_id")
        val productId: String = "",
        @SerializedName("product_quantity")
        val productQuantity: Int = 0,
        @SerializedName("product_notes")
        val productNotes: String = ""
)