package com.tokopedia.topchat.chatsearch.view.uimodel


import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.topchat.chatsearch.data.ContactProfile
import com.tokopedia.topchat.chatsearch.view.adapter.ChatSearchTypeFactory

data class ChatReplyUiModel(
        @SerializedName("contact")
        val contact: ContactProfile = ContactProfile(),
        @SerializedName("lastMessage")
        val lastMessage: String = "",
        @SerializedName("createTimeStr")
        val timeStamp: String = "",
        @SerializedName("msgId")
        val msgId: Long = 0L,
        @SerializedName("productId")
        val productId: String = ""
) : Visitable<ChatSearchTypeFactory> {

    val tag get() = contact.attributes.tag
    val thumbnailUrl: String get() = contact.attributes.thumbnail
    val timeStampMillis get() = timeStamp.toLongOrZero()

    val modifiedTimeStamp: String get() {
        val addOffsetTimeStamp = timeStamp.toLongOrZero() + 5000
        return addOffsetTimeStamp.toString()
    }

    override fun type(typeFactory: ChatSearchTypeFactory): Int {
        return typeFactory.type(this)
    }

}