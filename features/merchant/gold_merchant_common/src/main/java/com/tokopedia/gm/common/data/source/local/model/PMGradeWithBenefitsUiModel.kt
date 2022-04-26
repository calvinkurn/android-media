package com.tokopedia.gm.common.data.source.local.model

/**
 * Created By @ilhamsuaib on 10/03/21
 */

data class PMGradeWithBenefitsUiModel(
    val gradeName: String = "",
    val isActive: Boolean = false,
    val benefitList: List<PMBenefitItemUiModel> = emptyList(),
    val benefits: List<PMGradeBenefitUiModel>? = null
)