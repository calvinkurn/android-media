package com.tokopedia.gm.common.data.source.local.model

/**
 * Created By @ilhamsuaib on 10/03/21
 */

data class PMGradeBenefitInfoUiModel(
        val shopId: String = "",
        val nextMonthlyRefreshDate: String = "",
        val nextQuarterlyCalibrationRefreshDate: String = "",
        val currentPMGrade: PMCurrentGradeUiModel? = null,
        val currentPMBenefits: List<PMGradeBenefitUiModel>? = null,
        val nextPMGrade: PMNextGradeUiModel? = null,
        val nextPMBenefits: List<PMGradeBenefitUiModel>? = null
)