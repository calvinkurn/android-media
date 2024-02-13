package com.tokopedia.home.beranda.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Devara on 02/04/20
 */
data class LabelGroup(
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("position")
        @Expose
        val position: String = "",
        @SerializedName("type")
        @Expose
        val type: String = "",
        @SerializedName("url")
        @Expose
        val imageUrl: String = "",
        @SerializedName("styles")
        @Expose
        val styles: List<Style> = listOf(),
) {
    data class Style(
        @SerializedName("key")
        @Expose
        val key: String = "",
        @SerializedName("value")
        @Expose
        val value: String = "",
    )
}
