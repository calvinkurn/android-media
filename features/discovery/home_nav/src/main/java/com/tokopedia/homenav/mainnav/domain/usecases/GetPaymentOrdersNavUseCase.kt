package com.tokopedia.homenav.mainnav.domain.usecases

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.homenav.mainnav.data.pojo.payment.Payment
import com.tokopedia.homenav.mainnav.data.pojo.payment.PaymentQuery
import com.tokopedia.homenav.mainnav.domain.model.NavPaymentOrder
import com.tokopedia.homenav.mainnav.domain.model.NavProductOrder
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase

/**
 * Created by Fikry on 03/11/20.
 */
class GetPaymentOrdersNavUseCase (
        private val graphqlUseCase: GraphqlUseCase<Payment>
): UseCase<List<NavPaymentOrder>>(){

    private var params : Map<String, Any> = mapOf()

    init {
        val query = """
            query PaymentQuery {
              paymentQuery: paymentList(lang: "ID", cursor:"") {
                paymentList: payment_list {
                  merchantCode: merchant_code
                  transactionID: transaction_id
                  paymentAmount: payment_amount
                  tickerMessage: ticker_message
                  gatewayImg: gateway_img
                  applink: app_link
                  bankImg: bank_img
                }
              }
            }
        """.trimIndent()
        graphqlUseCase.setGraphqlQuery(query)
        graphqlUseCase.setRequestParams(generateParam())
        graphqlUseCase.setTypeClass(Payment::class.java)
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    override suspend fun executeOnBackground(): List<NavPaymentOrder> {
        val responseData = Success(graphqlUseCase.executeOnBackground().paymentQuery?: PaymentQuery())
        val navPaymentList = mutableListOf<NavPaymentOrder>()

        if (responseData.data.paymentList?.isNotEmpty() == true) {
            responseData.data.paymentList?.map {
                navPaymentList.add(NavPaymentOrder(
                        statusText = "",
                        statusTextColor = "",
                        paymentAmountText = it.paymentAmount.toString(),
                        descriptionText = it.tickerMessage?:"",
                        imageUrl = if(it.bankImg?.isNotBlank() == true) it.bankImg else it.gatewayImg ?: "",
                        id = it.transactionID?:"",
                        applink = it.applink?:""
                )
                )
            }
        }
        return navPaymentList
    }

    companion object{
        private const val LANG = "lang"
        private const val DEFAULT_VALUE_LANG = "ID"
    }

    private fun generateParam(): Map<String, Any?> {
        return mapOf(LANG to DEFAULT_VALUE_LANG)
    }
}