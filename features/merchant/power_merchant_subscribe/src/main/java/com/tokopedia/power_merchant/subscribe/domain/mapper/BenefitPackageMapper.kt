package com.tokopedia.power_merchant.subscribe.domain.mapper

import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.power_merchant.subscribe.domain.model.BenefitPackageResponse
import com.tokopedia.power_merchant.subscribe.view.model.BenefitPackageHeaderUiModel
import javax.inject.Inject

class BenefitPackageMapper @Inject constructor() {

    fun mapToBenefitPackageHeader(
        benefitPackageResponse: BenefitPackageResponse
    ): BenefitPackageHeaderUiModel {
        return BenefitPackageHeaderUiModel(
            periodDate = benefitPackageResponse.shopLevel.result.period.orEmpty(),
            gradeName = benefitPackageResponse.data?.currentPMGrade?.gradeName.orEmpty(),
            nextUpdate = DateFormatUtils.formatDate(
                DateFormatUtils.FORMAT_YYYY_MM_DD, DateFormatUtils.FORMAT_DD_MMMM_YYYY,
                benefitPackageResponse.data?.nextMonthlyRefreshDate.orEmpty()
            )
        )
    }
}