package com.tokopedia.digital_checkout.usecase

import com.tokopedia.digital_checkout.data.DigitalCheckoutQueries
import com.tokopedia.digital_checkout.data.response.CancelVoucherData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import javax.inject.Inject

class DigitalCancelVoucherUseCase @Inject constructor(
        private val useCase: GraphqlUseCase<CancelVoucherData.Response>
) {
    fun execute(
            promoCode: String,
            onSuccess: (CancelVoucherData.Response) -> Unit,
            onError: (Throwable) -> Unit
    ) {
        useCase.apply {
            setRequestParams(createParam(promoCode))
            setTypeClass(CancelVoucherData.Response::class.java)
            setGraphqlQuery(DigitalCheckoutQueries.getCancelVoucherCartQuery())
            execute(onSuccess, onError)
        }
    }

    private fun createParam(promoCode: String): HashMap<String, Any> {
        val queryMap = HashMap<String, Any>()
        queryMap[PARAM_SERVICE_ID] = DEFAULT_DIGITAL_SERVICE_ID
        queryMap[PARAM_PROMO_CODE] = arrayOf(promoCode)
        queryMap[PARAM_IS_OCC] = DEFAULT_VALUE_IS_OCC
        return queryMap
    }

    companion object {
        const val PARAM_SERVICE_ID = "serviceID"
        const val PARAM_PROMO_CODE = "promoCode"
        const val PARAM_IS_OCC = "isOCC"
        const val DEFAULT_VALUE_IS_OCC = false
        const val DEFAULT_DIGITAL_SERVICE_ID = "recharge"
    }
}