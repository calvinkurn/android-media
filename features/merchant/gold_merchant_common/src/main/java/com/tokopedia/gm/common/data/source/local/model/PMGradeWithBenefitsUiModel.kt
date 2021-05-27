package com.tokopedia.gm.common.data.source.local.model

/**
 * Created By @ilhamsuaib on 10/03/21
 */

data class PMGradeWithBenefitsUiModel(
        val gradeName: String = "",
        val isActive: Boolean = false,
        val benefits: List<PMGradeBenefitUiModel>? = null
)