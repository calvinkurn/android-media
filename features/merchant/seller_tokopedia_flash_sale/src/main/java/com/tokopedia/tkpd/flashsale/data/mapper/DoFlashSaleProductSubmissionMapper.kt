package com.tokopedia.tkpd.flashsale.data.mapper

import com.tokopedia.tkpd.flashsale.data.response.DoFlashSaleProductSubmissionResponse
import com.tokopedia.tkpd.flashsale.domain.entity.ProductSubmissionResult
import javax.inject.Inject

class DoFlashSaleProductSubmissionMapper @Inject constructor() {

    fun map(response: DoFlashSaleProductSubmissionResponse, totalSubmittedProduct: Long): ProductSubmissionResult {
        return ProductSubmissionResult(
            response.doFlashSaleProductSubmission.responseHeader.success,
            response.doFlashSaleProductSubmission.responseHeader.errorMessage.firstOrNull().orEmpty(),
            totalSubmittedProduct,
            response.doFlashSaleProductSubmission.sseKey,
            response.doFlashSaleProductSubmission.useSse
        )
    }
}
