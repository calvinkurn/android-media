package com.tokopedia.gm.common.domain.mapper

import com.tokopedia.gm.common.data.source.cloud.model.*
import com.tokopedia.gm.common.data.source.local.model.*
import com.tokopedia.kotlin.extensions.view.orZero
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 10/03/21
 */

class PMGradeBenefitInfoMapper @Inject constructor() {

    fun mapRemoteModelToUiModel(response: PMGradeBenefitInfoModel?): PMGradeBenefitInfoUiModel {
        return PMGradeBenefitInfoUiModel(
                shopId = response?.shopId.orZero().toString(),
                nextMonthlyRefreshDate = response?.nextMonthlyRefreshDate.orEmpty(),
                nextQuarterlyCalibrationRefreshDate = response?.nextQuarterlyCalibrationRefreshDate.orEmpty(),
                currentPMGrade = getCurrentPMGrade(response?.currentPMGrade),
                currentPMBenefits = getPMGradeBenefits(response?.currentPMBenefits),
                nextPMGrade = getNextPMGrade(response?.nextPMGrade),
                nextPMBenefits = getPMGradeBenefits(response?.nextPMBenefits),
                potentialPmGrade = getPMPotentialGrade(response?.potentialPmGrade),
                potentialPMBenefits = getPMGradeBenefits(response?.potentialBenefits),
                pmGradeBenefitList = getPMGradeBenefitList(response?.pmGradeBenefitList)
        )
    }

    private fun getPMGradeBenefitList(pmGradeBenefitList: List<PmGradeWithBenefitsModel>?): List<PMGradeWithBenefitsUiModel>? {
        pmGradeBenefitList?.let { list ->
            return list.map {
                PMGradeWithBenefitsUiModel(
                        gradeName = it.gradeName.orEmpty(),
                        isActive = it.isActive ?: false,
                        benefits = getPMGradeBenefits(it.benefits)
                )
            }
        }
        return null
    }

    private fun getPMPotentialGrade(potentialPmGrade: PotentialPmGradeModel?): PMPotentialGradeUiModel? {
        potentialPmGrade?.let {
            return PMPotentialGradeUiModel(
                    shopLevelCurrent = it.shopLevelCurrent.orZero(),
                    shopScoreCurrent = it.shopScoreCurrent.orZero(),
                    gradeName = it.gradeName.orEmpty(),
                    imgBadgeUrl = it.imgBadgeUrl.orEmpty(),
                    backgroundUrl = it.backgroundUrl
            )
        }
        return null
    }

    private fun getNextPMGrade(nextPmGrade: NextPMGradeModel?): PMNextGradeUiModel? {
        nextPmGrade?.let {
            return PMNextGradeUiModel(
                    shopLevel = it.shopLevel.orZero(),
                    shopScoreMin = it.shopScoreMin.orZero(),
                    shopScoreMax = it.shopScoreMax.orZero(),
                    gradeName = it.gradeName.orEmpty(),
                    imgBadgeUrl = it.imgBadgeUrl.orEmpty(),
                    backgroundUrl = it.backgroundUrl
            )
        }
        return null
    }

    private fun getPMGradeBenefits(currentBenefits: List<PMGradeBenefitModel>?): List<PMGradeBenefitUiModel>? {
        currentBenefits?.let { list ->
            return list.sortedBy { it.sequenceNum }
                    .map {
                        PMGradeBenefitUiModel(
                                categoryName = it.categoryName.orEmpty(),
                                benefitName = it.benefitName.orEmpty(),
                                sequenceNum = it.sequenceNum.orZero(),
                                appLink = it.appLink,
                                iconUrl = it.iconUrl
                        )
                    }
        }
        return null
    }

    private fun getCurrentPMGrade(currentPmGrade: CurrentPmGradeModel?): PMCurrentGradeUiModel? {
        currentPmGrade?.let {
            return PMCurrentGradeUiModel(
                    shopLevelActual = it.shopLevelActual.orZero(),
                    shopScoreActual = it.shopScoreActual.orZero(),
                    gradeName = it.gradeName.orEmpty(),
                    imgBadgeUrl = it.imgBadgeUrl.orEmpty(),
                    backgroundUrl = it.backgroundUrl.orEmpty()
            )
        }
        return null
    }
}