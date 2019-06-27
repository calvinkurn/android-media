package com.tokopedia.groupchat.chatroom.domain.pojo.poll

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Option (

    @SerializedName("option_id")
    @Expose
    var optionId: Int = 0,
    @SerializedName("option")
    @Expose
    var option: String = "",
    @SerializedName("image_option")
    @Expose
    var imageOption: String = ""
){}
