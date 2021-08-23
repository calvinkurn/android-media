package com.tokopedia.attachproduct.data.model

import com.google.gson.annotations.SerializedName

data class NewDataShopResponse (
    @SerializedName("name")
    val shopName: String = ""
)