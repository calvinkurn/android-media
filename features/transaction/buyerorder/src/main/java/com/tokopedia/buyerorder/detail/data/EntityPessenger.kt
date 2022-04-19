package com.tokopedia.buyerorder.detail.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class EntityPessenger(
    @SerializedName("title")
    @Expose
    val title: String = "",

    @SerializedName("value")
    @Expose
    val value: String = ""
)