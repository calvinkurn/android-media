package com.tokopedia.content.common.report_content.model

import androidx.annotation.StringRes

/**
 * @author by astidhiyaa on 16/12/21
 */
data class PlayUserReportSection(
    val type: PlayUserReportSectionType,
    @StringRes val title: Int,
    val isUrl: Boolean,
    val onClick: (PlayUserReportSection) -> Unit = {}
) : PlayUserReportReasoningUiModel()

