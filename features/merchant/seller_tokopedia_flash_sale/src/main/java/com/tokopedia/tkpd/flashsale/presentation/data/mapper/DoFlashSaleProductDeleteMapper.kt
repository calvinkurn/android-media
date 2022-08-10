package com.tokopedia.tkpd.flashsale.presentation.data.mapper

import com.tokopedia.tkpd.flashsale.presentation.data.response.DoFlashSaleProductDeleteResponse
import com.tokopedia.tkpd.flashsale.presentation.domain.entity.ProductDeleteResult
import javax.inject.Inject

class DoFlashSaleProductDeleteMapper @Inject constructor() {

    fun map(response: DoFlashSaleProductDeleteResponse): ProductDeleteResult {

        return ProductDeleteResult(
            response.doFlashSaleProductDelete.responseHeader.success,
            response.doFlashSaleProductDelete.responseHeader.toErrorMessage()
        )
    }

    private fun DoFlashSaleProductDeleteResponse.DoFlashSaleProductDelete.ResponseHeader.toErrorMessage(): String {
        return if (errorMessage.isEmpty()) {
            ""
        } else {
            errorMessage.first()
        }
    }
}