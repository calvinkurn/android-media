package com.tokopedia.home.beranda.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by dhaba
 */
data class GetHomeBalanceItem(
    @Expose
    @SerializedName("title")
    val title: String = "",
    @Expose
    @SerializedName("type")
    val type: String = "",
    @Expose
    @SerializedName("data")
    val data: String = ""
)