package com.tokopedia.checkout.revamp.view.processor

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.checkout.domain.model.platformfee.PaymentFeeCheckoutRequest
import com.tokopedia.checkout.domain.usecase.GetPaymentFeeCheckoutUseCase
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class CheckoutPaymentProcessor @Inject constructor(
    private val getPaymentFeeCheckoutUseCase: GetPaymentFeeCheckoutUseCase,
    private val dispatchers: CoroutineDispatchers
) {

    suspend fun getDynamicPaymentFee(request: PaymentFeeCheckoutRequest) {
//        view?.showPaymentFeeSkeletonLoading()
        withContext(dispatchers.io) {
            try {
                getPaymentFeeCheckoutUseCase.setParams(request)
                val paymentFeeGqlResponse = getPaymentFeeCheckoutUseCase.executeOnBackground()
                if (paymentFeeGqlResponse.response.success) {
//                    view?.showPaymentFeeData(paymentFeeGqlResponse.response)
                } else {
//                    view?.showPaymentFeeTickerFailedToLoad(shipmentPlatformFeeData.errorWording)
                }
            } catch (t: Throwable) {
                Timber.d(t)
//                view?.showPaymentFeeTickerFailedToLoad(shipmentPlatformFeeData.errorWording)
            }
        }
    }
}
