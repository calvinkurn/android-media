package com.tokopedia.talk_old.reporttalk.view.viewmodel

/**
 * @author by nisie on 8/30/18.
 */
data class TalkReportOptionViewModel(
        var position : Int = 0,
        var reportTitle : String = "",
        var reportReason : String = "",
        var isChecked : Boolean = false
)