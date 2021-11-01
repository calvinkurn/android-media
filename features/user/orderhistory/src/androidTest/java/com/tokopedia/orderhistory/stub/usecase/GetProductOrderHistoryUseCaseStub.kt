package com.tokopedia.orderhistory.stub.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.orderhistory.data.ChatHistoryProductResponse
import com.tokopedia.orderhistory.data.OrderHistoryParam
import com.tokopedia.orderhistory.usecase.GetProductOrderHistoryUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetProductOrderHistoryUseCaseStub @Inject constructor(
        private val repository: GraphqlRepository,
        dispatchers: CoroutineDispatcher
) : GetProductOrderHistoryUseCase(repository, dispatchers) {

    override suspend fun execute(params: OrderHistoryParam): ChatHistoryProductResponse {
        return response
    }

    var response = ChatHistoryProductResponse()
        set(value) {
            field = value
        }

}