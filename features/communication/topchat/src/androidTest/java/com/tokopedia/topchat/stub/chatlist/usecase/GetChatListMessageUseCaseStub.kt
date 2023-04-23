package com.tokopedia.topchat.stub.chatlist.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.test.application.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.topchat.chatlist.domain.mapper.GetChatListMessageMapper
import com.tokopedia.topchat.chatlist.domain.pojo.ChatListPojo
import com.tokopedia.topchat.chatlist.domain.pojo.ChatListResponse
import com.tokopedia.topchat.chatlist.domain.usecase.GetChatListMessageUseCase
import javax.inject.Inject

class GetChatListMessageUseCaseStub @Inject constructor(
    gqlUseCase: GraphqlUseCase<ChatListPojo>,
    private val mapper: GetChatListMessageMapper
) : GetChatListMessageUseCase(gqlUseCase, mapper, CoroutineTestDispatchersProvider) {

    var response = ChatListPojo()

    override fun getChatList(
        page: Int,
        filter: String,
        tab: String,
        onSuccess: (ChatListResponse) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        onSuccess(ChatListResponse(response, mapper.mapPinChat(response, page), mapper.mapUnpinChat(response)))
    }
}
