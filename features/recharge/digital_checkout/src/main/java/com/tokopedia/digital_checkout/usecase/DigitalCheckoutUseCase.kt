package com.tokopedia.digital_checkout.usecase

import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.digital_checkout.data.request.DigitalCheckoutDataParameter
import com.tokopedia.digital_checkout.utils.DigitalCheckoutMapper
import com.tokopedia.network.exception.ResponseErrorException
import javax.inject.Inject

/**
 * @author Created By : Muhammad Furqan on Aug 5, 2022
 */
class DigitalCheckoutUseCase @Inject constructor(
    private val gqlUseCase: DigitalCheckoutGqlUseCase
) {
    suspend fun execute(
        requestCheckoutParams: DigitalCheckoutDataParameter,
        digitalIdentifierParams: RequestBodyIdentifier
    ): PaymentPassData {
        gqlUseCase.setParams(requestCheckoutParams, digitalIdentifierParams)
        val result = gqlUseCase.executeOnBackground().rechargeCheckoutV3

        if (result.errors.isNotEmpty()) {
            throw ResponseErrorException(result.errors.first().title)
        }

        return DigitalCheckoutMapper.mapToPaymentPassData(result)
    }
}
