package com.tokopedia.review.feature.reviewreminder.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevGetReminderStatsResponseWrapper(
        @SerializedName("productrevGetReminderStats")
        @Expose
        val productrevGetReminderStats: ProductrevGetReminderStats = ProductrevGetReminderStats()
)