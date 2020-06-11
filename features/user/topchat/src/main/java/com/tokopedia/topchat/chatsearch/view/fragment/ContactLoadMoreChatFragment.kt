package com.tokopedia.topchat.chatsearch.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.topchat.chatsearch.view.adapter.ChatSearchTypeFactory
import com.tokopedia.topchat.chatsearch.view.adapter.ChatSearchTypeFactoryImpl

class ContactLoadMoreChatFragment : BaseListFragment<Visitable<*>, ChatSearchTypeFactory>() {

    private var listener: ContactLoadMoreChatListener? = null
    private var query: String = ""

    override fun onAttachActivity(context: Context?) {
        if (context is ContactLoadMoreChatListener) {
            listener = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initArguments()
        initToolbarTittle()
    }

    private fun initArguments() {
        query = arguments?.getString(KEY_QUERY) ?: ""
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
        fun create(query: String): ContactLoadMoreChatFragment {
            return ContactLoadMoreChatFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_QUERY, query)
                }
            }
        }
    }
}