package com.tokopedia.topchat.chatlist.listener

import com.tokopedia.topchat.chatlist.pojo.ItemChatListPojo

/**
 * @author : Steven 2019-08-06
 */
interface ChatListItemListener {
    fun chatItemClicked(element: ItemChatListPojo)
    fun chatItemDeleted(element: ItemChatListPojo, itemPosition: Int)
}