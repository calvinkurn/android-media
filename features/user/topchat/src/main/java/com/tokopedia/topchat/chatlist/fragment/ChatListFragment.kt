package com.tokopedia.topchat.chatlist.fragment

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.chat_common.util.EndlessRecyclerViewScrollUpListener
import com.tokopedia.kotlin.extensions.view.debug
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.adapter.ChatListAdapter
import com.tokopedia.topchat.chatlist.adapter.typefactory.ChatListTypeFactoryImpl
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant
import com.tokopedia.topchat.chatlist.di.ChatListComponent
import com.tokopedia.topchat.chatlist.listener.ChatListContract
import com.tokopedia.topchat.chatlist.listener.ChatListItemListener
import com.tokopedia.topchat.chatlist.listener.ChatListViewState
import com.tokopedia.topchat.chatlist.listener.ChatListViewStateImpl
import com.tokopedia.topchat.chatlist.model.IncomingItemWebSocketModel
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
class ChatListFragment : BaseListFragment<Visitable<*>, BaseAdapterTypeFactory>()
                        , ChatListContract.View
                        , ChatListItemListener{

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }

    private val chatItemListViewModel by lazy { viewModelProvider.get(ChatItemListViewModel::class.java) }
    private val webSocketViewModel by lazy { viewModelProvider.get(WebSocketViewModel::class.java) }

    private lateinit var performanceMonitoring: PerformanceMonitoring
    lateinit var viewState: ChatListViewState


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performanceMonitoring = PerformanceMonitoring.start(TopChatAnalytics.FPM_DETAIL_CHAT)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chat_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView(view)
        initView(view)
        setObserver()

        webSocketViewModel.connectWebSocket()
    }

    private fun initView(view: View) {
        viewState = ChatListViewStateImpl(view)
        getSwipeRefreshLayout(view)?.isEnabled = false
    }

    private fun setUpRecyclerView(view: View) {
        val recyclerView = super.getRecyclerView(view)
        recyclerView.setHasFixedSize(true)
        for (i in 0 until recyclerView.itemDecorationCount) {
            recyclerView.removeItemDecorationAt(i)
        }
    }

    private fun setObserver() {
        chatItemListViewModel.mutateChatListResponse.observe(
                this,
                Observer {
                    when(it) {
                        is Success -> onSuccessGetChatList(it.data.data)
                        is Fail -> onFailGetChatList(it.throwable)
                    }
                }
        )

        webSocketViewModel.itemChat.observe(
                this,
                Observer {
                    when(it) {
                        is Success -> processIncomingMessage(it)
                    }
                }
        )
    }

    private fun processIncomingMessage(newChat: Success<IncomingItemWebSocketModel>) {
        var existingThread = adapter.list.find {
            it is ItemChatListPojo && it.msgId == newChat.data.messageId
        }

        val index = adapter.list.indexOf(existingThread)


        when {
            index == -1 -> {
                var attributes = ItemChatAttributesPojo(newChat.data.message, newChat.data.contact)
                val item = ItemChatListPojo(newChat.data.messageId, attributes, "")
                adapter.list.add(0, item)
                adapter.notifyItemInserted(0)
            }
            index > 0 -> {
//                (existingThread as ItemChatListPojo).attributes?.lastReplyMessage = newChat.data.message
//                adapter.list.removeAt(index)
//                adapter.notifyItemRemoved(index)
//                adapter.list.add(0, existingThread)
//                adapter.notifyItemInserted(0)
//                animateWhenOnTop()

                (existingThread as ItemChatListPojo).attributes?.lastReplyMessage = newChat.data.message
                existingThread.attributes?.unreads = existingThread.attributes?.unreads.toZeroIfNull() + 1
                adapter.list.removeAt(index)
                adapter.list.add(0, existingThread)
                adapter.notifyItemRangeChanged(0, index+1)
                animateWhenOnTop()

//                (existingThread as ItemChatListPojo).attributes?.lastReplyMessage = newChat.data.message
//
//                var old= adapter.list
//                var new= old.toCollection(mutableListOf())
//
//                new.removeAt(index)
//                new.add(0, existingThread)
//
//                adapter.notifyChanges(old, new)
//                animateWhenOnTop()
            }
            else -> {
                (existingThread as ItemChatListPojo).attributes?.lastReplyMessage = newChat.data.message
                existingThread.attributes?.unreads = existingThread.attributes?.unreads.toZeroIfNull() + 1
                adapter.notifyItemChanged(0)
            }
        }
    }

    private fun animateWhenOnTop() {
        if((getRecyclerView(view).layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition() == 0) {
            getRecyclerView(view).smoothScrollToPosition(0)
        }
    }

    private fun onSuccessGetChatList(data: ChatListDataPojo) {
        renderList(data.list, data.hasNext)
    }

    private fun onFailGetChatList(throwable: Throwable) {
        debug("tevxxF", throwable.toString())
    }


    override fun createEndlessRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollUpListener(getRecyclerView(view).layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                showLoading()
                loadData(page)
            }
        }
    }

    override fun getAdapterTypeFactory(): ChatListTypeFactoryImpl {
        return ChatListTypeFactoryImpl(this)
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory> {
        return ChatListAdapter(adapterTypeFactory)
    }

    override fun getAdapter(): ChatListAdapter {
        return super.getAdapter() as ChatListAdapter
    }

    override fun onItemClicked(t: Visitable<*>?) {
        if(t is ItemChatListPojo) {
            debug("tevClick", adapter.list.indexOf(t).toString())
        }
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        getComponent(ChatListComponent::class.java).inject(this)
    }

    override fun loadData(page: Int) {
        chatItemListViewModel.mutateGetChatListMessage(
                page,
                ChatListQueriesConstant.PARAM_FILTER_ALL,
                ChatListQueriesConstant.PARAM_TAB_INBOX,
                10)
    }

    override fun chatItemClicked(element: ItemChatListPojo) {
        activity?.let {
            val intent = TopChatRoomActivity.getCallingIntent(
                    activity,
                    element.msgId,
                    element.attributes?.contact?.contactName,
                    element.attributes?.contact?.tag,
                    element.attributes?.contact?.contactId,
                    element.attributes?.contact?.role,
                    0,
                    "",
                    element.attributes?.contact?.thumbnail
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            this@ChatListFragment.startActivityForResult(intent, OPEN_DETAIL_MESSAGE)
            it.overridePendingTransition(0, 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == OPEN_DETAIL_MESSAGE
                && resultCode == Activity.RESULT_OK && data != null) {
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()

        chatItemListViewModel.mutateChatListResponse.removeObservers(this)
        chatItemListViewModel.clear()

        webSocketViewModel.itemChat.removeObservers(this)
        webSocketViewModel.clear()
    }

    companion object {
        const val OPEN_DETAIL_MESSAGE = 1324
    }
}