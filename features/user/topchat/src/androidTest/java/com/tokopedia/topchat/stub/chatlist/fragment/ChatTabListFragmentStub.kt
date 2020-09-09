package com.tokopedia.topchat.stub.chatlist.fragment

import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant
import com.tokopedia.topchat.chatlist.di.ChatListContextModule
import com.tokopedia.topchat.chatlist.fragment.ChatListFragment
import com.tokopedia.topchat.chatlist.fragment.ChatTabListFragment
import com.tokopedia.topchat.chatlist.pojo.ChatListPojo
import com.tokopedia.topchat.stub.chatlist.di.DaggerChatListComponentStub
import com.tokopedia.topchat.stub.chatlist.di.module.ChatListNetworkModuleStub
import com.tokopedia.topchat.stub.chatlist.di.module.ChatListQueryModuleStub
import com.tokopedia.user.session.UserSessionInterface

class ChatTabListFragmentStub : ChatTabListFragment() {

    lateinit var userSessionStub: UserSessionInterface
    lateinit var chatListUseCaseStub: GraphqlUseCase<ChatListPojo>

    override fun initInjector() {
        DaggerChatListComponentStub
                .builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .chatListContextModule(ChatListContextModule(context!!))
                .chatListNetworkModuleStub(ChatListNetworkModuleStub(userSessionStub))
                .chatListQueryModuleStub(ChatListQueryModuleStub(chatListUseCaseStub))
                .build()
                .inject(this)
    }

    override fun createSellerTabFragment(): ChatListFragment {
        return ChatListFragmentStub.createFragment(
                ChatListQueriesConstant.PARAM_TAB_SELLER,
                userSessionStub,
                chatListUseCaseStub
        )
    }

    override fun createBuyerTabFragment(): ChatListFragment {
        return ChatListFragmentStub.createFragment(
                ChatListQueriesConstant.PARAM_TAB_USER,
                userSessionStub,
                chatListUseCaseStub
        )
    }

    override fun initToolTip() {
        // Dont initialize tool-tip
    }

    companion object {
        fun create(
                userSessionInterface: UserSessionInterface,
                chatListUseCase: GraphqlUseCase<ChatListPojo>
        ): ChatTabListFragmentStub {
            return ChatTabListFragmentStub().apply {
                userSessionStub = userSessionInterface
                chatListUseCaseStub = chatListUseCase
            }
        }
    }
}