package com.tokopedia.gm.common.domain.mapper

import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.gm.common.constant.PMStatusConst
import com.tokopedia.gm.common.constant.PeriodType
import com.tokopedia.gm.common.data.source.cloud.model.PMInterruptDataResponse
import com.tokopedia.gm.common.view.model.PowerMerchantInterruptUiModel
import com.tokopedia.kotlin.extensions.view.orZero
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 21/03/21
 */

class GetPMInterruptDataMapper @Inject constructor() {

    fun mapRemoteModelToUiModel(data: PMInterruptDataResponse): PowerMerchantInterruptUiModel {
        val currentFormat = "yyyy-MM-dd"
        val newFormat = "dd MMMM yyyy"
        val osActiveStatus = PMStatusConst.ACTIVE
        val unformattedDate = data.gradeBenefitInfo?.nextMonthlyRefreshDate.orEmpty()
        val pmNewUpdateDateFmt = DateFormatUtils.formatDate(currentFormat, newFormat, unformattedDate)
        val reputationThreshold = 5
        val hasReputation = data.shopReputation.firstOrNull()?.score?.toDouble().orZero() >= reputationThreshold
        return PowerMerchantInterruptUiModel(
                shopScore = data.shopInfo?.shopScore.orZero(),
                shopScoreThreshold = data.shopInfo?.shopScoreThreshold.orZero(),
                shopLevel = data.shopInfo?.shopLevel.orZero(),
                isEligiblePm = data.shopInfo?.isEligiblePm ?: true,
                isNewSeller = data.shopInfo?.isNewSeller ?: true,
                shopAge = data.shopInfo?.shopAge ?: 1,
                pmStatus = data.pmStatus?.data?.powerMerchant?.status.orEmpty(),
                pmGrade = data.gradeBenefitInfo?.currentPMGrade?.gradeName.orEmpty(),
                pmGradeBadge = data.gradeBenefitInfo?.currentPMGrade?.imgBadgeUrl.orEmpty(),
                potentialPmGrade = data.gradeBenefitInfo?.potentialPmGrade?.gradeName.orEmpty(),
                potentialPmGradeBadge = data.gradeBenefitInfo?.potentialPmGrade?.imgBadgeUrl.orEmpty(),
                pmNewUpdateDateFmt = pmNewUpdateDateFmt,
                periodType = data.pmSettingInfo?.periodeType ?: PeriodType.COMMUNICATION_PERIOD,
                hasReputation = hasReputation,
                isOfficialStore = data.pmStatus?.data?.officialStore?.status == osActiveStatus
        )
    }
}