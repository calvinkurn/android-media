package com.tokopedia.topchat.stub.chatroom.view.activity

import android.os.Bundle
import com.tokopedia.chat_common.BaseChatFragment
import com.tokopedia.topchat.chatroom.domain.usecase.ChatAttachmentUseCase
import com.tokopedia.topchat.chatroom.domain.usecase.GetChatUseCase
import com.tokopedia.topchat.chatroom.view.activity.TopChatRoomActivity
import com.tokopedia.topchat.stub.chatroom.view.fragment.TopChatRoomFragmentStub
import com.tokopedia.topchat.stub.chatroom.websocket.RxWebSocketUtilStub

class TopChatRoomActivityStub : TopChatRoomActivity() {

    private lateinit var getChatUseCase: GetChatUseCase
    private lateinit var chatAttachmentUseCase: ChatAttachmentUseCase
    private lateinit var websocket: RxWebSocketUtilStub

    override fun inflateFragment() {
        // Don't inflate fragment immediately
    }

    fun setupTestFragment(
            getChatUseCase: GetChatUseCase,
            chatAttachmentUseCase: ChatAttachmentUseCase,
            websocket: RxWebSocketUtilStub
    ) {
        this.getChatUseCase = getChatUseCase
        this.chatAttachmentUseCase = chatAttachmentUseCase
        this.websocket = websocket
        supportFragmentManager.beginTransaction()
                .replace(parentViewResourceID, newFragment, TAG)
                .commit()
    }

    override fun getTagFragment(): String {
        return TAG
    }

    override fun createChatRoomFragment(bundle: Bundle): BaseChatFragment {
        return TopChatRoomFragmentStub.createInstance(
                bundle,
                getChatUseCase,
                chatAttachmentUseCase,
                websocket
        )
    }

    companion object {
        const val TAG = "chatroom-tag"
    }
}