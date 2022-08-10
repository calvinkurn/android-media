package com.tokopedia.tkpd.flashsale.presentation.data.mapper

import com.tokopedia.tkpd.flashsale.presentation.data.response.DoFlashSaleProductSubmissionResponse
import com.tokopedia.tkpd.flashsale.presentation.domain.entity.ProductSubmissionResult
import javax.inject.Inject

class DoFlashSaleProductSubmissionMapper @Inject constructor() {

    fun map(response: DoFlashSaleProductSubmissionResponse): ProductSubmissionResult {
        val errorMessage =
            if (response.doFlashSaleProductSubmission.responseHeader.errorMessage.isEmpty()) {
                ""
            } else {
                response.doFlashSaleProductSubmission.responseHeader.errorMessage.first()
            }

        return ProductSubmissionResult(
            response.doFlashSaleProductSubmission.responseHeader.success,
            errorMessage
        )
    }

}