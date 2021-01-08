package com.tokopedia.digital_checkout.usecase

import com.tokopedia.digital_checkout.data.DigitalCheckoutQueries
import com.tokopedia.digital_checkout.data.response.getcart.RechargeGetCart
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase

class DigitalGetCartUseCase(
        private val useCase: GraphqlUseCase<RechargeGetCart.Response>
) {
    fun execute(
            params: Map<String, String>,
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
}