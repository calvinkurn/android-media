package com.tokopedia.topchat.chatlist.listener

import androidx.fragment.app.FragmentManager
import com.tokopedia.topchat.chatlist.pojo.ChatChangeStateResponse
import com.tokopedia.topchat.chatlist.pojo.ItemChatListPojo
import com.tokopedia.usecase.coroutines.Result

/**
 * @author : Steven 2019-08-06
 */
interface ChatListItemListener {
    fun chatItemClicked(element: ItemChatListPojo, itemPosition: Int)
    fun deleteChat(element: ItemChatListPojo, itemPosition: Int)
    fun markChatAsRead(msgIds: List<String>, result: (Result<ChatChangeStateResponse>) -> Unit)
    fun markChatAsUnread(msgIds: List<String>, result: (Result<ChatChangeStateResponse>) -> Unit)
    fun increaseNotificationCounter()
    fun decreaseNotificationCounter()
    fun trackChangeReadStatus(element: ItemChatListPojo)
    fun trackDeleteChat(element: ItemChatListPojo)
    fun isTabSeller(): Boolean
    fun getSupportChildFragmentManager(): FragmentManager
    fun pinUnpinChat(element: ItemChatListPojo, position: Int, isPinChat: Boolean = true)
    fun returnToSellerHome()
}