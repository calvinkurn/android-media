package com.tokopedia.buyerorder.detail.data

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

/**
 * Created by baghira on 10/05/18.
 */
data class Status(
    @SerializedName("statusText")
    @Expose
    val statusText: String = "",

    @SerializedName("status")
    @Expose
    val status: String = "",

    @SerializedName("statusLabel")
    @Expose
    val statusLabel: String = "",

    @SerializedName("textColor")
    @Expose
    val textColor: String = "",

    @SerializedName("backgroundColor")
    @Expose
    val backgroundColor: String = ""
)