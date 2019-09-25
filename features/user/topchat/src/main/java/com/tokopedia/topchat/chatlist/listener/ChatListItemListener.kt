package com.tokopedia.topchat.chatlist.listener

import com.tokopedia.topchat.chatlist.pojo.ChatChangeStateResponse
import com.tokopedia.topchat.chatlist.pojo.ItemChatListPojo
import com.tokopedia.usecase.coroutines.Result

/**
 * @author : Steven 2019-08-06
 */
interface ChatListItemListener {
    fun chatItemClicked(element: ItemChatListPojo)
    fun deleteChat(element: ItemChatListPojo, itemPosition: Int)
    fun markChatAsRead(msgIds: List<String>, result: (Result<ChatChangeStateResponse>) -> Unit)
    fun markChatAsUnread(msgIds: List<String>, result: (Result<ChatChangeStateResponse>) -> Unit)
}