package com.tokopedia.feedcomponent.data.feedrevamp


import com.google.gson.annotations.SerializedName

data class FeedXProduct(
        @SerializedName("id")
        var id: String,
        @SerializedName("name")
        var name: String,
        @SerializedName("coverURL")
        var coverURL: String,
        @SerializedName("appLink")
        var appLink: String,
        @SerializedName("webLink")
        var webLink: String,
        @SerializedName("star")
        var star: Float,
        @SerializedName("price")
        var price: Float,
        @SerializedName("priceFmt")
        var priceFmt: String,
        @SerializedName("priceStruck")
        var priceStruck: Float,
        @SerializedName("priceStruckFmt")
        var priceStruckFmt: String,
        @SerializedName("discount")
        var discount: Float,
        @SerializedName("discountFmt")
        var discountFmt: String,
        @SerializedName("badgeImageURLs")
        var badgeImageURLs: List<String>,
        @SerializedName("badgeTexts")
        var badgeTexts: List<String>,
        @SerializedName("mods")
        var mods: List<String>
)