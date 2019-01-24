package com.tokopedia.product.detail.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Picture(
        @SerializedName("description")
        @Expose
        val description: String = "",

        @SerializedName("file_name")
        @Expose
        val fileName: String = "",

        @SerializedName("file_path")
        @Expose
        val filePath: String = "",

        @SerializedName("id")
        @Expose
        val id: Int = 0,

        @SerializedName("is_from_ig")
        @Expose
        val isFromIg: Boolean = false,

        @SerializedName("is_primary")
        @Expose
        val isPrimary: Boolean = false,

        @SerializedName("url_300")
        @Expose
        val url300: String = "",

        @SerializedName("url_original")
        @Expose
        val urlOriginal: String = "",

        @SerializedName("url_thumbnail")
        @Expose
        val urlThumbnail: String = "",

        @SerializedName("x")
        @Expose
        val width: Int = 0,

        @SerializedName("y")
        @Expose
        val height: Int = 0
)

data class Video(
        @SerializedName("source")
        @Expose
        val source: String = "",

        @SerializedName("url")
        @Expose
        val url: String = ""
)