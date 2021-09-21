package com.tokopedia.review.feature.reviewreminder.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevGetReminderCounterResponseWrapper(
        @SerializedName("productrevGetReminderCounter")
        @Expose
        val productrevGetReminderCounter: ProductrevGetReminderCounter = ProductrevGetReminderCounter()
)