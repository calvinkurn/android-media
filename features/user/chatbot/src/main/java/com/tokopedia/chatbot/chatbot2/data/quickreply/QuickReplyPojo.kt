package com.tokopedia.chatbot.chatbot2.data.quickreply

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by yfsx on 09/05/18.
 */

data class QuickReplyPojo(

    @SerializedName("text")
    @Expose
    val text: String = "",
    @SerializedName("value")
    @Expose
    val value: String = "",
    @SerializedName("action")
    @Expose
    val action: String = ""
)
