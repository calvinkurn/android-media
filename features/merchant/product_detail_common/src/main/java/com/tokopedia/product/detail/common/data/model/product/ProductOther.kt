package com.tokopedia.product.detail.common.data.model.product

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductOther(
        @SerializedName("id")
        @Expose
        val id: Int = -1,

        @SerializedName("name")
        @Expose
        val name: String = "",

        @SerializedName("url")
        @Expose
        val url: String = "",

        @SerializedName("image_url")
        @Expose
        val imageUrl: String = "",

        @SerializedName("image_url_300")
        @Expose
        val imageUrl300: String = "",

        @SerializedName("price_int")
        @Expose
        val price: Int = 0
){
    data class OtherProductResult(
            @SerializedName("count")
            @Expose
            val count: Int = 0,

            @SerializedName("products")
            @Expose
            val products: List<ProductOther> = listOf()
    )

    data class Response(
            @SerializedName("searchProduct")
            @Expose
            val result: OtherProductResult = OtherProductResult()
    )
}