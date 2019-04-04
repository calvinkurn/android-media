package com.tokopedia.groupchat.chatroom.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author : Steven 08/10/18
 */
class UserData {

    @SerializedName("id")
    @Expose
    var id: String = ""
    @SerializedName("name")
    @Expose
    var name: String = ""
    @SerializedName("image")
    @Expose
    var image: String = ""

}
