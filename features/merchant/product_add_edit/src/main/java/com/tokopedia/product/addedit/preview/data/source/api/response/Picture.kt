package com.tokopedia.product.addedit.preview.data.source.api.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Picture(
        @SerializedName("picID")
        @Expose
        val picID: String = "",
        @SerializedName("description")
        @Expose
        val description: String = "",
        @SerializedName("filePath")
        @Expose
        val filePath: String = "",
        @SerializedName("fileName")
        @Expose
        val fileName: String = "",
        @SerializedName("width")
        @Expose
        val width: String = "0",
        @SerializedName("height")
        @Expose
        val height: String = "0",
        @SerializedName("isFromIG")
        @Expose
        val isFromIG: String = "",
        @SerializedName("urlOriginal")
        @Expose
        val urlOriginal: String = "",
        @SerializedName("urlThumbnail")
        @Expose
        val urlThumbnail: String = "",
        @SerializedName("url300")
        @Expose
        val url300: String = "",
        @SerializedName("status")
        @Expose
        val status: String = ""
)