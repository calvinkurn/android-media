package com.tokopedia.mediauploader.image.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.mediauploader.common.data.entity.UploadHeader

data class ImageUploader(
    @Expose @SerializedName("header") val header: UploadHeader = UploadHeader(),
    @Expose @SerializedName("data") val data: UploadData? = UploadData()
)