package com.tokopedia.topchat.stub.chatroom.view.activity

import android.os.Bundle
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.chat_common.BaseChatFragment
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.topchat.AndroidFileUtil
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ChatAttachmentResponse
import com.tokopedia.topchat.chatroom.domain.usecase.ChatAttachmentUseCase
import com.tokopedia.topchat.chatroom.domain.usecase.GetChatUseCase
import com.tokopedia.topchat.chatroom.view.activity.TopChatRoomActivity
import com.tokopedia.topchat.stub.chatroom.usecase.ChatAttachmentUseCaseStub
import com.tokopedia.topchat.stub.chatroom.usecase.GetChatUseCaseStub
import com.tokopedia.topchat.stub.chatroom.view.fragment.TopChatRoomFragmentStub

class TopChatRoomActivityStub : TopChatRoomActivity() {

    private lateinit var getChatUseCase: GetChatUseCase
    private lateinit var chatAttachmentUseCase: ChatAttachmentUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val source = intent.getStringExtra(ApplinkConst.Chat.SOURCE_PAGE)
        if(source != null && source == ApplinkConst.Chat.SOURCE_CHAT_SEARCH) {
            setupFromSearch()
        }
    }

    override fun inflateFragment() {
        // Don't inflate fragment immediately
    }

    fun setupTestFragment(
            getChatUseCase: GetChatUseCase,
            chatAttachmentUseCase: ChatAttachmentUseCase
    ) {
        this.getChatUseCase = getChatUseCase
        this.chatAttachmentUseCase = chatAttachmentUseCase
        supportFragmentManager.beginTransaction()
                .replace(parentViewResourceID, newFragment, tagFragment)
                .commit()
    }

    override fun createChatRoomFragment(bundle: Bundle): BaseChatFragment {
        return TopChatRoomFragmentStub.createInstance(
                bundle,
                getChatUseCase,
                chatAttachmentUseCase
        )
    }

    private fun setupFromSearch() {
        var firstPageChat: GetExistingChatPojo = AndroidFileUtil.parse(
                "success_get_chat_first_page.json",
                GetExistingChatPojo::class.java
        )
        var chatAttachmentResponse: ChatAttachmentResponse = AndroidFileUtil.parse(
                "success_get_chat_attachments.json",
                ChatAttachmentResponse::class.java
        )
        var chatUseCase = GetChatUseCaseStub()
        var chatAttachmentUseCase = ChatAttachmentUseCaseStub()
        chatUseCase.response = firstPageChat
        chatAttachmentUseCase.response = chatAttachmentResponse
        setupTestFragment(chatUseCase, chatAttachmentUseCase)
    }
}