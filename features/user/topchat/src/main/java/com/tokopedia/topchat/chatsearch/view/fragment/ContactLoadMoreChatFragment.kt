package com.tokopedia.topchat.chatsearch.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView
import com.tokopedia.graphql.CommonUtils
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatsearch.data.GetChatSearchResponse
import com.tokopedia.topchat.chatsearch.di.ChatSearchComponent
import com.tokopedia.topchat.chatsearch.view.adapter.ChatSearchTypeFactory
import com.tokopedia.topchat.chatsearch.view.adapter.ChatSearchTypeFactoryImpl
import com.tokopedia.topchat.chatsearch.view.uimodel.SearchListHeaderUiModel
import com.tokopedia.topchat.chatsearch.viewmodel.ChatContactLoadMoreViewModel
import javax.inject.Inject

class ContactLoadMoreChatFragment : BaseListFragment<Visitable<*>, ChatSearchTypeFactory>() {

    private var listener: ContactLoadMoreChatListener? = null
    private var query: String = ""
    private var firstResponse: GetChatSearchResponse = GetChatSearchResponse()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelFragmentProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val viewModel by lazy { viewModelFragmentProvider.get(ChatContactLoadMoreViewModel::class.java) }

    override fun isAutoLoadEnabled(): Boolean = true
    override fun getRecyclerViewResourceId(): Int = R.id.recycler_view

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chat_contact_load_more_fragment, container, false)
    }

    override fun onAttachActivity(context: Context?) {
        if (context is ContactLoadMoreChatListener) {
            listener = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initArguments()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        initToolbarTittle()
        setupObserver()
    }

    override fun loadData(page: Int) {
        viewModel.loadSearchResult(page, query, firstResponse)
    }

    private fun setupRecyclerView() {
        view?.findViewById<VerticalRecyclerView>(recyclerViewResourceId)?.apply {
            clearItemDecoration()
            overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            clipToPadding = false
        }
    }

    private fun setupObserver() {
        setupSearchResultContactObserver()
        setupErrorObserver()
    }

    private fun setupSearchResultContactObserver() {
        viewModel.searchResult.observe(this, Observer {
            if (viewModel.isFirstPage()) {
                val header = SearchListHeaderUiModel(SearchListHeaderUiModel.TITLE_CONTACT, it.contactCount, true)
                val results: MutableList<Visitable<*>> = it.searchResults.toMutableList()
                results.add(0, header)
                renderList(results, it.hasNext)
            } else {
                renderList(it.searchResults, it.hasNext)
            }
        })
    }

    private fun setupErrorObserver() {
        viewModel.errorSearchResults.observe(this, Observer {
            showGetListError(it)
        })
    }

    private fun initArguments() {
        query = arguments?.getString(KEY_QUERY) ?: ""
        val stringFirstResponse = arguments?.getString(KEY_FIRST_PAGE_RESPONSE) ?: ""
        firstResponse = convertFirstResponseToObject(stringFirstResponse)
    }

    private fun convertFirstResponseToObject(stringFirstResponse: String): GetChatSearchResponse {
        return try {
            CommonUtils.fromJson(stringFirstResponse, GetChatSearchResponse::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            firstResponse
        }
    }

    private fun initToolbarTittle() {
        listener?.changeToolbarTitle(query)
    }

    override fun getAdapterTypeFactory(): ChatSearchTypeFactory {
        return ChatSearchTypeFactoryImpl()
    }

    override fun getScreenName(): String {
        return "contact load more"
    }

    override fun initInjector() {
        getComponent(ChatSearchComponent::class.java).inject(this)
    }

    override fun onItemClicked(t: Visitable<*>?) {

    }

    companion object {
        private const val KEY_QUERY = "key_query"
        private const val KEY_FIRST_PAGE_RESPONSE = "key_first_page_response"
        fun create(query: String, firstPageContacts: GetChatSearchResponse): ContactLoadMoreChatFragment {
            return ContactLoadMoreChatFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_QUERY, query)
                    putString(KEY_FIRST_PAGE_RESPONSE, CommonUtils.toJson(firstPageContacts))
                }
            }
        }
    }
}