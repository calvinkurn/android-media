package com.tokopedia.buyerorder.detail.data

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import java.io.Serializable

data class Body(
    @SerializedName("body")
    @Expose
    val body: String = "",

    @SerializedName("appURL")
    @Expose
    val appURL: String = "",

    @SerializedName("method")
    @Expose
    val method: String = ""
) : Serializable