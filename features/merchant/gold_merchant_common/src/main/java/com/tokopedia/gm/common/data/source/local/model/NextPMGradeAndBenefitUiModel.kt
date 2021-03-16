package com.tokopedia.gm.common.data.source.local.model

/**
 * Created By @ilhamsuaib on 10/03/21
 */

data class NextPMGradeAndBenefitUiModel(
        val nextPMGrade: PMNextGradeUiModel? = null,
        val nextPMBenefits: List<PMGradeBenefitUiModel>? = null
)