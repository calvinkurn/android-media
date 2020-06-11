package com.tokopedia.topchat.chatsearch.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.graphql.CommonUtils
import com.tokopedia.topchat.chatsearch.data.SearchResult
import com.tokopedia.topchat.chatsearch.view.adapter.ChatSearchTypeFactory
import com.tokopedia.topchat.chatsearch.view.adapter.ChatSearchTypeFactoryImpl

class ContactLoadMoreChatFragment : BaseListFragment<Visitable<*>, ChatSearchTypeFactory>() {

    private var listener: ContactLoadMoreChatListener? = null
    private var query: String = ""
    private var firstResponse: List<SearchResult> = emptyList()

    override fun onAttachActivity(context: Context?) {
        if (context is ContactLoadMoreChatListener) {
            listener = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initArguments()
        initToolbarTittle()
        renderList(firstResponse)
    }

    private fun initArguments() {
        query = arguments?.getString(KEY_QUERY) ?: ""
        val stringFirstResponse = arguments?.getString(KEY_FIRST_PAGE_RESPONSE) ?: ""
        firstResponse = convertFirstResponseToObject(stringFirstResponse)
    }

    private fun convertFirstResponseToObject(stringFirstResponse: String): List<SearchResult> {
        return try {
            val listType = object : TypeToken<List<SearchResult>>() {}.type
            CommonUtils.fromJson(stringFirstResponse, listType)
        } catch (e: Exception) {
            emptyList()
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

    }

    override fun onItemClicked(t: Visitable<*>?) {

    }

    override fun loadData(page: Int) {

    }

    companion object {
        private const val KEY_QUERY = "key_query"
        private const val KEY_FIRST_PAGE_RESPONSE = "key_first_page_response"
        fun create(query: String, firstPageContacts: List<SearchResult>): ContactLoadMoreChatFragment {
            return ContactLoadMoreChatFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_QUERY, query)
                    putString(KEY_FIRST_PAGE_RESPONSE, CommonUtils.toJson(firstPageContacts))
                }
            }
        }
    }
}