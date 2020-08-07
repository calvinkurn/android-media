package com.tokopedia.oneclickcheckout.preference.edit.domain.payment

import com.tokopedia.oneclickcheckout.preference.edit.data.payment.ListingParam

class FakeGetPaymentListingParamUseCase: GetPaymentListingParamUseCase {

    private var internalOnSuccess: ((ListingParam) -> Unit)? = null
    private var internalOnError: ((Throwable) -> Unit)? = null

    fun invokeOnSuccess(param: ListingParam) {
        internalOnSuccess?.invoke(param)
    }

    fun invokeOnError(throwable: Throwable) {
        internalOnError?.invoke(throwable)
    }

    override fun execute(merchantCode: String, profileCode: String, callbackUrl: String, addressId: String, onSuccess: (ListingParam) -> Unit, onError: (Throwable) -> Unit) {
        this.internalOnSuccess = onSuccess
        this.internalOnError = onError
    }
}