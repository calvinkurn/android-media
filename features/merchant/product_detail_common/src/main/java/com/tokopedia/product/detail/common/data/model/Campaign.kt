package com.tokopedia.product.detail.common.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Campaign(
        @SerializedName("appLinks")
        @Expose
        val applinks: String = "",

        @SerializedName("endDate")
        @Expose
        val endDate: String = "",

        @SerializedName("endDateUnix")
        @Expose
        val endDateUnix: Int = 0,

        @SerializedName("campaignID")
        @Expose
        val id: String = "",

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
        val originalPrice: Float = 0f,

        @SerializedName("discountedPrice")
        @Expose
        val discountedPrice: Float = 0f,

        @SerializedName("percentageAmount")
        @Expose
        val percentage: Float = 0f,

        @SerializedName("startDate")
        @Expose
        val startDate: String = "",

        @SerializedName("stock")
        @Expose
        val stock: Int = 0,

        @SerializedName("campaignType")
        @Expose
        val type: Int = 0
)