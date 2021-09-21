package com.tokopedia.review.feature.reviewreminder.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevGetReminderTemplate(
        @SerializedName("template")
        @Expose
        val template: String = ""
)