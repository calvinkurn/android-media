package com.tokopedia.buyerorder.detail.data

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class ConditionalInfo (
    @SerializedName("text")
    @Expose
    val text: String = "",

    @SerializedName("color")
    @Expose
    val color: Color = Color()
)

