package com.tokopedia.product.detail.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Campaign(
        @SerializedName("end_date")
        @Expose
        val endDate: String = "",

        @SerializedName("id")
        @Expose
        val id: Int = 0,

        @SerializedName("is_active")
        @Expose
        val isActive: Boolean = false,

        @SerializedName("is_apps_only")
        @Expose
        val isAppsOnly: Boolean = false,

        @SerializedName("name")
        @Expose
        val name: String = "",

        @SerializedName("original_price")
        @Expose
        val originalPrice: Int = 0,

        @SerializedName("percentage")
        @Expose
        val percentage: Int = 0,

        @SerializedName("start_date")
        @Expose
        val startDate: String = "",

        @SerializedName("type")
        @Expose
        val type: Int = 0
)