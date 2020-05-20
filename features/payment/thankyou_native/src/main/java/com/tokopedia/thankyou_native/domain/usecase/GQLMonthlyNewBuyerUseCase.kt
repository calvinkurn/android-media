package com.tokopedia.thankyou_native.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.thankyou_native.GQL_THANKS_MONTHLY_NEW_BUYER
import com.tokopedia.thankyou_native.domain.model.GQLMonthlyNewBuyerResponse
import com.tokopedia.thankyou_native.domain.model.MonthlyNewBuyer
import javax.inject.Inject
import javax.inject.Named

class GQLMonthlyNewBuyerUseCase @Inject constructor(
        @Named(GQL_THANKS_MONTHLY_NEW_BUYER) val query: String, graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<GQLMonthlyNewBuyerResponse>(graphqlRepository) {

    fun getMonthlyNewBuyer(onSuccess: (MonthlyNewBuyer) -> Unit,
                           onError: (Throwable) -> Unit, orderId: String) {
        try {
            this.setTypeClass(GQLMonthlyNewBuyerResponse::class.java)
            this.setRequestParams(getRequestParams(orderId))
            this.setGraphqlQuery(query)
            this.execute(
                    { result ->
                        onSuccess(result.monthlyNewBuyer)
                    }, { error ->
                onError(error)
            }
            )
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
    }

    private fun getRequestParams(orderId: String): Map<String, Any> {
        return mapOf(ORDER_ID to orderId.toLong())
    }

    companion object {
        const val ORDER_ID = "order_id"
    }
}