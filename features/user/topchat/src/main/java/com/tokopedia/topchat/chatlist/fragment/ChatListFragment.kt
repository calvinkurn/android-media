package com.tokopedia.topchat.chatlist.fragment

import android.app.Activity
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.chat_common.util.EndlessRecyclerViewScrollUpListener
import com.tokopedia.design.component.Menus
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.kotlin.util.getParamString
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.activity.ChatListActivity
import com.tokopedia.topchat.chatlist.adapter.ChatListAdapter
import com.tokopedia.topchat.chatlist.adapter.typefactory.ChatListTypeFactoryImpl
import com.tokopedia.topchat.chatlist.adapter.viewholder.ChatItemListViewHolder
import com.tokopedia.topchat.chatlist.analytic.ChatListAnalytic
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_FILTER_READ
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_FILTER_UNREAD
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_TAB_SELLER
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_TAB_USER
import com.tokopedia.topchat.chatlist.di.ChatListComponent
import com.tokopedia.topchat.chatlist.listener.ChatListContract
import com.tokopedia.topchat.chatlist.listener.ChatListItemListener
import com.tokopedia.topchat.chatlist.listener.ChatListViewState
import com.tokopedia.topchat.chatlist.listener.ChatListViewStateImpl
import com.tokopedia.topchat.chatlist.model.EmptyChatModel
import com.tokopedia.topchat.chatlist.model.IncomingChatWebSocketModel
import com.tokopedia.topchat.chatlist.model.IncomingTypingWebSocketModel
import com.tokopedia.topchat.chatlist.pojo.ChatListDataPojo
import com.tokopedia.topchat.chatlist.pojo.ChatChangeStateResponse
import com.tokopedia.topchat.chatlist.pojo.ItemChatAttributesPojo
import com.tokopedia.topchat.chatlist.pojo.ItemChatListPojo
import com.tokopedia.topchat.chatlist.viewmodel.ChatItemListViewModel
import com.tokopedia.topchat.chatlist.viewmodel.ChatItemListViewModel.Companion.arrayFilterParam
import com.tokopedia.topchat.chatroom.view.activity.TopChatRoomActivity
import com.tokopedia.topchat.chatroom.view.viewmodel.ReplyParcelableModel
import com.tokopedia.topchat.common.TopChatInternalRouter
import com.tokopedia.topchat.common.analytics.TopChatAnalytics
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import java.util.*
import javax.inject.Inject

/**
 * @author : Steven 2019-08-06
 */
class ChatListFragment : BaseListFragment<Visitable<*>,
        BaseAdapterTypeFactory>(),
        ChatListItemListener,
        LifecycleOwner {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelFragmentProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }

    private val chatItemListViewModel by lazy { viewModelFragmentProvider.get(ChatItemListViewModel::class.java) }


    private lateinit var performanceMonitoring: PerformanceMonitoring
    private lateinit var viewState: ChatListViewState

    private var activityContract: ChatListContract.Activity? = null

    private var mUserSeen = false
    private var mViewCreated = false
    private var sightTag = ""

    private var itemPositionLongClicked: Int = -1
    private var filterChecked = 0


    @Inject
    lateinit var chatListAnalytics: ChatListAnalytic

    override fun onAttachActivity(context: Context?) {
        if (context is ChatListContract.Activity) {
            activityContract = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performanceMonitoring = PerformanceMonitoring.start(TopChatAnalytics.FPM_DETAIL_CHAT)
        sightTag = getParamString(CHAT_TAB_TITLE, arguments, null, "")
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
                chatListAnalytics.eventClickFilterChat()
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
        debug(TAG, "$sightTag onViewCreated")
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
    }

    fun processIncomingMessage(newChat: IncomingChatWebSocketModel) {
        if (adapter.list.size < 1 && adapter.list[0] is LoadingModel) {
            return
        } else if (adapter.list.size == 0) {
            return
        } else if (filterChecked == arrayFilterParam.indexOf(PARAM_FILTER_READ)) {
            return
        }

        val existingThread = adapter.list.find {
            it is ItemChatListPojo && it.msgId == newChat.messageId
        }

        val index = adapter.list.indexOf(existingThread)


        updateItemOnIndex(index, newChat)
    }

    private fun updateItemOnIndex(index: Int, newChat: IncomingChatWebSocketModel) {
        when {
            //not found on list
            index == -1 -> {
                if (adapter.hasEmptyModel()) {
                    adapter.clearAllElements()
                }
                val attributes = ItemChatAttributesPojo(newChat.message, newChat.time, newChat.contact)
                val item = ItemChatListPojo(newChat.messageId, attributes, "")
                adapter.list.add(0, item)
                adapter.notifyItemInserted(0)
                animateWhenOnTop()
            }
            //found on list, not the first
            index > 0 -> {
                val existingThread: Visitable<Any>? = adapter.list[index]
                (existingThread as ItemChatListPojo).attributes?.lastReplyMessage = newChat.message
                existingThread.attributes?.unreads = existingThread.attributes?.unreads.toZeroIfNull() + 1
                existingThread.attributes?.readStatus = ChatItemListViewHolder.STATE_CHAT_UNREAD
                existingThread.attributes?.lastReplyTimeStr = newChat.time
                adapter.list.swap(index, 0)
                adapter.notifyItemMoved(index, 0)
                animateWhenOnTop()
            }
            //found on list, and the first item
            else -> {
                val existingThread: Visitable<Any>? = adapter.list[index]
                (existingThread as ItemChatListPojo).attributes?.lastReplyMessage = newChat.message
                existingThread.attributes?.unreads = existingThread.attributes?.unreads.toZeroIfNull() + 1
                existingThread.attributes?.readStatus = ChatItemListViewHolder.STATE_CHAT_UNREAD
                existingThread.attributes?.lastReplyTimeStr = newChat.time
                adapter.notifyItemChanged(0)
            }
        }
    }

    fun processIncomingMessage(newItem: IncomingTypingWebSocketModel) {

        if (adapter.list.size < 1 && adapter.list[0] is LoadingModel) {
            return
        } else if (adapter.list.size == 0) {
            return
        } else if (filterChecked == arrayFilterParam.indexOf(PARAM_FILTER_READ)) {
            return
        }

        val existingThread = adapter.list.find {
            it is ItemChatListPojo && it.msgId == newItem.messageId
        }

        val index = adapter.list.indexOf(existingThread)

        when {
            index >= 0 -> {
                if (newItem.isTyping) {
                    adapter.notifyItemChanged(index, ChatItemListViewHolder.PAYLOAD_TYPING_STATE)
                } else {
                    adapter.notifyItemChanged(index, ChatItemListViewHolder.PAYLOAD_STOP_TYPING_STATE)
                }
            }
        }
    }

    private fun animateWhenOnTop() {
        if ((getRecyclerView(view).layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition() == 0) {
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
                if (totalItemsCount > 1) {
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
    }

    private fun showFilterDialog() {
        activity?.let {
            val itemMenus = ArrayList<Menus.ItemMenus>()
            val arrayFilterString = arrayListOf(
                    it.getString(R.string.filter_chat_all),
                    it.getString(R.string.filter_chat_unread),
                    it.getString(R.string.filter_chat_unreplied)
            )

            for ((index, title) in arrayFilterString.withIndex()) {
                if (index == filterChecked) itemMenus.add(Menus.ItemMenus(title, true))
                else itemMenus.add(Menus.ItemMenus(title, false))
            }

            Menus(it, R.style.BottomFilterDialogTheme).apply {
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setTitle(getString(R.string.label_filter))
                itemMenuList = itemMenus
                setOnItemMenuClickListener { menus, pos ->
                    chatListAnalytics.eventClickListFilterChat(menus.title.toLowerCase())
                    filterChecked = pos - 1
                    loadInitialData()
                    dismiss()
                }
                show()
            }
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

    override fun chatItemClicked(element: ItemChatListPojo, itemPosition: Int) {
        activity?.let {
            with(chatListAnalytics) {
                eventClickChatList(
                        if (sightTag == PARAM_TAB_SELLER) ChatListActivity.SELLER_ANALYTICS_LABEL
                        else ChatListActivity.BUYER_ANALYTICS_LABEL)
            }

            val intent = TopChatRoomActivity.getCallingIntent(
                    activity,
                    element.msgId,
                    element.attributes?.contact?.contactName,
                    element.attributes?.contact?.tag,
                    element.attributes?.contact?.contactId,
                    element.attributes?.contact?.role,
                    0,
                    "",
                    element.attributes?.contact?.thumbnail,
                    itemPosition
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            this@ChatListFragment.startActivityForResult(intent, OPEN_DETAIL_MESSAGE)
            it.overridePendingTransition(0, 0)
        }
    }

    override fun deleteChat(element: ItemChatListPojo, itemPosition: Int) {
        chatItemListViewModel.chatMoveToTrash(element.msgId.toInt())
        itemPositionLongClicked = itemPosition
    }

    override fun markChatAsRead(msgIds: List<String>, result: (Result<ChatChangeStateResponse>) -> Unit) {
        chatItemListViewModel.markChatAsRead(msgIds, result)
    }

    override fun markChatAsUnread(msgIds: List<String>, result: (Result<ChatChangeStateResponse>) -> Unit) {
        chatItemListViewModel.markChatAsUnread(msgIds, result)
    }

    override fun increaseNotificationCounter() {
        when (sightTag) {
            PARAM_TAB_USER -> activityContract?.increaseUserNotificationCounter()
            PARAM_TAB_SELLER -> activityContract?.increaseSellerNotificationCounter()
        }
    }

    override fun decreaseNotificationCounter() {
        when (sightTag) {
            PARAM_TAB_USER -> activityContract?.decreaseUserNotificationCounter()
            PARAM_TAB_SELLER -> activityContract?.decreaseSellerNotificationCounter()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == OPEN_DETAIL_MESSAGE) {
            data?.extras?.let { extras ->
                itemPositionLongClicked = extras.getInt(TopChatInternalRouter.Companion.RESULT_INBOX_CHAT_PARAM_INDEX, -1)
                when (resultCode) {
//                    Activity.RESULT_OK -> {
//                        val moveToTop = extras.getBoolean(TopChatInternalRouter.Companion.RESULT_INBOX_CHAT_PARAM_MOVE_TO_TOP)
//                        if(moveToTop) {
//                            val lastItem = extras.getParcelable<ReplyParcelableModel>(TopChatInternalRouter.Companion.RESULT_LAST_ITEM)
//                            lastItem?.let {
//                                val model = IncomingChatWebSocketModel(lastItem.messageId, lastItem.msg, lastItem.replyTime)
//                                updateItemOnIndex(itemPositionLongClicked, model)
//                            }
//                        }
//                    }
                    TopChatInternalRouter.Companion.CHAT_DELETED_RESULT_CODE -> {
                        adapter.deleteItem(itemPositionLongClicked)
                    }
                    else -> {}
                }
            }
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

    private fun onViewCreatedFirstSight(view: View?) {
        debug(TAG, "$sightTag onViewCreatedFirstSight")
        (activity as ChatListContract.Activity).notifyViewCreated()
        loadInitialData()
    }

    private fun onUserFirstSight() {
        debug(TAG, "$sightTag onUserFirstSight")
    }


    private fun onUserVisibleChanged(visible: Boolean) {
        debug(TAG, "$sightTag onUserVisibleChanged $visible")
    }

    override fun callInitialLoadAutomatically(): Boolean {
        return false
    }

    override fun hasInitialSwipeRefresh(): Boolean {
        return true
    }

    override fun getEmptyDataViewModel(): Visitable<*> {
        var title = ""
        var subtitle = ""
        var image = ""
        activity?.let {
            when (sightTag) {
                PARAM_TAB_USER -> {
                    title = it.getString(R.string.buyer_empty_chat_title)
                    subtitle = it.getString(R.string.buyer_empty_chat_subtitle)
                    image = CHAT_BUYER_EMPTY
                }

                PARAM_TAB_SELLER -> {
                    title = it.getString(R.string.seller_empty_chat_title)
                    subtitle = it.getString(R.string.seller_empty_chat_subtitle)
                    image = CHAT_SELLER_EMPTY
                }
            }

            if (filterChecked == arrayFilterParam.indexOf(PARAM_FILTER_UNREAD)) {
                image = CHAT_BUYER_EMPTY
                title = it.getString(R.string.empty_chat_read_all_title)
                subtitle = ""
            }
        }

        return EmptyChatModel(title, subtitle, image)
    }

    override fun onSwipeRefresh() {
        super.onSwipeRefresh()
        activityContract?.loadNotificationCounter()
    }

    companion object {
        const val OPEN_DETAIL_MESSAGE = 1324
        private const val CHAT_TAB_TITLE = "chat_tab_title"
        private const val CHAT_SELLER_EMPTY = "https://ecs7.tokopedia.net/img/android/others/chat-seller-empty.png"
        private const val CHAT_BUYER_EMPTY = "https://ecs7.tokopedia.net/img/android/others/chat-buyer-empty.png"
        const val TAG = "ChatListFragment"

        fun createFragment(title: String): ChatListFragment {
            val bundle = Bundle()
            bundle.putString(CHAT_TAB_TITLE, title)
            val fragment = ChatListFragment()
            fragment.arguments = bundle
            return fragment
        }

    }
}