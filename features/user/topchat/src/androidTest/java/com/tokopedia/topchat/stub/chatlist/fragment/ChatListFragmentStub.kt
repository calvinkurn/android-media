package com.tokopedia.topchat.stub.chatlist.fragment

import android.os.Bundle
import com.tokopedia.topchat.chatlist.di.ChatListContextModule
import com.tokopedia.topchat.chatlist.fragment.ChatListFragment
import com.tokopedia.topchat.stub.chatlist.di.DaggerChatListComponentStub
import com.tokopedia.topchat.stub.chatlist.di.module.ChatListNetworkModuleStub
import com.tokopedia.topchat.stub.chatlist.di.module.ChatListQueryModuleStub
import com.tokopedia.topchat.stub.chatlist.usecase.GetChatListMessageUseCaseStub
import com.tokopedia.topchat.stub.chatlist.usecase.GetChatNotificationUseCaseStub
import com.tokopedia.topchat.stub.common.di.DaggerFakeBaseAppComponent
import com.tokopedia.topchat.stub.common.di.module.FakeAppModule
import com.tokopedia.user.session.UserSessionInterface

class ChatListFragmentStub : ChatListFragment() {

    lateinit var stubUserSession: UserSessionInterface
    lateinit var chatListUseCase: GetChatListMessageUseCaseStub
    lateinit var chatNotificationUseCase: GetChatNotificationUseCaseStub

    override fun initInjector() {
        val baseComponent = DaggerFakeBaseAppComponent.builder()
                .fakeAppModule(FakeAppModule(context!!.applicationContext))
                .build()
        DaggerChatListComponentStub
                .builder()
                .fakeBaseAppComponent(baseComponent)
                .chatListContextModule(ChatListContextModule(context!!))
                .chatListNetworkModuleStub(ChatListNetworkModuleStub(stubUserSession))
                .chatListQueryModuleStub(ChatListQueryModuleStub(chatListUseCase, chatNotificationUseCase))
                .build()
                .inject(this)
    }

    /**
     * Workaround for [com.tokopedia.unifycomponents.LoaderUnify] doesn't support
     * for no animation setting. This prevent the adapter showing loading state.
     * TODO: Ask unify team to add support for no animation device
     */
    override fun showLoading() {
        adapter?.removeErrorNetwork()
        hideSnackBarRetry()
    }

    companion object {
        fun createFragment(
                title: String,
                userSessionInterface: UserSessionInterface,
                chatListUseCaseStub: GetChatListMessageUseCaseStub,
                chatNotificationUseCaseStub: GetChatNotificationUseCaseStub
        ): ChatListFragment {
            val bundle = Bundle().apply {
                putString(CHAT_TAB_TITLE, title)
            }
            return ChatListFragmentStub().apply {
                arguments = bundle
                stubUserSession = userSessionInterface
                chatListUseCase = chatListUseCaseStub
                chatNotificationUseCase = chatNotificationUseCaseStub
            }
        }
    }
}