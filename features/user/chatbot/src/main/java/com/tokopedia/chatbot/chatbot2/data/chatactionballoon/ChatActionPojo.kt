package com.tokopedia.chatbot.chatbot2.data.chatactionballoon

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Hendri on 18/07/18.
 */
class ChatActionPojo {
    @SerializedName("text")
    @Expose
    var text: String = ""

    @SerializedName("value")
    @Expose
    var value: String = ""

    @SerializedName("action")
    @Expose
    var action: String = ""

    @SerializedName("hex_color")
    @Expose
    var hexColor: String = ""

    @SerializedName("icon_url")
    @Expose
    var iconUrl: String = ""
}
