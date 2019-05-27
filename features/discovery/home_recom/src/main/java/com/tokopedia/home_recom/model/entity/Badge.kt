package com.tokopedia.home_recom.model.entity


import com.google.gson.annotations.SerializedName

data class Badge(
        @SerializedName("image_url")
        val imageUrl: String,
        @SerializedName("title")
        val title: String
)