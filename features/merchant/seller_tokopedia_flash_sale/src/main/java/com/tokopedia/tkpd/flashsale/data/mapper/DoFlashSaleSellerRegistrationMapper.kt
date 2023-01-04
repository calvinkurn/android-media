package com.tokopedia.tkpd.flashsale.data.mapper

import com.tokopedia.tkpd.flashsale.data.response.DoFlashSaleProductRegistrationResponse
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSaleRegistrationResult
import javax.inject.Inject

class DoFlashSaleSellerRegistrationMapper @Inject constructor() {
    fun map(response: DoFlashSaleProductRegistrationResponse): FlashSaleRegistrationResult {
        return FlashSaleRegistrationResult(
            response.doFlashSaleSellerRegistration.responseHeader.success,
            response.doFlashSaleSellerRegistration.responseHeader.errorMessage.firstOrNull()
                .orEmpty()
        )
    }
}