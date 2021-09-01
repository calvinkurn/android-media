package com.tokopedia.power_merchant.subscribe.domain.mapper

import com.tokopedia.power_merchant.subscribe.domain.model.BenefitPackageResponse
import com.tokopedia.power_merchant.subscribe.view.model.BaseBenefitPackageUiModel
import com.tokopedia.power_merchant.subscribe.view.model.BenefitItem
import com.tokopedia.power_merchant.subscribe.view.model.BenefitPackageHeaderUiModel
import javax.inject.Inject

class BenefitPackageMapper @Inject constructor() {

    fun mapToBenefitPackageList(
        benefitPackageResponse: BenefitPackageResponse
    ): List<BaseBenefitPackageUiModel> {
        return listOf(
            mapToBenefitPackageHeader(benefitPackageResponse)
        )
    }

    private fun mapToBenefitPackageHeader(
        benefitPackageResponse: BenefitPackageResponse
    ): BenefitPackageHeaderUiModel {
        return BenefitPackageHeaderUiModel(
            periodDate = benefitPackageResponse.shopLevel.result.period.orEmpty(),
            gradeName = benefitPackageResponse.currentPMGrade?.gradeName.orEmpty(),
            nextUpdate = benefitPackageResponse.nextMonthlyRefreshDate.orEmpty()
        )
    }

    private fun mapToBenefitItem(
        benefitPackageResponse:
        BenefitPackageResponse.NextBenefitPackageList
    ): List<BenefitItem> {
        return benefitPackageResponse.benefitList.map {
            BenefitItem(imageUrL = it.imageUrl, title = it.benefitName)
        }
    }
}