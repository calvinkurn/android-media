package com.tokopedia.product.detail.common.data.model.pdplayout

import com.google.gson.annotations.SerializedName

data class CustomInfoLabelData(
        @SerializedName("value")
        val value: String = "",
        @SerializedName("color")
        val color: String = ""
)
