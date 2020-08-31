package com.tokopedia.discovery2.data.categorynavigationresponse

import com.google.gson.annotations.SerializedName

data class ChildItem(

        @SerializedName("applinks")
        val applinks: String? = null,

        @SerializedName("name")
        val name: String? = null,

        @SerializedName("id")
        val id: String? = null,

        @SerializedName("url")
        val url: String? = null,

        @SerializedName("is_adult")
        val isAdult: Int? = null,

        @SerializedName("thumbnail_image")
        val thumbnailImage: String? = null
)