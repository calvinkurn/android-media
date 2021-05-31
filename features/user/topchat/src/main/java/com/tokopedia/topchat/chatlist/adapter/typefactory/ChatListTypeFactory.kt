package com.tokopedia.topchat.chatlist.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingMoreViewHolder
import com.tokopedia.topchat.chatlist.adapter.viewholder.ChatItemListViewHolder
import com.tokopedia.topchat.chatlist.fragment.ChatListFragment
import com.tokopedia.topchat.chatlist.listener.ChatListItemListener
import com.tokopedia.topchat.chatlist.model.EmptyChatModel
import com.tokopedia.topchat.chatlist.pojo.ChatAdminNoAccessUiModel
import com.tokopedia.topchat.chatlist.pojo.ItemChatListPojo
import com.tokopedia.topchat.chatlist.viewmodel.ChatItemListViewModel

/**
 * @author : Steven 2019-08-06
 */
interface ChatListTypeFactory {

    fun type(chatItemListViewModel: ItemChatListPojo): Int

    fun type(emptyChatItemListViewModel: EmptyChatModel): Int

    fun type(chatAdminNoAccessUiModel: ChatAdminNoAccessUiModel): Int

    fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*>
}