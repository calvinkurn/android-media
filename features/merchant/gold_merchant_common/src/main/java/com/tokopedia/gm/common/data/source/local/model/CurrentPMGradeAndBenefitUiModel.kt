package com.tokopedia.gm.common.data.source.local.model

/**
 * Created By @ilhamsuaib on 10/03/21
 */

data class CurrentPMGradeAndBenefitUiModel(
        val nextMonthlyRefreshDate: String = "",
        val nextQuarterlyCalibrationRefreshDate: String = "",
        val currentPMGrade: PMCurrentGradeUiModel? = null,
        val currentPMBenefits: List<PMGradeBenefitUiModel>? = null
)