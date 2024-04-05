@file:SuppressLint("Invalid Data Type")

package com.tokopedia.home.beranda.data.model

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.tokopedia.home_component.model.AtfContent

data class TargetedTicker(
    @SerializedName("GetTargetedTicker")
    val targetedTicker: GetTargetedTicker = GetTargetedTicker()
)

data class GetTargetedTicker(
    @SerializedName("List")
    val tickers: List<GetTargetedTickerItem> = emptyList()
) : AtfContent

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
) {

    fun getTickerType() =
        when(type) {
            TYPE_INFO -> Type.Info
            TYPE_WARNING -> Type.Warning
            else -> Type.Danger
        }


    sealed class Type {
        object Info : Type()
        object Warning : Type()
        object Danger : Type()
    }

    companion object {
        private const val TYPE_INFO = "info"
        private const val TYPE_WARNING = "warning"
    }
}

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
