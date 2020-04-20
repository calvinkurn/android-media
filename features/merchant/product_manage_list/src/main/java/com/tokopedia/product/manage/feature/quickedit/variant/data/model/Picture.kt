package com.tokopedia.product.manage.feature.quickedit.variant.data.model

import com.google.gson.annotations.SerializedName

data class Picture(
    @SerializedName("picID")
    val picId: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("filePath")
    val filePath: String,
    @SerializedName("fileName")
    val fileName: String,
    @SerializedName("width")
    val width: Int,
    @SerializedName("height")
    val height: Int,
    @SerializedName("isFromIG")
    val isFromIG: Boolean,
    @SerializedName("urlOriginal")
    val urlOriginal: String,
    @SerializedName("urlThumbnail")
    val urlThumbnail: String,
    @SerializedName("url300")
    val url300: String,
    @SerializedName("status")
    val status: Int
)