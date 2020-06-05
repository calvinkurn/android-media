package com.tokopedia.product.addedit.preview.data.source.api.response

import com.google.gson.annotations.SerializedName

data class Position(
        @SerializedName("position")
        val position: String,
        @SerializedName("isSwap")
        val isSwap: Boolean
)