package com.tokopedia.power_merchant.subscribe.view.model

import com.tokopedia.gm.common.data.source.local.model.PMGradeWithBenefitsUiModel
import com.tokopedia.power_merchant.subscribe.view.adapter.WidgetAdapterFactory

/**
 * Created By @ilhamsuaib on 02/03/21
 */

class WidgetGradeBenefitUiModel(
    val currentShopLevel: Int,
    val currentCompletedOrder: Long,
    val currentIncome: Long,
    val isEligiblePm: Boolean,
    val isEligiblePmPro: Boolean,
    val benefitPages: List<PMGradeWithBenefitsUiModel>,
    val ctaAppLink: String,
    val shopScore:Int
) : BaseWidgetUiModel {

    override fun type(typeFactory: WidgetAdapterFactory): Int {
        return typeFactory.type(this)
    }
}