package com.tokopedia.home_recom.model.entity


import com.google.gson.annotations.SerializedName

data class Label(
        @SerializedName("color")
        val color: String,
        @SerializedName("title")
        val title: String
)