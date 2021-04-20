package com.tokopedia.topchat.stub.chatlist.usecase

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.topchat.chatlist.domain.mapper.GetChatListMessageMapper
import com.tokopedia.topchat.chatlist.pojo.ChatListPojo
import com.tokopedia.topchat.chatlist.usecase.GetChatListMessageUseCase
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider

class GetChatListMessageUseCaseStub constructor(
        gqlUseCase: GraphqlUseCase<ChatListPojo> = GraphqlUseCase(GraphqlInteractor.getInstance().graphqlRepository),
        private val mapper: GetChatListMessageMapper = GetChatListMessageMapper()
) : GetChatListMessageUseCase(gqlUseCase, mapper, CoroutineDispatchersProvider) {

    var response = ChatListPojo()

    override fun getChatList(
            page: Int,
            filter: String,
            tab: String,
            onSuccess: (ChatListPojo, List<String>, List<String>) -> Unit,
            onError: (Throwable) -> Unit
    ) {
        onSuccess(response, mapper.mapPinChat(response, page), mapper.mapUnpinChat(response))
    }
}