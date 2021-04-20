package com.tokopedia.topchat.chatroom.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.topchat.chatroom.domain.pojo.orderprogress.OrderProgressResponse
import javax.inject.Inject

class OrderProgressUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<OrderProgressResponse>
) {

    private val paramMsgId = "msgID"

    fun getOrderProgress(
            msgId: String,
            onSuccess: (OrderProgressResponse) -> Unit,
            onError: (Throwable) -> Unit
    ) {
        val params = generateParams(msgId)
        gqlUseCase.apply {
            setTypeClass(OrderProgressResponse::class.java)
            setRequestParams(params)
            setGraphqlQuery(query)
            execute({ result ->
                onSuccess(result)
            }, { error ->
                onError(error)
            })
        }
    }

    private fun generateParams(msgId: String): Map<String, Any> {
        return mapOf(
                paramMsgId to msgId
        )
    }

    private val query = """
        query chatOrderProgress($$paramMsgId: String!){
          chatOrderProgress(msgID:$$paramMsgId) {
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
}
