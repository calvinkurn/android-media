package com.tokopedia.buyerorder.detail.data

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

/**
 * Created by baghira on 11/05/18.
 */
data class Pricing(
    @SerializedName("label")
    @Expose
    val label: String = "",

    @SerializedName("value")
    @Expose
    val value: String = ""
)