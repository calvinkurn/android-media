package com.tokopedia.play.view.uimodel

import androidx.annotation.StringRes
import com.tokopedia.play.view.type.PlayUserReportSectionType

/**
 * @author by astidhiyaa on 16/12/21
 */
data class PlayUserReportSection(
    val type: PlayUserReportSectionType,
    @StringRes val title: Int,
    val isUrl: Boolean,
    val onClick: (PlayUserReportSection) -> Unit = {}
) : PlayUserReportReasoningUiModel()

