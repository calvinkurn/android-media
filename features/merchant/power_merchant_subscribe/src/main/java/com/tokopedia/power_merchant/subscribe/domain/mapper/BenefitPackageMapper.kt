package com.tokopedia.power_merchant.subscribe.domain.mapper

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.kotlin.extensions.view.asCamelCase
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.common.constant.Constant
import com.tokopedia.power_merchant.subscribe.domain.model.BenefitPackageResponse
import com.tokopedia.power_merchant.subscribe.view.model.*
import javax.inject.Inject

class BenefitPackageMapper @Inject constructor(@ApplicationContext val context: Context?) {

    fun mapToBenefitPackageList(
        benefitPackageResponse: BenefitPackageResponse
    ): List<BaseBenefitPackageUiModel> {
        return listOf(
            mapToBenefitPackageHeader(benefitPackageResponse),
            mapToBenefitPackageGrade(benefitPackageResponse)
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

    private fun mapToBenefitPackageGrade(
        benefitPackageResponse: BenefitPackageResponse
    ): BenefitPackageDataUiModel {
        return BenefitPackageDataUiModel(
            benefitPackageData = benefitPackageResponse.data?.nextBenefitPackageList?.map {
                val isUpgrade = it.pmGradeName == benefitPackageResponse.currentPMGrade?.gradeName
                BenefitPackageGradeUiModel(
                    gradeName = it.pmGradeName,
                    iconBenefitUrl = if (isUpgrade) {
                        Constant.Image.IC_PM_PRO_UPGRADE_LEVEL
                    } else {
                        Constant.Image.IC_PM_PRO_DOWNGRADE_WARNING
                    },
                    descBenefit = when (it.pmGradeName.asCamelCase()) {
                        Constant.PM_PRO_ADVANCED -> {
                            context?.getString(R.string.pm_desc_level_2_benefit_package_section)
                                .orEmpty()
                        }
                        Constant.PM_PRO_EXPERT -> {
                            context?.getString(R.string.pm_desc_level_3_benefit_package_section)
                                .orEmpty()
                        }
                        Constant.PM_PRO_ULTIMATE -> {
                            context?.getString(R.string.pm_desc_level_4_benefit_package_section)
                                .orEmpty()
                        }
                        else -> ""
                    },
                    benefitItemList = mapToBenefitItem(it.benefitList)
                )
            } ?: emptyList()
        )
    }

    private fun mapToBenefitItem(
        benefitPackageResponse:
        List<BenefitPackageResponse.PMBenefitData>
    ): List<BenefitItem> {
        return benefitPackageResponse.map {
            BenefitItem(imageUrL = it.imageUrl, title = it.benefitName)
        }
    }
}