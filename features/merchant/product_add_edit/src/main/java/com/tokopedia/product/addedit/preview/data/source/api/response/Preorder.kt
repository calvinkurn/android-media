package com.tokopedia.product.addedit.preview.data.source.api.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Preorder(
        @SerializedName("duration")
        @Expose
        val duration: Int = 0,
        @SerializedName("timeUnit")
        @Expose
        val timeUnit: String = "",
        @SerializedName("isActive")
        @Expose
        val isActive: Boolean = false
)