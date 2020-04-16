package com.tokopedia.talk.feature.report.presentation.uimodel

data class TalkReportUiModel(
        val message: String = "",
        val type: Int = 0,
        val displayName: String = "",
        val isSelected: Boolean = false
)