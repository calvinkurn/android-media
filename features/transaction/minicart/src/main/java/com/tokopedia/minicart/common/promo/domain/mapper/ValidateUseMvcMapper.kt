package com.tokopedia.minicart.common.promo.domain.mapper

import com.tokopedia.minicart.common.promo.data.response.ValidateUseMvcResponse
import com.tokopedia.minicart.common.promo.domain.data.ValidateUseMvcData
import javax.inject.Inject

class ValidateUseMvcMapper @Inject constructor() {

    fun mapValidateUseMvcResponse(validateUseMvcResponse: ValidateUseMvcResponse): ValidateUseMvcData {
        return ValidateUseMvcData(
            validateUseMvcResponse.status,
            validateUseMvcResponse.message,
            validateUseMvcResponse.detail.currentPurchase,
            validateUseMvcResponse.detail.minimumPurchase,
            validateUseMvcResponse.detail.progressPercentage,
            validateUseMvcResponse.detail.message
        )
    }
}