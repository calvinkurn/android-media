package com.tokopedia.tokochat.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.tokochat.domain.response.orderprogress.TokoChatOrderProgressResponse
import com.tokopedia.tokochat.domain.response.orderprogress.param.TokoChatOrderProgressParam
import javax.inject.Inject

class TokoChatOrderProgressUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<TokoChatOrderProgressParam, TokoChatOrderProgressResponse>(dispatcher.io) {

    override fun graphqlQuery(): String = """
        query getTokochatOrderProgress(${'$'}orderID: String!, ${'$'}serviceType: String!){
          tokochatOrderProgress(orderID: ${'$'}orderID, serviceType: ${'$'}serviceType) {
            enable
            state
            orderId
            invoiceId
            imageUrl
            name
            status
            statusId
            label {
              title
              value
            }
            uri
          }
        }
    """.trimIndent()

    override suspend fun execute(params: TokoChatOrderProgressParam): TokoChatOrderProgressResponse {
        return repository.request(graphqlQuery(), params)
    }
}
