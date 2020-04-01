package com.tokopedia.similarsearch.getsimilarproducts.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

internal data class Data(
        @SerializedName("products")
        @Expose
        val productList: List<Product> = listOf(),

        @SerializedName("originalProduct")
        @Expose
        val originalProduct: Product = Product()
)