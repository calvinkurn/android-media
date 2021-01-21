package com.tokopedia.digital_checkout.usecase

import com.tokopedia.digital_checkout.data.DigitalCheckoutQueries
import com.tokopedia.digital_checkout.data.response.CancelVoucherData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import javax.inject.Inject

class DigitalCancelVoucherUseCase @Inject constructor(
        private val useCase: GraphqlUseCase<CancelVoucherData.Response>
) {
    fun execute(
            onSuccess: (CancelVoucherData.Response) -> Unit,
            onError: (Throwable) -> Unit
    ) {
        useCase.apply {
            setTypeClass(CancelVoucherData.Response::class.java)
            setGraphqlQuery(DigitalCheckoutQueries.getCancelVoucherCartQuery())
            execute(onSuccess, onError)
        }
    }
}