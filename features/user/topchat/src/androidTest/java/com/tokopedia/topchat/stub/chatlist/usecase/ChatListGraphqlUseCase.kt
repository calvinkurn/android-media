package com.tokopedia.topchat.stub.chatlist.usecase

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topchat.chatlist.pojo.ChatListPojo

class ChatListGraphqlUseCase(
        graphqlRepository: GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository
) : GraphqlUseCase<ChatListPojo>(graphqlRepository) {

    var response = ChatListPojo()

    override suspend fun executeOnBackground(): ChatListPojo {
        return response
    }

}