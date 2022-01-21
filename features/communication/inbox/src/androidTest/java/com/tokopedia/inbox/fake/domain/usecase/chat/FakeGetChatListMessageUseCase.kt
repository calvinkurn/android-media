package com.tokopedia.inbox.fake.domain.usecase.chat

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.inbox.fake.common.FakeGraphqlUseCase
import com.tokopedia.topchat.chatlist.domain.mapper.GetChatListMessageMapper
import com.tokopedia.topchat.chatlist.pojo.ChatListPojo
import com.tokopedia.topchat.chatlist.usecase.GetChatListMessageUseCase
import javax.inject.Inject

class FakeGetChatListMessageUseCase @Inject constructor(
        private val gqlUseCase: FakeGraphqlUseCase<ChatListPojo>,
        mapper: GetChatListMessageMapper,
        dispatchers: CoroutineDispatchers
) : GetChatListMessageUseCase(
        gqlUseCase, mapper, dispatchers) {

    var response = ChatListPojo()
        set(value) {
            field = value
            gqlUseCase.response = value
        }

    init {
        response = response
    }
}