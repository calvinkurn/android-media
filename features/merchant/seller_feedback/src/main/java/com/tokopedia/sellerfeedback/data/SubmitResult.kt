package com.tokopedia.sellerfeedback.data

sealed class SubmitResult {

    object Success : SubmitResult()

    object NetworkFail : SubmitResult()

    data class UploadFail(
            val message: String
    ) : SubmitResult()

    data class SubmitFail(
            val message: String
    ) : SubmitResult()
}
