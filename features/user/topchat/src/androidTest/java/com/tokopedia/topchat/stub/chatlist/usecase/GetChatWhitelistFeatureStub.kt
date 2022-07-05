package com.tokopedia.topchat.stub.chatlist.usecase

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.test.application.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.topchat.chatlist.domain.pojo.whitelist.ChatWhitelistFeatureResponse
import com.tokopedia.topchat.chatlist.domain.usecase.GetChatWhitelistFeature

class GetChatWhitelistFeatureStub (
    gqlUseCase: GraphqlUseCase<ChatWhitelistFeatureResponse> =
        GraphqlUseCase(GraphqlInteractor.getInstance().graphqlRepository)
): GetChatWhitelistFeature(gqlUseCase, CoroutineTestDispatchersProvider) {

    var response = ChatWhitelistFeatureResponse()
    var isError = false
    var errorMessage = ""

    override fun getWhiteList(
        feature: String,
        onSuccess: (ChatWhitelistFeatureResponse) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        if (isError) {
            onError(Throwable(errorMessage))
        } else {
            onSuccess(response)
        }
    }
}