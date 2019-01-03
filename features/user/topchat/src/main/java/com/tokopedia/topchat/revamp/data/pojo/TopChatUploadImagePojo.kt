package com.tokopedia.topchat.uploadimage.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author : Steven 14/12/18
 */

data class TopChatImageUploadPojo(
        @SerializedName("data")
        @Expose
        var data: TopChatImageUploadDataPojo? = null,
        @SerializedName("server_process_time")
        @Expose
        var serverProcessTime: String? = null,
        @SerializedName("status")
        @Expose
        var status: String? = null
) {


    data class TopChatImageUploadDataPojo(

            @SerializedName("message_status")
            @Expose
            var messageStatus: String? = null,
            @SerializedName("pic_obj")
            @Expose
            var picObj: String? = null,
            @SerializedName("pic_src")
            @Expose
            var picSrc: String? = null,
            @SerializedName("server_id")
            @Expose
            var serverId: String? = null,
            @SerializedName("success")
            @Expose
            var success: String? = null

    )
}