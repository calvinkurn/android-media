package com.tokopedia.topchat.stub.chatroom.view.fragment

import android.os.Bundle
import android.util.Log
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.topchat.chatroom.di.ChatRoomContextModule
import com.tokopedia.topchat.chatroom.domain.usecase.ChatAttachmentUseCase
import com.tokopedia.topchat.chatroom.domain.usecase.GetChatUseCase
import com.tokopedia.topchat.chatroom.view.fragment.TopChatRoomFragment
import com.tokopedia.topchat.stub.chatroom.di.*
import com.tokopedia.topchat.stub.chatroom.websocket.RxWebSocketUtilStub

class TopChatRoomFragmentStub : TopChatRoomFragment() {

    private lateinit var getChatUseCase: GetChatUseCase
    private lateinit var chatAttachmentUseCase: ChatAttachmentUseCase
    private lateinit var websocket: RxWebSocketUtilStub

    override fun initInjector() {
        Log.d("DEBUG_TEXT", "initInjector ${Thread.currentThread().id}")
        DaggerChatComponentStub
                .builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .chatModuleStub(createChatModuleStub())
                .chatNetworkModuleStub(createChatNetworkStub())
                .chatRoomContextModule(ChatRoomContextModule(context!!))
                .chatRoomFakePresenterModule(ChatRoomFakePresenterModule())
                .chatRoomFakeUseCaseModule(createChatRoomFakeUseCaseModule())
                .build()
                .inject(this)
    }

    private fun createChatRoomFakeUseCaseModule(): ChatRoomFakeUseCaseModule {
        return ChatRoomFakeUseCaseModule(
                getChatUseCase,
                chatAttachmentUseCase
        )
    }

    private fun createChatModuleStub(): ChatModuleStub {
        return ChatModuleStub(websocket)
    }

    private fun createChatNetworkStub(): ChatNetworkModuleStub {
        return ChatNetworkModuleStub()
    }

    companion object {
        fun createInstance(
                bundle: Bundle,
                getChatUseCase: GetChatUseCase,
                chatAttachmentUseCase: ChatAttachmentUseCase,
                websocket: RxWebSocketUtilStub
        ): TopChatRoomFragmentStub {
            return TopChatRoomFragmentStub().apply {
                arguments = bundle
                this.getChatUseCase = getChatUseCase
                this.chatAttachmentUseCase = chatAttachmentUseCase
                this.websocket = websocket
            }
        }
    }

    override fun showErrorWebSocket(isWebSocketError: Boolean) {

    }
}