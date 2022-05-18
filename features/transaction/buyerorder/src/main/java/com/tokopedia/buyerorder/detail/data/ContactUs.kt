package com.tokopedia.buyerorder.detail.data

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

/**
 * Created by baghira on 11/05/18.
 */

data class ContactUs(
    @SerializedName("helpText")
    @Expose
    val helpText: String = "",

    @SerializedName("helpUrl")
    @Expose
    val helpUrl: String = ""
)