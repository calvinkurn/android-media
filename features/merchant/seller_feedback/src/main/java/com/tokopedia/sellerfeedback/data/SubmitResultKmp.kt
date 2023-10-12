package com.tokopedia.sellerfeedback.data

import com.tokopedia.seller.feedback.domain.model.SubmitFeedbackModel

sealed class SubmitResultKmp {

    data class SubmitFeedbackSuccess(val submitFeedbackModel: SubmitFeedbackModel?) : SubmitResultKmp()

    data class NetworkFail(
        val cause: Throwable
    ) : SubmitResultKmp()

    data class UploadFail(
        val cause: Throwable
    ) : SubmitResultKmp()

    data class SubmitFail(
        val cause: Throwable
    ) : SubmitResultKmp()
}
