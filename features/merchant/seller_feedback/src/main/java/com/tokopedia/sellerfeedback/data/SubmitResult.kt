package com.tokopedia.sellerfeedback.data

sealed class SubmitResult {

    object Success : SubmitResult()

    data class NetworkFail(
            val cause: Throwable
    ) : SubmitResult()

    data class UploadFail(
            val cause: Throwable
    ) : SubmitResult()

    data class SubmitFail(
            val cause: Throwable
    ) : SubmitResult()
}
