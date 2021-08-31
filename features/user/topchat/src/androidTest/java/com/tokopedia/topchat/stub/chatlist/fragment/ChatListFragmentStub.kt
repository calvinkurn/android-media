package com.tokopedia.topchat.stub.chatlist.fragment

import android.content.Intent
import android.os.Bundle
import com.tokopedia.topchat.chatlist.di.ChatListContextModule
import com.tokopedia.topchat.chatlist.fragment.ChatListFragment
import com.tokopedia.topchat.chatlist.pojo.ItemChatListPojo
import com.tokopedia.topchat.common.TopChatInternalRouter
import com.tokopedia.topchat.stub.chatlist.activity.TopChatRoomActivityForChatListStub
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

    override fun chatItemClicked(
        element: ItemChatListPojo,
        itemPosition: Int,
        lastActiveChat: Pair<ItemChatListPojo?, Int?>
    ) {
        activity?.let {
            val intent = Intent(it, TopChatRoomActivityForChatListStub::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            intent.putExtra(TopChatInternalRouter.Companion.RESULT_INBOX_CHAT_PARAM_INDEX, itemPosition)
            this@ChatListFragmentStub.startActivityForResult(intent, OPEN_DETAIL_MESSAGE)
            it.overridePendingTransition(0, 0)
        }
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