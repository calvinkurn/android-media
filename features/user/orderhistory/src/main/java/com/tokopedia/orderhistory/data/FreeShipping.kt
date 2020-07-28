package com.tokopedia.orderhistory.data

import com.google.gson.annotations.SerializedName

data class FreeShipping(
        @SerializedName("imageUrl")
        val imageUrl: String = "",
        @SerializedName("isActive")
        val isActive: Boolean = false
)