package com.tokopedia.home.beranda.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by dhaba
 */
data class SubscriptionsResultStatus(
    @Expose
    @SerializedName("Code")
    val code: String = "",
    @Expose
    @SerializedName("Message")
    val message: List<String> = listOf(),
    @Expose
    @SerializedName("Reason")
    val reason: String = ""
)