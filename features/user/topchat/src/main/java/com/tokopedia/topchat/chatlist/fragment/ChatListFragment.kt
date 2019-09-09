package com.tokopedia.topchat.chatlist.fragment

import android.app.Activity
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.Toast
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.chat_common.util.EndlessRecyclerViewScrollUpListener
import com.tokopedia.design.component.Dialog
import com.tokopedia.design.component.Menus
import com.tokopedia.kotlin.extensions.view.debug
import com.tokopedia.kotlin.extensions.view.showErrorToaster
import com.tokopedia.kotlin.extensions.view.showNormalToaster
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.kotlin.util.getParamString
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.adapter.ChatListAdapter
import com.tokopedia.topchat.chatlist.adapter.typefactory.ChatListTypeFactoryImpl
import com.tokopedia.topchat.chatlist.adapter.viewholder.ChatItemListViewHolder
import com.tokopedia.topchat.chatlist.di.ChatListComponent
import com.tokopedia.topchat.chatlist.listener.*
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
import java.util.ArrayList
import javax.inject.Inject

/**
 * @author : Steven 2019-08-06
 */
class ChatListFragment: BaseListFragment<Visitable<*>,
        BaseAdapterTypeFactory>(),
        ChatListContract.View,
        ChatListItemListener,
        ChatListWebSocketContract.Fragment,
        LifecycleOwner {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelActivityProvider by lazy { activity?.let { ViewModelProviders.of(it, viewModelFactory) } }
    private val viewModelFragmentProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }

    private val chatItemListViewModel by lazy { viewModelFragmentProvider.get(ChatItemListViewModel::class.java) }
    private val webSocketViewModel by lazy { viewModelActivityProvider?.get(WebSocketViewModel::class.java) }

    private lateinit var performanceMonitoring: PerformanceMonitoring
    private lateinit var viewState: ChatListViewState

    private var mUserSeen = false
    private var mViewCreated = false
    private var sightTag = ""

    private var itemPositionLongClicked = 0
    private var filterChecked = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performanceMonitoring = PerformanceMonitoring.start(TopChatAnalytics.FPM_DETAIL_CHAT)
        sightTag =  getParamString(CHAT_TAB_TITLE, arguments, null, "")
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.chat_options_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
//            R.id.menu_chat_search -> {
//                RouteManager.route(activity, ApplinkConstInternalMarketplace.CHAT_SEARCH)
//                true
//            }
            R.id.menu_chat_filter -> {
                showFilterDialog()
                true
            }
//            R.id.menu_chat_setting -> {
//                true
//            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chat_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        debug("stevensight", "$sightTag onViewCreated")
        mViewCreated = true;
        tryViewCreatedFirstSight()
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView(view)
        initView(view)
        setObserver()
    }

    private fun tryViewCreatedFirstSight() {
        if (mUserSeen && mViewCreated) {
            onViewCreatedFirstSight(view)
        }
    }

    private fun initView(view: View) {
        showLoading()
        viewState = ChatListViewStateImpl(view)
    }

    private fun setUpRecyclerView(view: View) {
        val recyclerView = super.getRecyclerView(view)
        recyclerView.setHasFixedSize(true)
        for (i in 0 until recyclerView.itemDecorationCount) {
            recyclerView.removeItemDecorationAt(i)
        }
    }

    private fun setObserver() {
        chatItemListViewModel.mutateChatList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessGetChatList(it.data.data)
                is Fail -> onFailGetChatList(it.throwable)
            }
        })

        chatItemListViewModel.deleteChat.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Success -> adapter.deleteItem(itemPositionLongClicked)
                is Fail -> view?.showErrorToaster(getString(R.string.delete_chat_default_error_message))
            }
        })

        activity?.let {
            webSocketViewModel?.itemChat?.observe(it,
                    Observer { result ->
                        when (result) {
                            is Success -> {
                                when(result.data) {
                                    is IncomingChatWebSocketModel -> processIncomingMessage(result.data as IncomingChatWebSocketModel)
                                    is IncomingTypingWebSocketModel -> processIncomingMessage(result.data as IncomingTypingWebSocketModel)
                                }
                            }
                        }
                    }
            )
        }
    }

    private fun processIncomingMessage(newChat: IncomingChatWebSocketModel) {

        val existingThread = adapter.list.find {
            it is ItemChatListPojo && it.msgId == newChat.messageId
        }

        val index = adapter.list.indexOf(existingThread)


        when {
            //not found on list
            index == -1 -> {
                val attributes = ItemChatAttributesPojo(newChat.message, newChat.contact)
                val item = ItemChatListPojo(newChat.messageId, attributes, "")
                adapter.list.add(0, item)
                adapter.notifyItemInserted(0)
            }
            //found on list, not the first
            index > 0 -> {

                (existingThread as ItemChatListPojo).attributes?.lastReplyMessage = newChat.message
                existingThread.attributes?.unreads = existingThread.attributes?.unreads.toZeroIfNull() + 1
                adapter.list.removeAt(index)
                adapter.list.add(0, existingThread)
                adapter.notifyItemRangeChanged(0, index+1)
                animateWhenOnTop()

            }
            //found on list, and the first item
            else -> {
                (existingThread as ItemChatListPojo).attributes?.lastReplyMessage = newChat.message
                existingThread.attributes?.unreads = existingThread.attributes?.unreads.toZeroIfNull() + 1
                adapter.notifyItemChanged(0)
            }
        }
    }

    private fun processIncomingMessage(newItem: IncomingTypingWebSocketModel) {
        val existingThread = adapter.list.find {
            it is ItemChatListPojo && it.msgId == newItem.messageId
        }

        val index = adapter.list.indexOf(existingThread)

        when {
            index >= 0 -> {
                if(newItem.isTyping) {
                    adapter.notifyItemChanged(index, ChatItemListViewHolder.PAYLOAD_TYPING_STATE)
                } else {
                    adapter.notifyItemChanged(index, ChatItemListViewHolder.PAYLOAD_STOP_TYPING_STATE)
                }
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

    }

    override fun createEndlessRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollUpListener(getRecyclerView(view).layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                showLoading()
                if(totalItemsCount > 1) {
                    loadData(page)
                }
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

    private fun showFilterDialog() {
        activity?.let {
            val menus = Menus(it)

            val arrayFilterString = arrayListOf(
                    it.getString(R.string.filter_chat_all),
                    it.getString(R.string.filter_chat_unread),
                    it.getString(R.string.filter_chat_read),
                    it.getString(R.string.filter_chat_unreplied)
            )
            val itemMenus = ArrayList<Menus.ItemMenus>()
            for ((index, title) in arrayFilterString.withIndex()) {
                if(index == filterChecked) itemMenus.add(Menus.ItemMenus(title, true))
                else itemMenus.add(Menus.ItemMenus(title, false))
            }
            menus.setTitle(getString(R.string.label_filter))
            menus.itemMenuList = itemMenus
            menus.setOnItemMenuClickListener { itemMenusClicked, pos ->
                filterChecked = pos-1
                loadInitialData()
                menus.dismiss()
            }
            menus.show()
        }
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        getComponent(ChatListComponent::class.java).inject(this)
    }

    override fun loadData(page: Int) {
        chatItemListViewModel.getChatListMessage(page, filterChecked, sightTag)
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

    override fun chatItemDeleted(element: ItemChatListPojo, itemPosition: Int) {
        Dialog(activity, Dialog.Type.PROMINANCE).apply {
            setTitle(getString(R.string.topchat_chat_delete_title))
            setDesc(getString(R.string.topchat_chat_delete_body))
            setBtnCancel(getString(R.string.topchat_chat_delete_cancel))
            setOnCancelClickListener {
                dismiss()
            }

            setBtnOk(getString(R.string.topchat_chat_delete_confirm))
            setOnOkClickListener {
                chatItemListViewModel.chatMoveToTrash(element.msgId.toInt())
                itemPositionLongClicked = itemPosition
                dismiss()
            }
            show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == OPEN_DETAIL_MESSAGE
                && resultCode == Activity.RESULT_OK && data != null) {
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        chatItemListViewModel.clear()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (!mUserSeen && isVisibleToUser) {
            mUserSeen = true
            onUserFirstSight()
            tryViewCreatedFirstSight()
        }
        onUserVisibleChanged(isVisibleToUser)
    }

    protected fun onViewCreatedFirstSight(view: View?) {
        debug("stevensight", "$sightTag onViewCreatedFirstSight")
        (activity as ChatListWebSocketContract.Activity).notifyViewCreated()
        loadInitialData()
    }

    protected fun onUserFirstSight() {
        debug("stevensight", "$sightTag onUserFirstSight")
    }


    protected fun onUserVisibleChanged(visible: Boolean) {
        debug("stevensight", "$sightTag onUserVisibleChanged $visible")
    }

    override fun callInitialLoadAutomatically(): Boolean {
        return false
    }

    override fun hasInitialSwipeRefresh(): Boolean {
        return true
    }

    companion object {
        const val OPEN_DETAIL_MESSAGE = 1324
        private const val CHAT_TAB_TITLE = "chat_tab_title"

        fun createFragment(title: String): ChatListFragment {
            val bundle = Bundle()
            bundle.putString(CHAT_TAB_TITLE, title)
            val fragment = ChatListFragment()
            fragment.arguments = bundle
            return fragment
        }

    }
}