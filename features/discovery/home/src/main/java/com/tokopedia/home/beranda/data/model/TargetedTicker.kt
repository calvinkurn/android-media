@file:SuppressLint("Invalid Data Type")

package com.tokopedia.home.beranda.data.model

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class TargetedTicker(
    @SerializedName("GetTargetedTicker")
    val targetedTicker: GetTargetedTicker = GetTargetedTicker()
)

data class GetTargetedTicker(
    @SerializedName("List")
    val tickers: List<GetTargetedTickerItem> = emptyList()
)

data class GetTargetedTickerItem(
    @SerializedName("ID")
    val id: Int = 0,

    @SerializedName("Title")
    val title: String = "",

    @SerializedName("Content")
    val content: String = "",

    @SerializedName("Action")
    val action: GetTargetedTickerAction = GetTargetedTickerAction(),

    @SerializedName("Type")
    val type: String = "",

    @SerializedName("Priority")
    val priority: Int = 0,

    @SerializedName("Metadata")
    val metadata: List<GetTargetedTickerMetadata> = emptyList(),
)

data class GetTargetedTickerAction(
    @SerializedName("Label")
    val label: String = "",

    @SerializedName("Type")
    val type: String = "",

    @SerializedName("AppURL")
    val appLink: String = "",

    @SerializedName("WebURL")
    val url: String = "",
)

data class GetTargetedTickerMetadata(
    @SerializedName("Type")
    val type: String = "",

    @SerializedName("Values")
    val values: List<String> = emptyList(),
)
