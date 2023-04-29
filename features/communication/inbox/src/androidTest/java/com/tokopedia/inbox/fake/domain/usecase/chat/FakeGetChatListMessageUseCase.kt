package com.tokopedia.inbox.fake.domain.usecase.chat

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topchat.chatlist.domain.mapper.GetChatListMessageMapper
import com.tokopedia.topchat.chatlist.domain.pojo.ChatListParam
import com.tokopedia.topchat.chatlist.domain.pojo.ChatListPojo
import com.tokopedia.topchat.chatlist.domain.pojo.ChatListResponse
import com.tokopedia.topchat.chatlist.domain.usecase.GetChatListMessageUseCase
import javax.inject.Inject

@ActivityScope
class FakeGetChatListMessageUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository,
    private val mapper: GetChatListMessageMapper,
    dispatchers: CoroutineDispatchers
) : GetChatListMessageUseCase(graphqlRepository, mapper, dispatchers) {

    var response = ChatListPojo()

    override suspend fun execute(params: ChatListParam): ChatListResponse {
        return ChatListResponse(
            response,
            mapper.mapPinChat(response, params.page),
            mapper.mapUnpinChat(response)
        )
    }
}
