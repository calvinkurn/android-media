package com.tokopedia.topchat.chatsearch.view.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.topchat.chatsearch.view.adapter.ChatSearchTypeFactory
import com.tokopedia.topchat.chatsearch.view.adapter.ChatSearchTypeFactoryImpl

class ContactLoadMoreChatFragment : BaseListFragment<Visitable<*>, ChatSearchTypeFactory>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Toast.makeText(context, "load more", Toast.LENGTH_SHORT).show()
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
        fun create(): ContactLoadMoreChatFragment {
            return ContactLoadMoreChatFragment()
        }
    }
}