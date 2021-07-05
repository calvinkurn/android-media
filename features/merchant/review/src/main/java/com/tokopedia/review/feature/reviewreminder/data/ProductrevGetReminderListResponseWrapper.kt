package com.tokopedia.review.feature.reviewreminder.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevGetReminderListResponseWrapper(
        @SerializedName("productrevGetReminderList")
        @Expose
        val productrevGetReminderList: ProductrevGetReminderList = ProductrevGetReminderList()
)