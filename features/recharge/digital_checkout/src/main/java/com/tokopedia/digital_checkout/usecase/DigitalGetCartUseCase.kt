package com.tokopedia.digital_checkout.usecase

import com.tokopedia.digital_checkout.data.DigitalCheckoutQueries
import com.tokopedia.digital_checkout.data.response.getcart.RechargeGetCart
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import javax.inject.Inject

class DigitalGetCartUseCase @Inject constructor(
        private val useCase: GraphqlUseCase<RechargeGetCart.Response>
) {
    fun execute(
            params: Map<String, Any>,
            onSuccess: (RechargeGetCart.Response) -> Unit,
            onError: (Throwable) -> Unit
    ) {
        useCase.apply {
            setTypeClass(RechargeGetCart.Response::class.java)
            setRequestParams(params)
            setGraphqlQuery(DigitalCheckoutQueries.getGetCartQuery())
            execute(onSuccess, onError)
        }
    }

    companion object {
        private const val PARAM_CATEGORY_ID = "categoryId"
        fun createParams(categoryId: Int): Map<String, Any> = mapOf(PARAM_CATEGORY_ID to categoryId)
    }
}