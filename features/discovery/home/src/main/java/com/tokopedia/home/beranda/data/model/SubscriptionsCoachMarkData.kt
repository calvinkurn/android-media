package com.tokopedia.home.beranda.data.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class SubscriptionsCoachMarkData(
    @SerializedName("Content")
    @Expose
    val content: String = "",
    @SerializedName("IsShown")
    @Expose
    val isShown: Boolean = false,
    @SerializedName("Title")
    @Expose
    val title: String = ""
)