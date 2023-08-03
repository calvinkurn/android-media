package com.tokopedia.play.view.uimodel.mapper

import com.tokopedia.content.common.report_content.model.UserReportOptions
import com.tokopedia.play.di.PlayScope
import com.tokopedia.content.common.report_content.model.PlayUserReportReasoningUiModel
import javax.inject.Inject

/**
 * @author by astidhiyaa on 09/12/21
 */

@PlayScope
class PlayUserReportReasoningMapper @Inject constructor(){
    fun mapUserReportReasoning(reasoning: UserReportOptions) : PlayUserReportReasoningUiModel {
        return PlayUserReportReasoningUiModel.Reasoning(
            reasoningId = reasoning.id,
            title = reasoning.value,
            detail = reasoning.detail,
            submissionData = if(reasoning.additionalField.isNotEmpty()) reasoning.additionalField.first() else UserReportOptions.OptionAdditionalField()
        )
    }
}
