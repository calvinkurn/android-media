package com.tokopedia.play.broadcaster.domain.model.product

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

/**
 * Created by kenny.hadisaputra on 28/01/22
 */
@SuppressLint("Invalid Data Type", "ResponseFieldAnnotation")
data class GetShopProductsResponse(
    @SerializedName("GetShopProduct")
    val response: GetShopProduct = GetShopProduct(),
) {

    data class GetShopProduct(
        @SerializedName("data")
        val data: List<Data> = emptyList(),

        @SerializedName("links")
        val links: Links = Links(),
    )

    data class Data(
        @SerializedName("product_id")
        val productId: String = "",

        @SerializedName("name")
        val name: String = "",

        @SerializedName("stock")
        val stock: Int = 0,

        @SerializedName("price")
        val price: Price = Price(),

        @SerializedName("campaign")
        val campaign: Campaign = Campaign(),

        @SerializedName("primary_image")
        val primaryImage: PrimaryImage = PrimaryImage(),
    )

    data class Price(
        @SerializedName("text_idr")
        val textIdr: String = "",
    )

    data class Campaign(
        @SerializedName("discounted_percentage")
        val discountedPercentage: String = "0",

        @SerializedName("original_price_fmt")
        val originalPriceFmt: String = "",

        @SerializedName("discounted_price_fmt")
        val discountedPriceFmt: String = "",
    )

    data class PrimaryImage(
        @SerializedName("thumbnail")
        val thumbnail: String = "",
    )

    data class Links(
        @SerializedName("next")
        val next: String = "",
    )
}