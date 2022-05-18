package com.tokopedia.product.addedit.preview.data.source.api.response

import com.google.gson.annotations.SerializedName

data class Campaign(
        @SerializedName("campaignID")
        val campaignID: String = "",
        @SerializedName("campaignType")
        val campaignType: Int = 0,
        @SerializedName("campaignTypeName")
        val campaignTypeName: String = "",
        @SerializedName("percentageAmount")
        val percentageAmount: Int = 0,
        @SerializedName("originalPrice")
        val originalPrice: Int = 0,
        @SerializedName("discountedPrice")
        val discountedPrice: Int = 0,
        @SerializedName("originalStock")
        val originalStock: Int = 0,
        @SerializedName("isActive")
        val isActive: Boolean = false
)