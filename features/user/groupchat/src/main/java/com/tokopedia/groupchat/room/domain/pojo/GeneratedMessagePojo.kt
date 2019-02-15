package com.tokopedia.groupchat.room.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by nisie on 3/29/18.
 */

class GeneratedMessagePojo : BaseGroupChatPojo() {

    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("is_vibrate")
    @Expose
    var isVibrate: Boolean = false
    @SerializedName("send_pns")
    @Expose
    var isSendPns: Boolean = false
}
