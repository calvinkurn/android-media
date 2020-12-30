package com.tokopedia.sellerappwidget.data.model

import com.google.gson.annotations.SerializedName

data class GetChatResponse(
        @SerializedName("chatListMessage")
        val chatListMessage: GetChatListMessageModel? = GetChatListMessageModel(),
        @SerializedName("notifications")
        val notifications: NotificationsModel? = NotificationsModel()
)

data class GetChatListMessageModel(
        @SerializedName("list")
        val list: List<ChatListModel> = emptyList()
)

data class ChatListModel(
        @SerializedName("attributes")
        val attributes: ChatAttributesModel? = ChatAttributesModel(),
        @SerializedName("messageKey")
        val messageKey: String? = "",
        @SerializedName("msgID")
        val msgID: Long? = 0
)

data class ChatAttributesModel(
        @SerializedName("contact")
        val contact: ChatContactModel? = ChatContactModel(),
        @SerializedName("lastReplyMessage")
        val lastReplyMessage: String? = "",
        @SerializedName("lastReplyTimeStr")
        val lastReplyTimeStr: String? = ""
)

data class ChatContactModel(
        @SerializedName("name")
        val name: String? = ""
)

