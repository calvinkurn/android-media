package com.tokopedia.review.feature.reviewreminder.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevSendReminderResponseWrapper(
        @SerializedName("productrevSendReminder")
        @Expose
        val productrevSendReminder: ProductrevSendReminder = ProductrevSendReminder()
)