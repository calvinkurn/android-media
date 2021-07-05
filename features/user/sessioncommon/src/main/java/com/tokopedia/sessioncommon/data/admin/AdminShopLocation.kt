package com.tokopedia.sessioncommon.data.admin

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AdminShopLocation(
    @Expose
    @SerializedName("location_id")
    val id: String = ""
)