package com.tokopedia.topchat.chatroom.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.topchat.chatroom.domain.pojo.orderprogress.OrderProgressResponse
import javax.inject.Inject

open class OrderProgressUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
): CoroutineUseCase<String, OrderProgressResponse>(dispatcher.io) {

    override fun graphqlQuery(): String = """
        query chatOrderProgress($$PARAM_MSG_ID: String!){
          chatOrderProgress(msgID:$$PARAM_MSG_ID) {
            enable
            state
            orderId
            invoiceId
            shipment {
              id
              name
              productId
              productName
              awb
            }
            imageUrl
            name
            status
            label {
              title
              value
            }
            button {
              key
              label
              uri
            }
            uri
          }
        }
    """.trimIndent()

    override suspend fun execute(params: String): OrderProgressResponse {
        return repository.request(graphqlQuery(), mapOf(PARAM_MSG_ID to params))
    }

    companion object {
        private const val PARAM_MSG_ID = "msgId"
    }

}