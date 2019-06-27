package com.tokopedia.notifications.model

import com.google.gson.annotations.SerializedName

data class ProductInfo(

        @SerializedName("productTitle")
        var productTitle: String,

        @SerializedName("productImg")
        var productImage: String,

        @SerializedName("actualPrice")
        var productActualPrice: String,

        @SerializedName("currentPrice")
        var productCurrentPrice: String,

        @SerializedName("droppedPercent")
        var productPriceDroppedPercentage: String,

        @SerializedName("message")
        var productMessage: String,

        @SerializedName("buttonTxt")
        var productButtonMessage: String,

        @SerializedName("appLink")
        var appLink: String
)
