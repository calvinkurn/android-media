package com.tokopedia.power_merchant.subscribe.domain.mapper

import com.tokopedia.power_merchant.subscribe.domain.model.BenefitPackageResponse
import com.tokopedia.power_merchant.subscribe.view.model.BaseBenefitPackageUiModel
import javax.inject.Inject

class BenefitPackageMapper @Inject constructor() {

    fun mapToBenefitPackageList(benefitPackageResponse: BenefitPackageResponse): List<BaseBenefitPackageUiModel> {
        return emptyList()
    }
}