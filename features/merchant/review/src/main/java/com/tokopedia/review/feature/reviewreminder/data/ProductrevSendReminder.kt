package com.tokopedia.review.feature.reviewreminder.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevSendReminder(
        @SerializedName("success")
        @Expose
        val success: Boolean = false
)