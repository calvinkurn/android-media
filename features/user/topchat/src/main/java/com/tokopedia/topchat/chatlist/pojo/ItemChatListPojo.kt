package com.tokopedia.topchat.chatlist.pojo

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.domain.pojo.ChatItemPojo
import com.tokopedia.topchat.chatlist.adapter.typefactory.ChatListTypeFactory

/**
 * @author : Steven 2019-08-08
 */
data class ItemChatListPojo(
        @SerializedName("msgID")
        @Expose
        var msgId: String = "",
        @SerializedName("attributes")
        @Expose
        var attributes: ItemChatAttributesPojo?,
        @SerializedName("messageKey")
        @Expose
        var messageKey: String = ""
) : Visitable<ChatListTypeFactory>{

    override fun type(typeFactory: ChatListTypeFactory): Int {
        return typeFactory.type(this)
    }
}