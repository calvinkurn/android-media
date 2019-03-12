package com.tokopedia.groupchat.chatroom.domain.pojo.channelinfo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ChannelInfoPojo (

    @SerializedName("channel")
    @Expose
    var channel: Channel = Channel()
){
}
