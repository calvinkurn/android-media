package com.tokopedia.tokochat.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.tokochat.domain.response.ticker.TokochatRoomTickerResponse
import javax.inject.Inject

class GetRoomTickerUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
): CoroutineUseCase<String, TokochatRoomTickerResponse>(dispatcher.io) {

    override fun graphqlQuery(): String = """
        query getTokochatRoomTicker($$PARAM_SERVICE_TYPE: String!){
             tokochatRoomTicker(serviceType:"tokofood") {
                message
                status
                tickerType
            }
        }
    """.trimIndent()

    override suspend fun execute(params: String): TokochatRoomTickerResponse {
        return repository.request(graphqlQuery(), mapOf(PARAM_SERVICE_TYPE to PARAM_TOKOFOOD))
    }

    companion object {
        private const val PARAM_SERVICE_TYPE = "serviceType"
        private const val PARAM_TOKOFOOD = "tokofood"
    }

}
