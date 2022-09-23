package com.tokopedia.digital_checkout.usecase

import com.google.gson.reflect.TypeToken
import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.common_digital.atc.data.response.FintechProduct
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.digital_checkout.data.request.DigitalCheckoutDataParameter
import com.tokopedia.digital_checkout.data.response.ResponseCheckout
import com.tokopedia.digital_checkout.utils.DigitalCheckoutMapper
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.network.exception.ResponseErrorException
import javax.inject.Inject


/**
 * @author Created By : Muhammad Furqan on Aug 5, 2022
 */
class DigitalCheckoutUseCase @Inject constructor(
    private val restUseCase: DigitalCheckoutRestUseCase,
    private val gqlUseCase: DigitalCheckoutGqlUseCase
) {
    suspend fun execute(
        requestCheckoutParams: DigitalCheckoutDataParameter,
        digitalIdentifierParams: RequestBodyIdentifier,
        fintechProduct: FintechProduct?,
        isUseGql: Boolean
    ): PaymentPassData =
        if (isUseGql) {
            gqlUseCase.setParams(requestCheckoutParams, digitalIdentifierParams)
            val result = gqlUseCase.executeOnBackground().rechargeCheckoutV3

            if (result.errors.isNotEmpty()) {
                throw ResponseErrorException(result.errors.first().title)
            }

            DigitalCheckoutMapper.mapToPaymentPassData(result)
        } else {
            restUseCase.setRequestParams(
                DigitalCheckoutMapper.getRequestBodyCheckout(
                    requestCheckoutParams, digitalIdentifierParams, fintechProduct
                )
            )
            val result = restUseCase.executeOnBackground()
            val token = object : TypeToken<DataResponse<ResponseCheckout>>() {}.type
            val restResponse = result[token]
            val data = restResponse!!.getData<DataResponse<*>>()
            val responseCheckoutData = data.data as ResponseCheckout

            DigitalCheckoutMapper.mapToPaymentPassData(responseCheckoutData)
        }
}
