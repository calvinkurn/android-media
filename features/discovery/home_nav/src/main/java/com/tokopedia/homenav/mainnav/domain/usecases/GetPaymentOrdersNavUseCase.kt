package com.tokopedia.homenav.mainnav.domain.usecases

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.homenav.mainnav.data.pojo.payment.Payment
import com.tokopedia.homenav.mainnav.data.pojo.payment.PaymentQuery
import com.tokopedia.homenav.mainnav.domain.model.NavPaymentOrder
import com.tokopedia.homenav.mainnav.domain.usecases.query.GetPaymentQuery
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase

/**
 * Created by Fikry on 03/11/20.
 */
class GetPaymentOrdersNavUseCase(
    private val graphqlUseCase: GraphqlUseCase<Payment>
) : UseCase<Result<List<NavPaymentOrder>>>() {
    init {
        graphqlUseCase.setGraphqlQuery(GetPaymentQuery())
        graphqlUseCase.setRequestParams(generateParam())
        graphqlUseCase.setTypeClass(Payment::class.java)
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    override suspend fun executeOnBackground(): Result<List<NavPaymentOrder>> {
        return try {
            val responseData = graphqlUseCase.executeOnBackground().paymentQuery ?: PaymentQuery()
            val navPaymentList = responseData.paymentList?.map {
                NavPaymentOrder(
                    statusText = "",
                    statusTextColor = "",
                    paymentAmountText = it.paymentAmount.toString(),
                    descriptionText = it.tickerMessage ?: "",
                    imageUrl = if (it.bankImg?.isNotBlank() == true) it.bankImg else it.gatewayImg ?: "",
                    id = it.transactionID ?: "",
                    applink = it.applink ?: ""
                )
            }.orEmpty()
            Success(navPaymentList)
        } catch (e: Exception) {
            Fail(e)
        }
    }

    companion object {
        private const val LANG = "lang"
        private const val DEFAULT_VALUE_LANG = "ID"
    }

    private fun generateParam(): Map<String, Any?> {
        return mapOf(LANG to DEFAULT_VALUE_LANG)
    }
}
