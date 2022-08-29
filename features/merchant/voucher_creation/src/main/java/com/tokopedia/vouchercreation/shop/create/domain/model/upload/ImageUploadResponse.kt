package com.tokopedia.vouchercreation.shop.create.domain.model.upload

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ImageUploadResponse (
        @SerializedName("data")
        @Expose
        val data: ImageUploadData? = null,
        @SerializedName("message_error")
        @Expose
        val messageError: MutableList<String> = mutableListOf()) {

        data class ImageUploadData(
                @SerializedName("message_status")
                @Expose
                val messageStatus: String? = null,
                @SerializedName("pic_obj")
                @Expose
                val picObj: String? = null,
                @SerializedName("pic_src")
                @Expose
                val picSrc: String? = null,
                @SerializedName("server_id")
                @Expose
                val serverId: String? = null,
                @SerializedName("success")
                @Expose
                val success: String = "1"
        )
}

