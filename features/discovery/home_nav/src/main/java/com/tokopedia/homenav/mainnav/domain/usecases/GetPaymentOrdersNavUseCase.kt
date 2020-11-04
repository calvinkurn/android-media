package com.tokopedia.homenav.mainnav.domain.usecases

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.homenav.mainnav.data.payment.PaymentTransactionData
import com.tokopedia.homenav.mainnav.domain.model.NavPaymentOrder
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase

/**
 * Created by Fikry on 03/11/20.
 */
class GetPaymentOrdersNavUseCase (
        private val graphqlUseCase: GraphqlUseCase<PaymentTransactionData>
): UseCase<List<NavPaymentOrder>>(){

    private var params : Map<String, Any> = mapOf()

    init {
        val query = """
            query GetOrderHistory(${'$'}input:UOHOrdersRequest!){
              uohOrders(input:${'$'}input) {
                orders {
                  orderUUID
                  status
                  metadata {
                    detailURL {
                      appURL
                    }
                    status {
                      label
                      textColor
                      bgColor
                    }
                    products {
                      title
                      imageURL
                      inline1 {
                        label
                        textColor
                        bgColor
                      }
                      inline2 {
                        label
                        textColor
                        bgColor
                      }
                    }
                  }
                }
            }
        """.trimIndent()
        graphqlUseCase.setGraphqlQuery(query)
        graphqlUseCase.setRequestParams(generateParam())
        graphqlUseCase.setTypeClass(PaymentTransactionData::class.java)
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    override suspend fun executeOnBackground(): List<NavPaymentOrder> {
        return try {
            val responseData = Success(graphqlUseCase.executeOnBackground().paymentList)
            responseData.data.payment_list.map {
                NavPaymentOrder(
                        statusText = "",
                        statusTextColor = "",
                        paymentAmountText = it.payment_amount.toString(),
                        descriptionText = it.ticker_message,
                        imageUrl = it.gateway_img,
                        id = it.transaction_id
                )
            }
            listOf()
        } catch (e: Throwable){
            listOf()
        }
    }

    companion object{
        private const val LANG = "lang"
        private const val DEFAULT_VALUE_LANG = "ID"
    }

    private fun generateParam(): Map<String, Any?> {
        return mapOf(LANG to DEFAULT_VALUE_LANG)
    }
}