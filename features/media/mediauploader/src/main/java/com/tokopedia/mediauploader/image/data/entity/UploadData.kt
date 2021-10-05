package com.tokopedia.mediauploader.image.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UploadData(
        @Expose @SerializedName("upload_id") val uploadId: String = ""
)