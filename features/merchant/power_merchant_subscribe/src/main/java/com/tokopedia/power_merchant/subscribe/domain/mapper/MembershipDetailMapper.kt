package com.tokopedia.power_merchant.subscribe.domain.mapper

import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.power_merchant.subscribe.domain.model.MembershipDetailResponse
import com.tokopedia.power_merchant.subscribe.view.model.MembershipDetailUiModel
import javax.inject.Inject

class MembershipDetailMapper @Inject constructor() {

    fun mapToUiModel(
        response: MembershipDetailResponse
    ): MembershipDetailUiModel {
        return MembershipDetailUiModel(
            periodDate = response.shopLevel.result.period.orEmpty(),
            gradeName = response.data?.currentPMGrade?.gradeName.orEmpty(),
            nextUpdate = DateFormatUtils.formatDate(
                DateFormatUtils.FORMAT_YYYY_MM_DD, DateFormatUtils.FORMAT_DD_MMMM_YYYY,
                response.data?.nextMonthlyRefreshDate.orEmpty()
            ),
            totalOrder = response.shopLevel.result.itemSold?.toLong().orZero(),
            netIncome = response.shopLevel.result.netIncomeValue?.toLong().orZero(),
            shopScore = response.data?.currentPMGrade?.shopScore.orZero()
        )
    }
}