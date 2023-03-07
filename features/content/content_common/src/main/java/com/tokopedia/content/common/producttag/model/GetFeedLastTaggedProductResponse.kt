package com.tokopedia.content.common.producttag.model

import com.google.gson.annotations.SerializedName

/**
 * Created By : Jonathan Darwin on April 26, 2022
 */
data class GetFeedLastTaggedProductResponse(
    @SerializedName("feedXGetLastTaggedProducts")
    val data: Data = Data(),
) {

    data class Data(
        @SerializedName("products")
        val products: List<Product> = emptyList(),

        @SerializedName("nextCursor")
        val nextCursor: String = "",
    )

    data class Product(
        @SerializedName("id")
        val id: String = "",

        @SerializedName("shopID")
        val shopID: String = "",

        @SerializedName("shopName")
        val shopName: String = "",

        @SerializedName("shopBadgeURL")
        val shopBadgeURL: String = "",

        @SerializedName("name")
        val name: String = "",

        @SerializedName("coverURL")
        val coverURL: String = "",

        @SerializedName("webLink")
        val webLink: String = "",

        @SerializedName("appLink")
        val appLink: String = "",

        @SerializedName("star")
        val star: Double = 0.0,

        @SerializedName("price")
        val price: Double = 0.0,

        @SerializedName("priceFmt")
        val priceFmt: String = "",

        @SerializedName("isDiscount")
        val isDiscount: Boolean = false,

        @SerializedName("discount")
        val discount: Double = 0.0,

        @SerializedName("discountFmt")
        val discountFmt: String = "",

        @SerializedName("priceOriginal")
        val priceOriginal: Double = 0.0,

        @SerializedName("priceOriginalFmt")
        val priceOriginalFmt: String = "",

        @SerializedName("priceDiscount")
        val priceDiscount: Double = 0.0,

        @SerializedName("priceDiscountFmt")
        val priceDiscountFmt: String = "",

        @SerializedName("totalSold")
        val totalSold: Int = 0,

        @SerializedName("totalSoldFmt")
        val totalSoldFmt: String = "",

        @SerializedName("isBebasOngkir")
        val isBebasOngkir: Boolean = false,

        @SerializedName("bebasOngkirStatus")
        val bebasOngkirStatus: String = "",

        @SerializedName("bebasOngkirURL")
        val bebasOngkirURL: String = "",
    )
}
