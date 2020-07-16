package com.tokopedia.topchat.chatsearch.view.uimodel


import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topchat.chatsearch.data.ContactProfile
import com.tokopedia.topchat.chatsearch.view.adapter.ChatSearchTypeFactory

data class SearchResultUiModel(
        @SerializedName("contact")
        val contact: ContactProfile = ContactProfile(),
        @SerializedName("createBy")
        val createBy: Int = 0,
        @SerializedName("createTimeStr")
        val createTimeStr: String = "",
        @SerializedName("lastMessage")
        val lastMessage: String = "",
        @SerializedName("msgId")
        val msgId: Int = 0,
        @SerializedName("oppositeId")
        val oppositeId: Int = 0,
        @SerializedName("oppositeType")
        val oppositeType: Int = 0
) : Visitable<ChatSearchTypeFactory> {

    val thumbnailUrl: String get() = contact.attributes.thumbnail
    val userName: String get() = contact.attributes.name

    override fun type(typeFactory: ChatSearchTypeFactory): Int {
        return typeFactory.type(this)
    }
}