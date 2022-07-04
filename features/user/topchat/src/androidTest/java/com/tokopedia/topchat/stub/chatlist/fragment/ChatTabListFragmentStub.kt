package com.tokopedia.topchat.stub.chatlist.fragment

import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant
import com.tokopedia.topchat.chatlist.di.ChatListContextModule
import com.tokopedia.topchat.chatlist.view.fragment.ChatListFragment
import com.tokopedia.topchat.chatlist.view.fragment.ChatTabListFragment
import com.tokopedia.topchat.stub.chatlist.di.DaggerChatListComponentStub
import com.tokopedia.topchat.stub.chatlist.di.module.ChatListNetworkModuleStub
import com.tokopedia.topchat.stub.chatlist.di.module.ChatListQueryModuleStub
import com.tokopedia.topchat.stub.chatlist.usecase.GetChatListMessageUseCaseStub
import com.tokopedia.topchat.stub.chatlist.usecase.GetChatNotificationUseCaseStub
import com.tokopedia.topchat.stub.chatlist.usecase.GetChatWhitelistFeatureStub
import com.tokopedia.topchat.stub.common.di.DaggerFakeBaseAppComponent
import com.tokopedia.topchat.stub.common.di.module.FakeAppModule
import com.tokopedia.user.session.UserSessionInterface

class ChatTabListFragmentStub : ChatTabListFragment() {

    lateinit var userSessionStub: UserSessionInterface
    lateinit var chatListUseCaseStub: GetChatListMessageUseCaseStub
    lateinit var chatNotificationUseCaseStub: GetChatNotificationUseCaseStub
    lateinit var chatWhitelistFeatureStub: GetChatWhitelistFeatureStub

    override fun initInjector() {
        val baseComponent = DaggerFakeBaseAppComponent.builder()
                .fakeAppModule(FakeAppModule(context!!.applicationContext))
                .build()
        DaggerChatListComponentStub
                .builder()
                .fakeBaseAppComponent(baseComponent)
                .chatListContextModule(ChatListContextModule(context!!))
                .chatListNetworkModuleStub(ChatListNetworkModuleStub(userSessionStub))
                .chatListQueryModuleStub(ChatListQueryModuleStub(
                    chatListUseCaseStub, chatNotificationUseCaseStub, chatWhitelistFeatureStub
                ))
                .build()
                .inject(this)
    }

    override fun createSellerTabFragment(): ChatListFragment {
        return ChatListFragmentStub.createFragment(
                ChatListQueriesConstant.PARAM_TAB_SELLER,
                userSessionStub,
                chatListUseCaseStub,
                chatNotificationUseCaseStub,
                chatWhitelistFeatureStub
        )
    }

    override fun createBuyerTabFragment(): ChatListFragment {
        return ChatListFragmentStub.createFragment(
                ChatListQueriesConstant.PARAM_TAB_USER,
                userSessionStub,
                chatListUseCaseStub,
                chatNotificationUseCaseStub,
                chatWhitelistFeatureStub
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
                chatNotificationUseCase: GetChatNotificationUseCaseStub,
                chatWhitelistFeature: GetChatWhitelistFeatureStub
        ): ChatTabListFragmentStub {
            return ChatTabListFragmentStub().apply {
                userSessionStub = userSessionInterface
                chatListUseCaseStub = chatListUseCase
                chatNotificationUseCaseStub = chatNotificationUseCase
                chatWhitelistFeatureStub = chatWhitelistFeature
            }
        }
    }
}