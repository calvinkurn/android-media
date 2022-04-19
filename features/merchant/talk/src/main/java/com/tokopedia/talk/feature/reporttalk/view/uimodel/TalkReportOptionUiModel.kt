package com.tokopedia.talk.feature.reporttalk.view.uimodel

/**
 * @author by nisie on 8/30/18.
 */
data class TalkReportOptionUiModel(
        var position : Int = 0,
        var reportTitle : String = "",
        var reportReason : String = "",
        var isChecked : Boolean = false
)