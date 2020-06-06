package com.tokopedia.shop_showcase.shop_showcase_management.data.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopProductResponse(
        @SerializedName("name")
        @Expose
        val name: String = ""
){
    data class Response(
            @SerializedName("GetShopProduct")
            @Expose
            val getShopProduct: GetShopProduct = GetShopProduct())

    data class GetShopProduct(
            @SerializedName("errors")
            @Expose
            val errors: String = "",

            @SerializedName("status")
            @Expose
            val status: String = "",

            @SerializedName("totalData")
            @Expose
            val totalData: Int = 0
    )
}