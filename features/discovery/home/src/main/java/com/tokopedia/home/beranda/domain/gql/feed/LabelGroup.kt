package com.tokopedia.home.beranda.domain.gql.feed

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Lukas on 09/09/19
 */
class LabelGroup (
        @SerializedName("type")
        val type: String = "",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("position")
        val position: String = "",
        @SerializedName("url")
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
