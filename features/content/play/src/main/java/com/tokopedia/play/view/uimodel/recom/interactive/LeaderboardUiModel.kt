package com.tokopedia.play.view.uimodel.recom.interactive

import com.tokopedia.play_common.model.result.ResultState
import com.tokopedia.play_common.model.ui.LeaderboardGameUiModel

/**
 * Created by kenny.hadisaputra on 26/04/22
 */
data class LeaderboardUiModel(
    val data: List<LeaderboardGameUiModel>,
    val state: ResultState,
) {
    companion object {
        val Empty: LeaderboardUiModel
            get() = LeaderboardUiModel(
                data = emptyList(),
                state = ResultState.Loading,
            )
    }
}