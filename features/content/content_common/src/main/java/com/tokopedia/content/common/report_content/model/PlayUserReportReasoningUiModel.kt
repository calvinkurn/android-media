package com.tokopedia.content.common.report_content.model

/**
 * @author by astidhiyaa on 09/12/21
 */
sealed class PlayUserReportReasoningUiModel {
    data class Reasoning(
        val reasoningId: Int,
        val title: String,
        val detail: String,
        val submissionData: UserReportOptions.OptionAdditionalField
    ) : PlayUserReportReasoningUiModel()

    object Placeholder: PlayUserReportReasoningUiModel()
}
