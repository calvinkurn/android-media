package com.tokopedia.tokochat.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.flow.FlowUseCase
import com.tokopedia.tokochat.domain.response.orderprogress.TokoChatTokopediaOrderResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TokoChatGetTokopediaOrderIdUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : FlowUseCase<String, String>(dispatchers.io) {

    override fun graphqlQuery(): String = """
        query tokochatTokopediaOrder($$GOJEK_ORDER_NUMBER: String!) {
          tokochatTokopediaOrder($GOJEK_ORDER_NUMBER: $$GOJEK_ORDER_NUMBER) {
            tokopediaOrderID
          }
        }
    """.trimIndent()

    override suspend fun execute(params: String): Flow<String> = flow {
        val response = repository.request<Map<String, Any>, TokoChatTokopediaOrderResponse>(
            graphqlQuery(),
            mapOf(GOJEK_ORDER_NUMBER to params)
        )
        emit(response.tokopediaOrder.tokopediaOrderId)
    }

    companion object {
        private const val GOJEK_ORDER_NUMBER = "gojekOrderNumber"
    }
}
