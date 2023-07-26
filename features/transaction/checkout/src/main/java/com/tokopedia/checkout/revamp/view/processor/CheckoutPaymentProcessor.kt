package com.tokopedia.checkout.revamp.view.processor

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.checkout.domain.model.platformfee.PaymentFeeCheckoutRequest
import com.tokopedia.checkout.domain.model.platformfee.PaymentFeeResponse
import com.tokopedia.checkout.domain.usecase.GetPaymentFeeCheckoutUseCase
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class CheckoutPaymentProcessor @Inject constructor(
    private val getPaymentFeeCheckoutUseCase: GetPaymentFeeCheckoutUseCase,
    private val dispatchers: CoroutineDispatchers
) {

    suspend fun getDynamicPaymentFee(request: PaymentFeeCheckoutRequest): PaymentFeeResponse? {
//        view?.showPaymentFeeSkeletonLoading()
        return withContext(dispatchers.io) {
            try {
                getPaymentFeeCheckoutUseCase.setParams(request)
                val paymentFeeGqlResponse = getPaymentFeeCheckoutUseCase.executeOnBackground()
                if (paymentFeeGqlResponse.response.success) {
                    return@withContext paymentFeeGqlResponse.response
//                    view?.showPaymentFeeData(paymentFeeGqlResponse.response)
                } else {
                    return@withContext null
//                    view?.showPaymentFeeTickerFailedToLoad(shipmentPlatformFeeData.errorWording)
                }
            } catch (t: Throwable) {
                Timber.d(t)
                return@withContext null
//                view?.showPaymentFeeTickerFailedToLoad(shipmentPlatformFeeData.errorWording)
            }
        }
    }
}
