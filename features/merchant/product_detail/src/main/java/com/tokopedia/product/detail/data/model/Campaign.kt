package com.tokopedia.product.detail.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Campaign(
        @SerializedName("endDate")
        @Expose
        val endDate: String = "",

        @SerializedName("campaignID")
        @Expose
        val id: Int = 0,

        @SerializedName("isActive")
        @Expose
        val isActive: Boolean = false,

        @SerializedName("isAppsOnly")
        @Expose
        val isAppsOnly: Boolean = false,

        @SerializedName("campaignTypeName")
        @Expose
        val name: String = "",

        @SerializedName("originalPrice")
        @Expose
        val originalPrice: Int = 0,

        @SerializedName("percentageAmount")
        @Expose
        val percentage: Int = 0,

        @SerializedName("startDate")
        @Expose
        val startDate: String = "",

        @SerializedName("campaignType")
        @Expose
        val type: Int = 0
)