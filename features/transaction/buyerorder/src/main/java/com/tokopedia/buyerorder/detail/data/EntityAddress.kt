package com.tokopedia.buyerorder.detail.data

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class EntityAddress(
    @SerializedName("email")
    @Expose
    val email: String = "",

    @SerializedName("name")
    @Expose
    val name: String = ""
)