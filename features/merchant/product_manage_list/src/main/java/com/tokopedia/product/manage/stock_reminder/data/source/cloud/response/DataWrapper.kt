package com.tokopedia.product.manage.stock_reminder.data.source.cloud.response

import com.google.gson.annotations.SerializedName

data class DataWrapper(
        @SerializedName("data")
        val data: List<Product>
)