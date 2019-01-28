package com.tokopedia.product.detail.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Picture(
        @SerializedName("description")
        @Expose
        val description: String = "",

        @SerializedName("fileName")
        @Expose
        val fileName: String = "",

        @SerializedName("filePath")
        @Expose
        val filePath: String = "",

        @SerializedName("picID")
        @Expose
        val id: Int = 0,

        @SerializedName("isFromIG")
        @Expose
        val isFromIg: Boolean = false,

        @SerializedName("status")
        @Expose
        val status: Int = 1,

        @SerializedName("url300")
        @Expose
        val url300: String = "",

        @SerializedName("urlOriginal")
        @Expose
        val urlOriginal: String = "",

        @SerializedName("urlThumbnail")
        @Expose
        val urlThumbnail: String = "",

        @SerializedName("width")
        @Expose
        val width: Int = 0,

        @SerializedName("height")
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