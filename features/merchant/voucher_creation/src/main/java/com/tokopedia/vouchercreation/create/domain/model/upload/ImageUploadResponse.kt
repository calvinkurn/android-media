package com.tokopedia.vouchercreation.create.domain.model.upload

import com.google.gson.annotations.SerializedName

data class ImageUploadResponse (
        @SerializedName("data")
        val data: ImageUploadData = ImageUploadData()
)

data class ImageUploadData(
        @SerializedName("pic_obj")
        val picObj: String? = "",
        @SerializedName("pic_src")
        val picSrc: String? = "",
        @SerializedName("server_id")
        val serverId: String? = "",
        @SerializedName("success")
        val success: String? = ""
)