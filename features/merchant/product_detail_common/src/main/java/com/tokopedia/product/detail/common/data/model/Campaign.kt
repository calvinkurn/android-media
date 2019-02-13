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

        @SerializedName("stock")
        @Expose
        val stock: Int = 0,

        @SerializedName("campaignType")
        @Expose
        val type: Int = 0
)