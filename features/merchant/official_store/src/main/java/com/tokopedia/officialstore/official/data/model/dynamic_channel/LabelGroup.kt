package com.tokopedia.officialstore.official.data.model.dynamic_channel

import com.google.gson.annotations.SerializedName

/**
 * Created by Lukas on 3/23/21.
 */
data class LabelGroup (
    @SerializedName("position")
    val position: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("type")
    val type: String = "",
    @SerializedName("url")
    val imageUrl: String = ""
)