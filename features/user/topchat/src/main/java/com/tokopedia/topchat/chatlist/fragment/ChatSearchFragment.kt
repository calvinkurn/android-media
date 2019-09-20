package com.tokopedia.topchat.chatlist.fragment

import android.app.Activity
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.chat_common.util.EndlessRecyclerViewScrollUpListener
import com.tokopedia.kotlin.extensions.view.debug
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.kotlin.util.getParamString
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.adapter.ChatListAdapter
import com.tokopedia.topchat.chatlist.adapter.typefactory.ChatListTypeFactoryImpl
import com.tokopedia.topchat.chatlist.adapter.viewholder.ChatItemListViewHolder
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant
import com.tokopedia.topchat.chatlist.di.ChatListComponent
import com.tokopedia.topchat.chatlist.listener.*
import com.tokopedia.topchat.chatlist.model.BaseIncomingItemWebSocketModel
import com.tokopedia.topchat.chatlist.model.IncomingChatWebSocketModel
import com.tokopedia.topchat.chatlist.model.IncomingTypingWebSocketModel
import com.tokopedia.topchat.chatlist.pojo.ChatListDataPojo
import com.tokopedia.topchat.chatlist.pojo.ItemChatAttributesPojo
import com.tokopedia.topchat.chatlist.pojo.ItemChatListPojo
import com.tokopedia.topchat.chatlist.viewmodel.ChatItemListViewModel
import com.tokopedia.topchat.chatlist.viewmodel.WebSocketViewModel
import com.tokopedia.topchat.chatroom.view.activity.TopChatRoomActivity
import com.tokopedia.topchat.common.analytics.TopChatAnalytics
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject


/**
 * @author : Steven 2019-08-06
 */
class ChatSearchFragment : Fragment() {
//        BaseListFragment<Visitable<*>, BaseAdapterTypeFactory>()
//                        , LifecycleOwner {
//    override fun getAdapterTypeFactory(): BaseAdapterTypeFactory {
//
//    }
//
//    override fun onItemClicked(t: Visitable<*>?) {
//
//    }
//
//    override fun getScreenName(): String {
//
//    }
//
//    override fun initInjector() {
//
//    }
//
//    override fun loadData(page: Int) {
//
//    }

    companion object {
        const val OPEN_DETAIL_MESSAGE = 1324
        private const val CHAT_TAB_TITLE = "chat_tab_title"

        fun createFragment(): ChatSearchFragment {
            return ChatSearchFragment()
        }

    }
}