package com.tokopedia.topchat.stub.chatlist.fragment

import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant
import com.tokopedia.topchat.chatlist.di.ChatListContextModule
import com.tokopedia.topchat.chatlist.fragment.ChatListFragment
import com.tokopedia.topchat.chatlist.fragment.ChatTabListFragment
import com.tokopedia.topchat.stub.chatlist.di.DaggerChatListComponentStub
import com.tokopedia.topchat.stub.chatlist.di.module.ChatListNetworkModuleStub
import com.tokopedia.user.session.UserSessionInterface

class ChatTabListFragmentStub : ChatTabListFragment() {

    lateinit var stubUserSession: UserSessionInterface

    override fun initInjector() {
        DaggerChatListComponentStub
                .builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .chatListContextModule(ChatListContextModule(context!!))
                .chatListNetworkModuleStub(ChatListNetworkModuleStub(stubUserSession))
                .build()
                .inject(this)
    }

    override fun createSellerTabFragment(): ChatListFragment {
        return ChatListFragmentStub.createFragment(ChatListQueriesConstant.PARAM_TAB_SELLER)
    }

    override fun createBuyerTabFragment(): ChatListFragment {
        return ChatListFragmentStub.createFragment(ChatListQueriesConstant.PARAM_TAB_USER)
    }

    companion object {
        fun create(
                userSessionInterface: UserSessionInterface
        ): ChatTabListFragmentStub {
            return ChatTabListFragmentStub().apply {
                stubUserSession = userSessionInterface
            }
        }
    }
}