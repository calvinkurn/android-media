package com.tokopedia.oneclickcheckout.preference.edit.domain.create

import com.tokopedia.oneclickcheckout.preference.edit.domain.create.model.CreatePreferenceRequest

class FakeCreatePreferenceUseCase: CreatePreferenceUseCase {

    var errorResponse: Throwable? = null
    var successResponse: String? = null

    private var internalOnError: ((Throwable) -> Unit)? = null
    private var internalOnSuccess: ((String) -> Unit)? = null

    fun invokeOnSuccess(response: String) {
        internalOnSuccess?.invoke(response)
    }

    fun invokeOnError(throwable: Throwable) {
        internalOnError?.invoke(throwable)
    }

    override fun execute(param: CreatePreferenceRequest, onSuccess: (String) -> Unit, onError: (Throwable) -> Unit) {
        when {
            this.errorResponse != null -> {
                onError(this.errorResponse!!)
            }
            this.successResponse != null -> {
                onSuccess(this.successResponse!!)
            }
            else -> {
                this.internalOnSuccess = onSuccess
                this.internalOnError = onError
            }
        }
    }

}