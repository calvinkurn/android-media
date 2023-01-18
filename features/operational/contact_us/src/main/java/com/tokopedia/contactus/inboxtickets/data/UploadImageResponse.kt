package com.tokopedia.contactus.inboxtickets.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class UploadImageResponse(
        @SerializedName("data")
        @Expose
        val data: Data = Data()
)

data class Data(
        @SerializedName("pic_obj")
        @Expose
        val picObj: String = ""
)
