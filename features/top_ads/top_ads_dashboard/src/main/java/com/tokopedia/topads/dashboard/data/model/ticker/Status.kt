package com.tokopedia.topads.dashboard.data.model.ticker

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Status (
    @SerializedName("error_code")
    @Expose
    val errorCode: Int = 0,

    @SerializedName("message")
    @Expose
    val message: String = ""
)
