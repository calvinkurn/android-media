package com.tokopedia.product.addedit.preview.data.source.api.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Campaign(
        @SerializedName("campaignID")
        @Expose
        val campaignID: String = "",
        @SerializedName("campaignType")
        @Expose
        val campaignType: Int = 0,
        @SerializedName("campaignTypeName")
        @Expose
        val campaignTypeName: String = "",
        @SerializedName("percentageAmount")
        @Expose
        val percentageAmount: Int = 0,
        @SerializedName("originalPrice")
        @Expose
        val originalPrice: Int = 0,
        @SerializedName("discountedPrice")
        @Expose
        val discountedPrice: Int = 0,
        @SerializedName("originalStock")
        @Expose
        val originalStock: Int = 0,
        @SerializedName("isActive")
        @Expose
        val isActive: Boolean = false
)