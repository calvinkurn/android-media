package com.tokopedia.gm.subscribe.membership.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Subscription(
        @SerializedName("subscription_type")
        @Expose
        var subscription_type: Int = 0,
        @SerializedName("name")
        @Expose
        var name: String = "",
        @SerializedName("notes")
        @Expose
        var notes: String = "",
        @SerializedName("days_limit")
        @Expose
        var days_limit: Int = 0,
        @SerializedName("price")
        @Expose
        var price:  Int = 0,
        @SerializedName("price_fmt")
        @Expose
        var price_fmt: String = "",
        @SerializedName("product_time_range_fmt")
        @Expose
        var product_time_range_fmt: String = ""
)