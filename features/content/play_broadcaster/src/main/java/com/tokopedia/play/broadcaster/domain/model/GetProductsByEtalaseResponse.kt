package com.tokopedia.play.broadcaster.domain.model

import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 02/06/20
 */
data class GetProductsByEtalaseResponse(
        @SerializedName("GetShopProduct")
        val getShopProduct: GetShopProductData
) {

        data class GetShopProductData(

                @SerializedName("status")
                val status: String,

                @SerializedName("errors")
                val errors: String,

                @SerializedName("data")
                val data: List<ShopProductListDetail>,

                @SerializedName("totalData")
                val totalData: Int
        )

        data class ShopProductListDetail(

                @SerializedName("product_id")
                val productId: String,

                @SerializedName("name")
                val name: String,

                @SerializedName("product_url")
                val productUrl: String,

                @SerializedName("stock")
                val stock: Int,

                @SerializedName("primary_image")
                val primaryImage: PrimaryImage
        )

        data class PrimaryImage(

                @SerializedName("original")
                val original: String,

                @SerializedName("thumbnail")
                val thumbnail: String,

                @SerializedName("resize300")
                val resize300: String
        )
}