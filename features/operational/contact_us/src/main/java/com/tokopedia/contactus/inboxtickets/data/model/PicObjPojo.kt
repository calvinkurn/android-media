package com.tokopedia.contactus.inboxtickets.data.model


import com.google.gson.annotations.SerializedName

data class PicObjPojo(
    @SerializedName("file_name")
    var fileName: String?,
    @SerializedName("file_path")
    var filePath: String?,
    @SerializedName("h")
    val h: String?,
    @SerializedName("server_id")
    val serverId: String?,
    @SerializedName("w")
    val w: String?
)
