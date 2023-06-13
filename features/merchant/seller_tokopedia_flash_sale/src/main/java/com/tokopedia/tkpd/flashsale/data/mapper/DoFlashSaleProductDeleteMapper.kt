package com.tokopedia.tkpd.flashsale.data.mapper

import com.tokopedia.tkpd.flashsale.data.response.DoFlashSaleProductDeleteResponse
import com.tokopedia.tkpd.flashsale.domain.entity.ProductDeleteResult
import javax.inject.Inject

class DoFlashSaleProductDeleteMapper @Inject constructor() {

    fun map(response: DoFlashSaleProductDeleteResponse): ProductDeleteResult {
        return ProductDeleteResult(
            response.doFlashSaleProductDelete.responseHeader.success,
            response.doFlashSaleProductDelete.responseHeader.errorMessage.firstOrNull().orEmpty()
        )
    }
}
