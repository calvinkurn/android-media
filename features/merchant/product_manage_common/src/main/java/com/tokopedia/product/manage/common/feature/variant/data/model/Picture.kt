package com.tokopedia.product.manage.common.feature.variant.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Picture(
    @Expose
    @SerializedName("picID")
    val picId: String,
    @Expose
    @SerializedName("description")
    val description: String,
    @Expose
    @SerializedName("filePath")
    val filePath: String,
    @Expose
    @SerializedName("fileName")
    val fileName: String,
    @Expose
    @SerializedName("width")
    val width: Int,
    @Expose
    @SerializedName("height")
    val height: Int,
    @Expose
    @SerializedName("isFromIG")
    val isFromIG: Boolean,
    @Expose
    @SerializedName("urlOriginal")
    val urlOriginal: String,
    @Expose
    @SerializedName("urlThumbnail")
    val urlThumbnail: String,
    @Expose
    @SerializedName("url300")
    val url300: String,
    @Expose
    @SerializedName("status")
    val status: Int
)