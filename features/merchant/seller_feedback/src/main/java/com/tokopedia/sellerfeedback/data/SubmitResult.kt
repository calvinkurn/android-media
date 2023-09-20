package com.tokopedia.sellerfeedback.data

import com.tokopedia.seller.feedback.domain.model.SubmitFeedbackModel

sealed class SubmitResult {

    object Success : SubmitResult()

    data class SubmitFeedbackSuccess(val submitFeedbackModel: SubmitFeedbackModel?) : SubmitResult()

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
