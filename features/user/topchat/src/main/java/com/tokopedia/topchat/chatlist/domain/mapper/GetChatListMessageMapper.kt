package com.tokopedia.topchat.chatlist.domain.mapper

import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.topchat.chatlist.pojo.ChatListPojo
import javax.inject.Inject

class GetChatListMessageMapper @Inject constructor() {

    fun mapPinChat(response: ChatListPojo, page: Int): List<String> {
        if (page != 1) return emptyList()
        return response.data.list
                .filter { it.isPinned }
                .map { it.msgId }
    }

    fun mapUnpinChat(response: ChatListPojo): List<String> {
        return response.data.list
                .filter { !it.isPinned }
                .map { it.msgId }
    }

    fun convertStrTimestampToLong(response: ChatListPojo) {
        for (chat in response.data.list) {
            chat.attributes?.lastReplyTimestamp = chat.attributes?.lastReplyTimeStr?.toLongOrZero()
                    ?: 0
        }
    }

}