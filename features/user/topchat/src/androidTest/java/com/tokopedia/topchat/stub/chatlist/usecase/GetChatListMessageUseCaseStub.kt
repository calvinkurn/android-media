package com.tokopedia.topchat.stub.chatlist.usecase

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.topchat.TopchatAndroidTestCoroutineContextDispatcher
import com.tokopedia.topchat.chatlist.domain.mapper.GetChatListMessageMapper
import com.tokopedia.topchat.chatlist.pojo.ChatListPojo
import com.tokopedia.topchat.chatlist.usecase.GetChatListMessageUseCase
import com.tokopedia.topchat.chatroom.view.viewmodel.TopchatCoroutineContextProvider

class GetChatListMessageUseCaseStub constructor(
        gqlUseCase: GraphqlUseCase<ChatListPojo> = GraphqlUseCase(GraphqlInteractor.getInstance().graphqlRepository),
        private val mapper: GetChatListMessageMapper = GetChatListMessageMapper(),
        dispatcher: TopchatCoroutineContextProvider = TopchatAndroidTestCoroutineContextDispatcher(),
) : GetChatListMessageUseCase(gqlUseCase, mapper, dispatcher) {

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