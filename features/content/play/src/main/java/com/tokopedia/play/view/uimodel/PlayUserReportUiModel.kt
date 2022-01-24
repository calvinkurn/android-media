package com.tokopedia.play.view.uimodel

/**
 * @author by astidhiyaa on 09/12/21
 */
sealed class PlayUserReportUiModel{
    data class Loaded(val reasoningList: List<PlayUserReportReasoningUiModel>) : PlayUserReportUiModel()
}