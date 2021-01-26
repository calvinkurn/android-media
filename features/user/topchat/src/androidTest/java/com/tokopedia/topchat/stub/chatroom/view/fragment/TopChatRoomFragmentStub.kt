package com.tokopedia.topchat.stub.chatroom.view.fragment

import android.os.Bundle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.chat_common.BaseChatFragment
import com.tokopedia.topchat.chatroom.di.ChatRoomContextModule
import com.tokopedia.topchat.chatroom.domain.usecase.ChatAttachmentUseCase
import com.tokopedia.topchat.chatroom.domain.usecase.GetChatUseCase
import com.tokopedia.topchat.chatroom.view.fragment.TopChatRoomFragment
import com.tokopedia.topchat.stub.chatroom.di.ChatListFakeUseCaseModule
import com.tokopedia.topchat.stub.chatroom.di.ChatModuleStub
import com.tokopedia.topchat.stub.chatroom.di.ChatNetworkModuleStub
import com.tokopedia.topchat.stub.chatroom.di.DaggerChatComponentStub

class TopChatRoomFragmentStub : TopChatRoomFragment() {

    private lateinit var getChatUseCase: GetChatUseCase
    private lateinit var chatAttachmentUseCase: ChatAttachmentUseCase

    override fun initInjector() {
        DaggerChatComponentStub
                .builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .chatModuleStub(createChatModuleStub())
                .chatNetworkModuleStub(createChatNetworkStub())
                .chatRoomContextModule(ChatRoomContextModule(context!!))
                .chatListFakeUseCaseModule(createChatListFakeUseCaseModule())
                .build()
                .inject(this)
    }

    private fun createChatListFakeUseCaseModule(): ChatListFakeUseCaseModule {
        return ChatListFakeUseCaseModule(
                getChatUseCase,
                chatAttachmentUseCase
        )
    }

    private fun createChatModuleStub(): ChatModuleStub {
        return ChatModuleStub()
    }

    private fun createChatNetworkStub(): ChatNetworkModuleStub {
        return ChatNetworkModuleStub()
    }

    companion object {
        fun createInstance(
                bundle: Bundle,
                getChatUseCase: GetChatUseCase,
                chatAttachmentUseCase: ChatAttachmentUseCase
        ): BaseChatFragment {
            return TopChatRoomFragmentStub().apply {
                arguments = bundle
                this.getChatUseCase = getChatUseCase
                this.chatAttachmentUseCase = chatAttachmentUseCase
            }
        }
    }

    override fun showErrorWebSocket(isWebSocketError: Boolean) {

    }
}