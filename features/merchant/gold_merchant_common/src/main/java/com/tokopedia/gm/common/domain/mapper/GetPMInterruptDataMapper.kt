package com.tokopedia.gm.common.domain.mapper

import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
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
        val unformattedDate = data.gradeBenefitInfo?.nextMonthlyRefreshDate.orEmpty()
        val pmNewUpdateDateFmt = DateFormatUtils.formatDate(currentFormat, newFormat, unformattedDate)
        return PowerMerchantInterruptUiModel(
                shopScore = data.shopInfo?.shopScore.orZero(),
                shopScoreThreshold = data.shopInfo?.shopScoreThreshold.orZero(),
                shopLevel = data.shopInfo?.shopLevel.orZero(),
                pmStatus = data.pmStatus?.data?.powerMerchant?.status.orEmpty(),
                pmGrade = data.gradeBenefitInfo?.currentPMGrade?.gradeName.orEmpty(),
                pmGradeBadge = data.gradeBenefitInfo?.currentPMGrade?.imgBadgeUrl.orEmpty(),
                potentialPmGrade = data.gradeBenefitInfo?.potentialPmGrade?.gradeName.orEmpty(),
                potentialPmGradeBadge = data.gradeBenefitInfo?.potentialPmGrade?.imgBadgeUrl.orEmpty(),
                pmNewUpdateDateFmt = pmNewUpdateDateFmt,
                periodType = data.pmSettingInfo?.periodeType ?: PeriodType.COMMUNICATION_PERIOD
        )
    }
}