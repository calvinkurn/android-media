package com.tokopedia.power_merchant.subscribe.domain.mapper

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
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
            gradeName = benefitPackageResponse.data?.currentPMGrade?.gradeName.orEmpty(),
            nextUpdate = DateFormatUtils.formatDate(
                DateFormatUtils.FORMAT_YYYY_MM_DD, DateFormatUtils.FORMAT_DD_MMMM_YYYY,
                benefitPackageResponse.data?.nextMonthlyRefreshDate.orEmpty()
            )
        )
    }

    private fun mapToBenefitPackageGrade(
        benefitPackageResponse: BenefitPackageResponse
    ): BenefitPackageDataUiModel {
        return BenefitPackageDataUiModel(
            benefitPackageData = benefitPackageResponse.data?.nextBenefitPackageList?.map {
                val isUpgrade =
                    it.pmGradeName == benefitPackageResponse.data.currentPMGrade?.gradeName
                val pmStatusText =
                    if (it.pmGradeName == benefitPackageResponse.data.currentPMGrade?.gradeName) {
                        context?.getString(R.string.pm_benefit_package_upgrade).orEmpty()
                    } else {
                        context?.getString(R.string.pm_benefit_package_downgrade).orEmpty()
                    }
                val mapDescAndBg = getBgAndDescBenefitPackage(it, pmStatusText)
                BenefitPackageGradeUiModel(
                    gradeName = it.pmGradeName,
                    iconBenefitUrl = if (isUpgrade) {
                        Constant.Image.IC_PM_PRO_UPGRADE_LEVEL
                    } else {
                        Constant.Image.IC_PM_PRO_DOWNGRADE_WARNING
                    },
                    descBenefit = mapDescAndBg.first,
                    backgroundUrl = mapDescAndBg.second,
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

    private fun getBgAndDescBenefitPackage(
        item:
        BenefitPackageResponse.NextBenefitPackageList,
        pmStatusText: String
    ): Pair<String, String> {
        val blackColor = com.tokopedia.unifyprinciples.R.color.Unify_N700_96.toString()
        return when (item.pmGradeName.asCamelCase()) {
            Constant.PM_PRO_ADVANCED -> {
                Pair(
                    context?.getString(
                        R.string.pm_desc_level_2_benefit_package_section,
                        pmStatusText,
                        blackColor,
                        blackColor,
                        blackColor
                    ).orEmpty(), Constant.Image.BG_BENEFIT_PACKAGE_PM_PRO_ADVANCED
                )
            }
            Constant.PM_PRO_EXPERT -> {
                Pair(
                    context?.getString(
                        R.string.pm_desc_level_3_benefit_package_section,
                        pmStatusText,
                        blackColor,
                        blackColor,
                        blackColor
                    ).orEmpty(), Constant.Image.BG_BENEFIT_PACKAGE_PM_PRO_EXPERT
                )
            }
            Constant.PM_PRO_ULTIMATE -> {
                Pair(
                    context?.getString(
                        R.string.pm_desc_level_4_benefit_package_section,
                        pmStatusText,
                        blackColor,
                        blackColor,
                        blackColor
                    ).orEmpty(), Constant.Image.BG_BENEFIT_PACKAGE_PM_PRO_ULTIMATE
                )
            }
            else -> Pair("", "")
        }
    }
}