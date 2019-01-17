package com.tokopedia.topchat.chatroom.domain.pojo

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
        var serverProcessTime: String = "",
        @SerializedName("status")
        @Expose
        var status: String = ""
) {


    data class TopChatImageUploadDataPojo(

            @SerializedName("message_status")
            @Expose
            var messageStatus: String? = null,
            @SerializedName("pic_obj")
            @Expose
            var picObj: String = "",
            @SerializedName("pic_src")
            @Expose
            var picSrc: String = "",
            @SerializedName("server_id")
            @Expose
            var serverId: String = "",
            @SerializedName("success")
            @Expose
            var success: String =""

    )
}