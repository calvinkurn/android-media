package com.tokopedia.oneclickcheckout.preference.list.domain

class FakeSetDefaultPreferenceUseCase: SetDefaultPreferenceUseCase {

    var errorResponse: Throwable? = null
    var successResponse: String? = null

    private var internalOnError: ((Throwable) -> Unit)? = null
    private var internalOnSuccess: ((String) -> Unit)? = null

    override fun execute(profileId: Int, onSuccess: (String) -> Unit, onError: (Throwable) -> Unit) {
        //save callback then wait for trigger
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

    fun invokeOnSuccess(response: String) {
        internalOnSuccess?.invoke(response)
    }

    fun invokeOnError(throwable: Throwable) {
        internalOnError?.invoke(throwable)
    }

}