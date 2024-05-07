package com.tokopedia.checkoutpayment.list.domain

import com.tokopedia.checkoutpayment.list.data.ListingParam
import com.tokopedia.checkoutpayment.list.data.PaymentListingParamRequest
import com.tokopedia.checkoutpayment.list.domain.GetPaymentListingParamUseCase

class FakeGetPaymentListingParamUseCase : GetPaymentListingParamUseCase {

    private var internalOnSuccess: ((ListingParam) -> Unit)? = null
    private var internalOnError: ((Throwable) -> Unit)? = null

    fun invokeOnSuccess(param: ListingParam) {
        internalOnSuccess?.invoke(param)
    }

    fun invokeOnError(throwable: Throwable) {
        internalOnError?.invoke(throwable)
    }

    override fun execute(param: PaymentListingParamRequest, onSuccess: (ListingParam) -> Unit, onError: (Throwable) -> Unit) {
        this.internalOnSuccess = onSuccess
        this.internalOnError = onError
    }
}
