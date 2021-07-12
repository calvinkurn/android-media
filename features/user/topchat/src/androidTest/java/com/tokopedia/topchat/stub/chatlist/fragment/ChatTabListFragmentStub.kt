package com.tokopedia.topchat.stub.chatlist.fragment

import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant
import com.tokopedia.topchat.chatlist.di.ChatListContextModule
import com.tokopedia.topchat.chatlist.fragment.ChatListFragment
import com.tokopedia.topchat.chatlist.fragment.ChatTabListFragment
import com.tokopedia.topchat.stub.chatlist.di.DaggerChatListComponentStub
import com.tokopedia.topchat.stub.chatlist.di.module.ChatListNetworkModuleStub
import com.tokopedia.topchat.stub.chatlist.di.module.ChatListQueryModuleStub
import com.tokopedia.topchat.stub.chatlist.usecase.GetChatListMessageUseCaseStub
import com.tokopedia.topchat.stub.chatlist.usecase.GetChatNotificationUseCaseStub
import com.tokopedia.topchat.stub.common.di.DaggerFakeBaseAppComponent
import com.tokopedia.topchat.stub.common.di.module.FakeAppModule
import com.tokopedia.user.session.UserSessionInterface

class ChatTabListFragmentStub : ChatTabListFragment() {

    lateinit var userSessionStub: UserSessionInterface
    lateinit var chatListUseCaseStub: GetChatListMessageUseCaseStub
    lateinit var chatNotificationUseCaseStub: GetChatNotificationUseCaseStub

    override fun initInjector() {
        val baseComponent = DaggerFakeBaseAppComponent.builder()
                .fakeAppModule(FakeAppModule(context!!.applicationContext))
                .build()
        DaggerChatListComponentStub
                .builder()
                .fakeBaseAppComponent(baseComponent)
                .chatListContextModule(ChatListContextModule(context!!))
                .chatListNetworkModuleStub(ChatListNetworkModuleStub(userSessionStub))
                .chatListQueryModuleStub(ChatListQueryModuleStub(chatListUseCaseStub, chatNotificationUseCaseStub))
                .build()
                .inject(this)
    }

    override fun createSellerTabFragment(): ChatListFragment {
        return ChatListFragmentStub.createFragment(
                ChatListQueriesConstant.PARAM_TAB_SELLER,
                userSessionStub,
                chatListUseCaseStub,
                chatNotificationUseCaseStub
        )
    }

    override fun createBuyerTabFragment(): ChatListFragment {
        return ChatListFragmentStub.createFragment(
                ChatListQueriesConstant.PARAM_TAB_USER,
                userSessionStub,
                chatListUseCaseStub,
                chatNotificationUseCaseStub
        )
    }

    override fun initToolTip() {
        // Don't initialize tool-tip
    }

    override fun isOnBoardingAlreadyShown(): Boolean {
        return true // Don't show onBoarding
    }

    companion object {
        fun create(
                userSessionInterface: UserSessionInterface,
                chatListUseCase: GetChatListMessageUseCaseStub,
                chatNotificationUseCase: GetChatNotificationUseCaseStub
        ): ChatTabListFragmentStub {
            return ChatTabListFragmentStub().apply {
                userSessionStub = userSessionInterface
                chatListUseCaseStub = chatListUseCase
                chatNotificationUseCaseStub = chatNotificationUseCase
            }
        }
    }
}