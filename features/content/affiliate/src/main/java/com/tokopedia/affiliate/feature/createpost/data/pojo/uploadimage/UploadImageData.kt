package com.tokopedia.affiliate.feature.createpost.data.pojo.uploadimage

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by milhamj on 10/1/18.
 */
class UploadImageData {
    @SerializedName("pic_obj")
    @Expose
    val picObj: String = ""
    @SerializedName("pic_src")
    @Expose
    val picSrc: String = ""
    @SerializedName("server_id")
    @Expose
    val serverId: String = ""
    @SerializedName("success")
    @Expose
    val success: String = ""
}