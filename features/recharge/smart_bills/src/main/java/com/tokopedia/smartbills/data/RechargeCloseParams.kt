package com.tokopedia.smartbills.data

import com.google.gson.annotations.SerializedName

data class RechargeCloseParams (
    @SerializedName("UUID")
    val uUID : String = "",
    @SerializedName("ContentID")
    val contentID : String = "",
)