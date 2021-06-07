package com.tokopedia.oneclickcheckout.preference.edit.domain.payment

import com.tokopedia.oneclickcheckout.payment.domain.GetPaymentListingParamUseCase
import com.tokopedia.oneclickcheckout.payment.list.data.ListingParam
import com.tokopedia.oneclickcheckout.payment.list.data.PaymentListingParamRequest

class FakeGetPaymentListingParamUseCase: GetPaymentListingParamUseCase {

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