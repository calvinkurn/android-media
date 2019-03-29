package com.tokopedia.broadcast.message.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ImageAttachment(@SerializedName("data")
                           @Expose
                           val data: Data? = null,
                           @SerializedName("message_error")
                           @Expose
                           var messageError: MutableList<String> = mutableListOf()){

    data class Data (
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