package com.tokopedia.mediauploader.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Uploader(
        @Expose @SerializedName("header") val header: UploadHeader = UploadHeader(),
        @Expose @SerializedName("data") val data: UploadData = UploadData()
)