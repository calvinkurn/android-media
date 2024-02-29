package com.tokopedia.discovery2.data.productcarditem

import com.google.gson.annotations.SerializedName

data class LabelsGroup(
    @SerializedName("position")
    var position: String = "",

    @SerializedName("title")
    val title: String = "",

    @SerializedName("type")
    val type: String = "",

    @SerializedName("url")
    val url: String = "",

    @SerializedName("colors")
    val colors: ArrayList<String> = arrayListOf(),

    @SerializedName("styles")
    var styles: List<StylesGroup>? = listOf(),

    )

data class StylesGroup(
    @SerializedName("key")
    var key: String = "",

    @SerializedName("value")
    val value: String = "",
)
