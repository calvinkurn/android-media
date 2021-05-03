package com.tokopedia.gm.common.data.source.local.model

import com.tokopedia.gm.common.constant.PMConstant

/**
 * Created By @ilhamsuaib on 10/03/21
 */

data class PMGradeWithBenefitsUiModel(
        val gradeName: String = "",
        val isActive: Boolean = false,
        val pmTier: Int = PMConstant.PMTierType.PM_REGULAR,
        val benefits: List<PMGradeBenefitUiModel>? = null
)