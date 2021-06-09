package com.tokopedia.inbox.fake

import com.tokopedia.inbox.common.AndroidFileUtil
import com.tokopedia.inbox.fake.common.FakeUserSession
import com.tokopedia.inbox.fake.domain.chat.websocket.FakeTopchatWebSocket
import com.tokopedia.inbox.fake.domain.usecase.chat.FakeGetChatListMessageUseCase
import com.tokopedia.inbox.test.R
import com.tokopedia.topchat.chatlist.pojo.ChatListPojo
import javax.inject.Inject

/**
 * All chatlist fake dependency on new inbox
 */
class InboxChatFakeDependency @Inject constructor(
        val userSession: FakeUserSession,
        val ws: FakeTopchatWebSocket,
        val getChatList: FakeGetChatListMessageUseCase
) {

    var getChatList_EmptyBuyerResponse = ChatListPojo()
    var getChatList_BuyerSize3Response = ChatListPojo()

    fun init() {
        initResponse()
    }

    private fun initResponse() {
        getChatList_EmptyBuyerResponse = AndroidFileUtil.parseRaw(
                R.raw.chat_list_buyer_empty, ChatListPojo::class.java
        )
        getChatList_BuyerSize3Response = AndroidFileUtil.parseRaw(
                R.raw.chat_list_buyer_normal_size_3, ChatListPojo::class.java
        )
    }
}