package com.tokopedia.buyerorder.detail.data

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class EntityPackage (
    @SerializedName("address")
    @Expose
    val address: String = "",

    @SerializedName("display_name")
    @Expose
    val displayName: String = ""
)