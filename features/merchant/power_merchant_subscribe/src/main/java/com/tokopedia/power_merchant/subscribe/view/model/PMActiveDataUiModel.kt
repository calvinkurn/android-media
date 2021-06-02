package com.tokopedia.power_merchant.subscribe.view.model

import com.tokopedia.gm.common.data.source.local.model.PMCurrentGradeUiModel
import com.tokopedia.gm.common.data.source.local.model.PMGradeBenefitUiModel
import com.tokopedia.gm.common.data.source.local.model.PMNextGradeUiModel

/**
 * Created By @ilhamsuaib on 16/03/21
 */

data class PMActiveDataUiModel(
        val nextMonthlyRefreshDate: String = "",
        val nextQuarterlyCalibrationRefreshDate: String = "",
        val currentPMGrade: PMCurrentGradeUiModel? = null,
        val currentPMBenefits: List<PMGradeBenefitUiModel>? = null,
        val nextPMGrade: PMNextGradeUiModel? = null,
        val nextPMBenefits: List<PMGradeBenefitUiModel>? = null
)