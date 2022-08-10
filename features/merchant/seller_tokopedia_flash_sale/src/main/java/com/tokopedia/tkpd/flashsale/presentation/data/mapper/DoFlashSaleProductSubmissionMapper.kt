package com.tokopedia.tkpd.flashsale.presentation.data.mapper

import com.tokopedia.tkpd.flashsale.presentation.data.response.DoFlashSaleProductSubmissionResponse
import com.tokopedia.tkpd.flashsale.presentation.domain.entity.ProductSubmissionResult
import javax.inject.Inject

class DoFlashSaleProductSubmissionMapper @Inject constructor() {

    fun map(response: DoFlashSaleProductSubmissionResponse): ProductSubmissionResult {
        return ProductSubmissionResult(
            response.doFlashSaleProductSubmission.responseHeader.success,
            response.doFlashSaleProductSubmission.responseHeader.toErrorMessage()
        )
    }

    private fun DoFlashSaleProductSubmissionResponse.DoFlashSaleProductSubmission.ResponseHeader.toErrorMessage(): String {
        return if (errorMessage.isEmpty()) {
            ""
        } else {
            errorMessage.first()
        }
    }
}