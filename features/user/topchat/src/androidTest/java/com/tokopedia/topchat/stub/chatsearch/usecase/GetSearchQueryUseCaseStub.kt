package com.tokopedia.topchat.stub.chatsearch.usecase

import com.tokopedia.test.application.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.topchat.chatsearch.data.GetMultiChatSearchResponse
import com.tokopedia.topchat.chatsearch.usecase.GetSearchQueryUseCase
import com.tokopedia.topchat.stub.common.GraphqlUseCaseStub

class GetSearchQueryUseCaseStub(
        private val gqlUseCase: GraphqlUseCaseStub<GetMultiChatSearchResponse>,
        dispatchers: CoroutineTestDispatchersProvider
) : GetSearchQueryUseCase(gqlUseCase, dispatchers) {

    var response: GetMultiChatSearchResponse = GetMultiChatSearchResponse()
        set(value) {
            gqlUseCase.response = value
            field = value
        }
}