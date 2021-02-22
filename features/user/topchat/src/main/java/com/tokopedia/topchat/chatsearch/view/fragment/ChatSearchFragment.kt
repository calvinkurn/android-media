package com.tokopedia.topchat.chatsearch.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatsearch.analytic.ChatSearchAnalytic
import com.tokopedia.topchat.chatsearch.data.RecentSearch
import com.tokopedia.topchat.chatsearch.di.ChatSearchComponent
import com.tokopedia.topchat.chatsearch.view.activity.ChatSearchActivity
import com.tokopedia.topchat.chatsearch.view.adapter.ChatSearchAdapter
import com.tokopedia.topchat.chatsearch.view.adapter.ChatSearchTypeFactory
import com.tokopedia.topchat.chatsearch.view.adapter.ChatSearchTypeFactoryImpl
import com.tokopedia.topchat.chatsearch.view.adapter.viewholder.ContactLoadMoreViewHolder
import com.tokopedia.topchat.chatsearch.view.adapter.viewholder.EmptySearchChatViewHolder
import com.tokopedia.topchat.chatsearch.view.adapter.viewholder.ItemSearchChatReplyViewHolder
import com.tokopedia.topchat.chatsearch.view.uimodel.ChatReplyUiModel
import com.tokopedia.topchat.chatsearch.viewmodel.ChatSearchViewModel
import javax.inject.Inject

/**
 * @author : Steven 2019-08-06
 */
open class ChatSearchFragment : BaseListFragment<Visitable<*>, ChatSearchTypeFactory>(),
        ChatSearchActivity.Listener, LifecycleOwner, EmptySearchChatViewHolder.Listener,
        ContactLoadMoreViewHolder.Listener, ItemSearchChatReplyViewHolder.Listener {

    @Inject
    lateinit var analytic: ChatSearchAnalytic

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var adapter: ChatSearchAdapter
    private val viewModelFragmentProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val viewModel by lazy { viewModelFragmentProvider.get(ChatSearchViewModel::class.java) }
    private var listener: ChatSearchFragmentListener? = null
    private var alreadyLoaded = false

    override fun getRecyclerViewResourceId() = R.id.recycler_view

    override fun isAutoLoadEnabled(): Boolean = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chat_search, container, false)
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, ChatSearchTypeFactory> {
        this.adapter = ChatSearchAdapter(adapterTypeFactory)
        return this.adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        listener?.showSearchBar()
        updateTouchListener(view)
        if (!alreadyLoaded) {
            super.onViewCreated(view, savedInstanceState)
            showRecentSearch()
            setupRecyclerView()
            setupObserver()
            alreadyLoaded = true
        } else {
            viewModel.resetLiveData()
            setupRecyclerView()
            setupObserver()
        }
    }

    private fun updateTouchListener(view: View) {
        view.setOnTouchListener { _, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN,
                MotionEvent.ACTION_MOVE -> listener?.hideKeyboard()
            }
            false
        }
    }

    override fun onAttachActivity(context: Context?) {
        if (context is ChatSearchFragmentListener) {
            listener = context
        }
    }

    override fun onClickContactLoadMore() {
        listener?.onClickContactLoadMore(viewModel.query, viewModel.firstContactSearchResults)
    }

    override fun onClickChangeKeyword() {
        listener?.onClickChangeKeyword()
    }

    private fun showRecentSearch() {
        val recentSearch = listOf(RecentSearch())
        renderList(recentSearch)
    }

    private fun setupRecyclerView() {
        view?.findViewById<VerticalRecyclerView>(recyclerViewResourceId)?.apply {
            clearItemDecoration()
            adapter = this@ChatSearchFragment.adapter
            overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            clipToPadding = false
            endlessRecyclerViewScrollListener.updateLayoutManager(layoutManager)
            addOnScrollListener(endlessRecyclerViewScrollListener)
        }
    }

    private fun setupObserver() {
        observeLoadInitialData()
        observeEmptyQuery()
        observeErrorSearch()
        observeSearchTriggered()
        observeSearchResult()
    }

    private fun observeLoadInitialData() {
        viewModel.loadInitialData.observe(viewLifecycleOwner, Observer {
            if (!it) return@Observer
            clearAllData()
            showLoading()
        })
    }

    private fun observeEmptyQuery() {
        viewModel.emptyQuery.observe(viewLifecycleOwner, Observer {
            if (!it) return@Observer
            clearAllData()
            showRecentSearch()
        })
    }

    private fun observeErrorSearch() {
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { error ->
            if (error == null) return@Observer
            if (viewModel.isFirstPage()) {
                clearAllData()
            }
            showGetListError(error)
        })
    }

    private fun observeSearchTriggered() {
        viewModel.triggerSearch.observe(viewLifecycleOwner, Observer {
            analytic.eventQueryTriggered()
        })
    }

    private fun observeSearchResult() {
        viewModel.searchResults.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            renderList(it, viewModel.hasNext)
        })
    }

    override fun onSearchQueryChanged(query: String) {
        viewModel.onSearchQueryChanged(query)
    }

    override fun loadData(page: Int) {
        viewModel.loadPage(page)
    }

    override fun getAdapterTypeFactory(): ChatSearchTypeFactory {
        return ChatSearchTypeFactoryImpl(this, this, this)
    }

    override fun onItemClicked(t: Visitable<*>) {}

    override fun getScreenName() = SCREEN_NAME

    override fun initInjector() {
        getComponent(ChatSearchComponent::class.java).inject(this)
    }

    override fun getSearchKeyWord(): String {
        return viewModel.query
    }

    override fun onChatReplyClick(element: ChatReplyUiModel) {
        val chatRoomIntent = RouteManager.getIntent(context, ApplinkConst.TOPCHAT, element.msgId.toString())
        chatRoomIntent.putExtra(ApplinkConst.Chat.SOURCE_PAGE, ApplinkConst.Chat.SOURCE_CHAT_SEARCH)
        chatRoomIntent.putExtra(ApplinkConst.Chat.SEARCH_CREATE_TIME, element.modifiedTimeStamp)
        chatRoomIntent.putExtra(ApplinkConst.Chat.SEARCH_PRODUCT_KEYWORD, getSearchKeyWord())
        startActivity(chatRoomIntent)
    }

    companion object {
        const val OPEN_DETAIL_MESSAGE = 1324
        const val SCREEN_NAME = "chat search"
        private const val CHAT_TAB_TITLE = "chat_tab_title"

        fun createFragment(): ChatSearchFragment {
            return ChatSearchFragment()
        }

    }
}