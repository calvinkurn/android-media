package com.tokopedia.gm.common.domain.mapper

import com.tokopedia.gm.common.data.source.cloud.model.PMInterruptDataResponse
import com.tokopedia.gm.common.view.model.PowerMerchantInterruptUiModel
import com.tokopedia.kotlin.extensions.view.orZero
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 21/03/21
 */

class GetPMInterruptDataMapper @Inject constructor() {

    fun mapRemoteModelToUiModel(data: PMInterruptDataResponse): PowerMerchantInterruptUiModel {
        return PowerMerchantInterruptUiModel(
                shopScore = data.shopInfo.shopScore.orZero(),
                shopScoreThreshold = data.shopInfo.shopScoreThreshold.orZero(),
                shopLevel = data.shopInfo.shopLevel.orZero(),
                pmStatus = data.pmStatus.data?.powerMerchant?.status.orEmpty(),
                pmGrade = data.gradeBenefitInfo.currentPMGrade?.gradeName.orEmpty(),
                pmGradeBadge = data.gradeBenefitInfo.currentPMGrade?.imgBadgeUrl.orEmpty(),
                potentialPmGrade = data.gradeBenefitInfo.potentialPmGrade?.gradeName.orEmpty(),
                potentialPmGradeBadge = data.gradeBenefitInfo.potentialPmGrade?.imgBadgeUrl.orEmpty(),
                pmNewUpdateDateFmt = data.gradeBenefitInfo.nextMonthlyRefreshDate.orEmpty()
        )
    }
}