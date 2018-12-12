package com.tokopedia.chatbot.domain.pojo.quickreply

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class QuickReplyAttachmentAttributes {

    @SerializedName("new_quick_replies")
    @Expose
    val quickReplies: List<QuickReplyPojo> = ArrayList()

}
