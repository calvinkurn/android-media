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
    ) : PlayUserReportReasoningUiModel() {
        companion object {
            val Empty = Reasoning(
                reasoningId = 0,
                title = "",
                detail = "",
                submissionData = UserReportOptions.OptionAdditionalField()
            )
        }
    }

    object Placeholder : PlayUserReportReasoningUiModel()
}
