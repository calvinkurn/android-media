package com.tokopedia.play.broadcaster.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 09/06/2020
 */
class PlayCoverUploadEntity(
        @SerializedName("data")
        @Expose
        val data: Data = Data())

class Data(
        @SerializedName("pic_obj")
        @Expose
        val picObj: String = "",
        @SerializedName("pic_src")
        @Expose
        val picSrc: String = "",
        @SerializedName("server_id")
        @Expose
        val serverId: String = "",
        @SerializedName("success")
        @Expose
        val success: String = ""
)