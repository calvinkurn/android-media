package com.tokopedia.product.addedit.preview.data.source.api.response

import com.google.gson.annotations.SerializedName

data class Picture(
        @SerializedName("picID")
        val picID: String,
        @SerializedName("description")
        val description: String,
        @SerializedName("filePath")
        val filePath: String,
        @SerializedName("fileName")
        val fileName: String,
        @SerializedName("width")
        val width: String,
        @SerializedName("height")
        val height: String,
        @SerializedName("isFromIG")
        val isFromIG: String,
        @SerializedName("urlOriginal")
        val urlOriginal: String,
        @SerializedName("urlThumbnail")
        val urlThumbnail: String,
        @SerializedName("url300")
        val url300: String,
        @SerializedName("status")
        val status: String
)