package com.tokopedia.product.addedit.preview.data.source.api.response

import com.google.gson.annotations.SerializedName

class Preorder(
        @SerializedName("duration")
        val duration: Int = 0,
        @SerializedName("timeUnit")
        val timeUnit: String = "",
        @SerializedName("isActive")
        val isActive: Boolean = false
)