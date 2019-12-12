package com.tokopedia.topchat.chatsearch.view.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView
import com.tokopedia.topchat.chatsearch.di.ChatSearchComponent
import com.tokopedia.topchat.chatsearch.view.activity.ChatSearchActivity
import com.tokopedia.topchat.chatsearch.view.adapter.ChatSearchTypeFactory
import com.tokopedia.topchat.chatsearch.view.adapter.ChatSearchTypeFactoryImpl
import com.tokopedia.topchat.chatsearch.view.adapter.viewholder.ItemSearchChatViewHolder
import com.tokopedia.topchat.chatsearch.viewmodel.ChatSearchViewModel
import com.tokopedia.unifycomponents.toPx
import javax.inject.Inject

/**
 * @author : Steven 2019-08-06
 */
class ChatSearchFragment : BaseListFragment<Visitable<*>, ChatSearchTypeFactory>(),
        ChatSearchActivity.Listener, LifecycleOwner, ItemSearchChatViewHolder.Listener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelFragmentProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val viewModel by lazy { viewModelFragmentProvider.get(ChatSearchViewModel::class.java) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderList(emptyList())
        setupRecyclerView()
        setupObserver()
    }

    private fun setupRecyclerView() {
        view?.findViewById<VerticalRecyclerView>(recyclerViewResourceId)?.apply {
            clearItemDecoration()
            overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            setPadding(paddingLeft, 16.toPx(), paddingRight, 16.toPx())
            clipToPadding = false
        }
    }

    private fun setupObserver() {
        viewModel.searchResult.observe(viewLifecycleOwner, Observer {
            renderList(it, viewModel.hasNext)
        })
        viewModel.loadInitialData.observe(viewLifecycleOwner, Observer {
            if (!it) return@Observer
            clearAllData()
            showLoading()
        })
        viewModel.showEmpty.observe(viewLifecycleOwner, Observer {
            if (!it) return@Observer
            clearAllData()
            showEmpty()
        })
    }

    override fun onSearchQueryChanged(query: String) {
        viewModel.onSearchQueryChanged(query)
    }

    override fun loadData(page: Int) {
        viewModel.loadNextPage(page)
    }

    override fun getAdapterTypeFactory(): ChatSearchTypeFactory {
        return ChatSearchTypeFactoryImpl(this)
    }

    override fun onItemClicked(t: Visitable<*>) {}

    override fun getScreenName() = SCREEN_NAME

    override fun initInjector() {
        getComponent(ChatSearchComponent::class.java).inject(this)
    }

    override fun finishSearchActivity() {
        activity?.finish()
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