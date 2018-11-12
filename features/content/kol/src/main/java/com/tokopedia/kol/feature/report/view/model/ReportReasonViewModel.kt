package com.tokopedia.kol.feature.report.view.model

/**
 * @author by milhamj on 12/11/18.
 */
data class ReportReasonViewModel(
        val type: String = "",
        val description: String = "",
        val content: String = "",
        val isSelected: Boolean = false
)