package com.tokopedia.play.view.uimodel

import com.tokopedia.play_common.model.result.ResultState

/**
 * @author by astidhiyaa on 09/12/21
 */
sealed class PlayUserReportUiModel{
    data class Loaded(val reasoningList: List<PlayUserReportReasoningUiModel>, val resultState: ResultState) : PlayUserReportUiModel()
    companion object{
        val Empty = Loaded(emptyList(), ResultState.Loading)
    }
}