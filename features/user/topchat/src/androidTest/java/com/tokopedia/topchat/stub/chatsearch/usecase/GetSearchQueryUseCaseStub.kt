package com.tokopedia.topchat.stub.chatsearch.usecase

import com.tokopedia.topchat.TopchatAndroidTestCoroutineContextDispatcher
import com.tokopedia.topchat.chatroom.view.viewmodel.TopchatCoroutineContextProvider
import com.tokopedia.topchat.chatsearch.data.GetMultiChatSearchResponse
import com.tokopedia.topchat.chatsearch.usecase.GetSearchQueryUseCase
import com.tokopedia.topchat.stub.common.GraphqlUseCaseStub

class GetSearchQueryUseCaseStub (
        private val gqlUseCase: GraphqlUseCaseStub<GetMultiChatSearchResponse> = GraphqlUseCaseStub(),
        dispatchers: TopchatCoroutineContextProvider = TopchatAndroidTestCoroutineContextDispatcher()
) : GetSearchQueryUseCase(gqlUseCase, dispatchers) {

    var response: GetMultiChatSearchResponse = GetMultiChatSearchResponse()
        set(value) {
            gqlUseCase.response = value
            field = value
        }
}