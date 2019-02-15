package com.tokopedia.groupchat.chatroom.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by yfsx on 18/12/18.
 */
data class OverlayMessagePojo (

        @SerializedName("type_message")
    @Expose
    var typeMessage: String = "",
        @SerializedName("status")
    @Expose
    var status: Int = 0,
        @SerializedName("assets")
    @Expose
    var assets: OverlayMessageAssetPojo = OverlayMessageAssetPojo(),
        @SerializedName("closeable")
    @Expose
    var isCloseable: Boolean = false
){
}
