package com.tokopedia.sellerappwidget.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetChatResponse(
        @Expose
        @SerializedName("chatListMessage")
        val chatListMessage: GetChatListMessageModel? = GetChatListMessageModel(),
        @Expose
        @SerializedName("notifications")
        val notifications: NotificationsModel? = NotificationsModel()
)

data class GetChatListMessageModel(
        @Expose
        @SerializedName("list")
        val list: List<ChatListModel> = emptyList()
)

data class ChatListModel(
        @Expose
        @SerializedName("attributes")
        val attributes: ChatAttributesModel? = ChatAttributesModel(),
        @Expose
        @SerializedName("messageKey")
        val messageKey: String? = "",
        @Expose
        @SerializedName("msgID")
        val msgID: Long? = 0
)

data class ChatAttributesModel(
        @Expose
        @SerializedName("contact")
        val contact: ChatContactModel? = ChatContactModel(),
        @Expose
        @SerializedName("lastReplyMessage")
        val lastReplyMessage: String? = "",
        @Expose
        @SerializedName("lastReplyTimeStr")
        val lastReplyTimeStr: String? = ""
)

data class ChatContactModel(
        @Expose
        @SerializedName("name")
        val name: String? = ""
)

