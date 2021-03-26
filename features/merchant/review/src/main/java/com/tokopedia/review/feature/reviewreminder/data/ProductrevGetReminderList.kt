package com.tokopedia.review.feature.reviewreminder.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevGetReminderList(
        @SerializedName("list")
        @Expose
        val list: List<ProductrevGetReminderData> = emptyList(),
        @SerializedName("lastProductID")
        @Expose
        val lastProductID: String = "",
        @SerializedName("hasNext")
        @Expose
        val hasNext: Boolean = false
)