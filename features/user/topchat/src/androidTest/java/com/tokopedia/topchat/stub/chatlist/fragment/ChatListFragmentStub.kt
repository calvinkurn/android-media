package com.tokopedia.topchat.stub.chatlist.fragment

import android.os.Bundle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.topchat.chatlist.di.ChatListContextModule
import com.tokopedia.topchat.chatlist.fragment.ChatListFragment
import com.tokopedia.topchat.chatlist.pojo.ChatListPojo
import com.tokopedia.topchat.stub.chatlist.di.DaggerChatListComponentStub
import com.tokopedia.topchat.stub.chatlist.di.module.ChatListNetworkModuleStub
import com.tokopedia.topchat.stub.chatlist.di.module.ChatListQueryModuleStub
import com.tokopedia.user.session.UserSessionInterface

class ChatListFragmentStub: ChatListFragment() {

    lateinit var stubUserSession: UserSessionInterface
    lateinit var chatListUseCase: GraphqlUseCase<ChatListPojo>

    override fun initInjector() {
        DaggerChatListComponentStub
                .builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .chatListContextModule(ChatListContextModule(context!!))
                .chatListNetworkModuleStub(ChatListNetworkModuleStub(stubUserSession))
                .chatListQueryModuleStub(ChatListQueryModuleStub(chatListUseCase))
                .build()
                .inject(this)
    }

    companion object {
        fun createFragment(
                title: String,
                userSessionInterface: UserSessionInterface,
                chatListUseCaseStub: GraphqlUseCase<ChatListPojo>
        ): ChatListFragment {
            val bundle = Bundle().apply {
                putString(CHAT_TAB_TITLE, title)
            }
            return ChatListFragmentStub().apply {
                arguments = bundle
                stubUserSession = userSessionInterface
                chatListUseCase = chatListUseCaseStub
            }
        }
    }
}