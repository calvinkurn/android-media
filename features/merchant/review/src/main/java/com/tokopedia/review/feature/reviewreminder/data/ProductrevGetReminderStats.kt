package com.tokopedia.review.feature.reviewreminder.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevGetReminderStats(
        @SerializedName("timeRange")
        @Expose
        val timeRange: String = "",
        @SerializedName("totalReminderStats")
        @Expose
        val totalReminderStats: Int = 0,
        @SerializedName("lastReminderTime")
        @Expose
        val lastReminderTime: String = "",
        @SerializedName("lastReminderStats")
        @Expose
        val lastReminderStats: String = "",
        @SerializedName("reviewPercentage")
        @Expose
        val reviewPercentage: String = ""
)