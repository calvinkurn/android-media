package com.tokopedia.topchat.stub.chatlist.usecase

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.test.application.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.topchat.chatlist.domain.mapper.GetChatListMessageMapper
import com.tokopedia.topchat.chatlist.domain.pojo.ChatListParam
import com.tokopedia.topchat.chatlist.domain.pojo.ChatListPojo
import com.tokopedia.topchat.chatlist.domain.pojo.ChatListResponse
import com.tokopedia.topchat.chatlist.domain.usecase.GetChatListMessageUseCase
import javax.inject.Inject

@ActivityScope
class GetChatListMessageUseCaseStub @Inject constructor(
    graphqlRepository: GraphqlRepository,
    private val mapper: GetChatListMessageMapper
) : GetChatListMessageUseCase(graphqlRepository, mapper, CoroutineTestDispatchersProvider) {

    var response = ChatListPojo()

    override suspend fun execute(params: ChatListParam): ChatListResponse {
        return ChatListResponse(response, mapper.mapPinChat(response, params.page), mapper.mapUnpinChat(response))
    }
}
