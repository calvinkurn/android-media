package com.tokopedia.topchat.chatsearch.view.uimodel


import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topchat.chatsearch.data.reply.ChatReplyContact
import com.tokopedia.topchat.chatsearch.view.adapter.ChatSearchTypeFactory

data class ChatReplyUiModel(
        @SerializedName("contact")
        val contact: ChatReplyContact = ChatReplyContact(),
        @SerializedName("lastMessage")
        val lastMessage: String = "",
        @SerializedName("msgId")
        val msgId: Int = 0,
        @SerializedName("productId")
        val productId: String = "",
        @SerializedName("replyId")
        val replyId: Int = 0
): Visitable<ChatSearchTypeFactory> {

        override fun type(typeFactory: ChatSearchTypeFactory): Int {
                return typeFactory.type(this)
        }

}