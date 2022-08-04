package com.tokopedia.digital_checkout.usecase

import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.digital_checkout.data.request.RequestBodyCheckout
import com.tokopedia.digital_checkout.data.request.checkout.RechargeCheckoutRequest
import java.lang.reflect.Type
import javax.inject.Inject

class DigitalCheckoutUseCase @Inject constructor(
    private val restUseCase: DigitalCheckoutRestUseCase,
    private val gqlUseCase: DigitalCheckoutGqlUseCase
) {
    suspend fun execute(
        restRequestParams: RequestBodyCheckout,
        gqlRequestParams: RechargeCheckoutRequest,
        isUseGql: Boolean
    ): Map<Type, RestResponse?> =
        if (isUseGql) {
            emptyMap()
        } else {
            restUseCase.setRequestParams(restRequestParams)
            restUseCase.executeOnBackground()
        }
}