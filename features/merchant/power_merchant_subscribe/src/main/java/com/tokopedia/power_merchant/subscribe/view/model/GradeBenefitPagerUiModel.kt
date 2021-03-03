package com.tokopedia.power_merchant.subscribe.view.model

/**
 * Created By @ilhamsuaib on 02/03/21
 */

data class GradeBenefitPagerUiModel(
        val title: String,
        val gradeBenefits: List<GradeBenefitItemUiModel>,
        val isSelected: Boolean
)

data class GradeBenefitItemUiModel(
        val iconUrl: String,
        val description: String
)