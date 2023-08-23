package com.tokopedia.topchat.chatlist.view.listener

import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.stories.widget.StoriesWidgetManager
import com.tokopedia.topchat.chatlist.domain.pojo.ChatChangeStateResponse
import com.tokopedia.topchat.chatlist.domain.pojo.ItemChatListPojo
import com.tokopedia.topchat.chatlist.domain.pojo.operational_insight.ShopChatTicker
import com.tokopedia.usecase.coroutines.Result

/**
 * @author : Steven 2019-08-06
 */
interface ChatListItemListener {
    fun chatItemClicked(element: ItemChatListPojo, itemPosition: Int, lastActiveChat: Pair<ItemChatListPojo?, Int?>)
    fun deleteChat(element: ItemChatListPojo, itemPosition: Int)
    fun markChatAsRead(msgIds: List<String>, result: (Result<ChatChangeStateResponse>) -> Unit)
    fun markChatAsUnread(msgIds: List<String>, result: (Result<ChatChangeStateResponse>) -> Unit)
    fun increaseNotificationCounter()
    fun decreaseNotificationCounter()
    fun trackChangeReadStatus(element: ItemChatListPojo)
    fun trackDeleteChat(element: ItemChatListPojo)
    fun isTabSeller(): Boolean
    fun getSupportChildFragmentManager(): FragmentManager
    fun getStoriesWidgetManager(): StoriesWidgetManager
    fun pinUnpinChat(element: ItemChatListPojo, position: Int, isPinChat: Boolean = true)
    fun returnToSellerHome()
    fun onScrollToTop()
    fun onOperationalInsightTickerShown(element: ShopChatTicker)
    fun onOperationalInsightTickerClicked(element: ShopChatTicker)
    fun onOperationalInsightCloseButtonClicked(visitable: Visitable<*>)
}
