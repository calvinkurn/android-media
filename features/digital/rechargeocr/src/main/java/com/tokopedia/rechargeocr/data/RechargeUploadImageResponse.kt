package com.tokopedia.rechargeocr.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RechargeUploadImageResponse {
    @SerializedName("data")
    @Expose
    val data: RechargeUploadImageData = RechargeUploadImageData()
}

class RechargeUploadImageData {
    @SerializedName("pic_obj")
    @Expose
    val picObj: String? = ""
    @SerializedName("pic_src")
    @Expose
    val picSrc: String? = ""
    @SerializedName("server_id")
    @Expose
    val serverId: String? = ""
    @SerializedName("success")
    @Expose
    val success: String? = ""
}