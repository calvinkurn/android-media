package com.tokopedia.feedcomponent.data.feedrevamp


import com.google.gson.annotations.SerializedName

data class FeedXProduct(
        @SerializedName("appLink")
        var appLink: String = "",
        @SerializedName("bebasOngkirStatus")
        var bebasOngkirStatus: String = "",
        @SerializedName("bebasOngkirURL")
        var bebasOngkirURL: String = "",
        @SerializedName("coverURL")
        var coverURL: String = "",
        @SerializedName("discount")
        var discount: Int = 0,
        @SerializedName("discountFmt")
        var discountFmt: String = "",
        @SerializedName("id")
        var id: String = "",
        @SerializedName("isBebasOngkir")
        var isBebasOngkir: Boolean = false,
        @SerializedName("isDiscount")
        var isDiscount: Boolean = false,
        @SerializedName("mods")
        var mods: List<Any> = listOf(),
        @SerializedName("name")
        var name: String = "",
        @SerializedName("price")
        var price: Int = 0,
        @SerializedName("priceDiscount")
        var priceDiscount: Int = 0,
        @SerializedName("priceDiscountFmt")
        var priceDiscountFmt: String = "",
        @SerializedName("priceFmt")
        var priceFmt: String = "",
        @SerializedName("priceOriginal")
        var priceOriginal: Int = 0,
        @SerializedName("priceOriginalFmt")
        var priceOriginalFmt: String = "",
        @SerializedName("star")
        var star: Int = 0,
        @SerializedName("totalSold")
        var totalSold: Int = 0,
        @SerializedName("webLink")
        var webLink: String = ""
)