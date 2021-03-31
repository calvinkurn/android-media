package com.tokopedia.orderhistory.stub.usecase

import com.tokopedia.orderhistory.OrderHistoryAndroidTestCoroutineContextDispatcher
import com.tokopedia.orderhistory.data.ChatHistoryProductResponse
import com.tokopedia.orderhistory.stub.common.GraphqlUseCaseStub
import com.tokopedia.orderhistory.usecase.GetProductOrderHistoryUseCase
import javax.inject.Inject

class GetProductOrderHistoryUseCaseStub @Inject constructor(
        private val gqlUseCase: GraphqlUseCaseStub<ChatHistoryProductResponse>,
        dispatchers: OrderHistoryAndroidTestCoroutineContextDispatcher
) : GetProductOrderHistoryUseCase(gqlUseCase, dispatchers) {

    var response = ChatHistoryProductResponse()
        set(value) {
            gqlUseCase.response = value
            field = value
        }

}