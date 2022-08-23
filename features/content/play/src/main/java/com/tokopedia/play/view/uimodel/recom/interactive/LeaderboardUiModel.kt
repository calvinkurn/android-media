package com.tokopedia.play.view.uimodel.recom.interactive

import com.tokopedia.play_common.model.result.ResultState
import com.tokopedia.play_common.model.ui.PlayLeaderboardInfoUiModel

/**
 * Created by kenny.hadisaputra on 26/04/22
 */
data class LeaderboardUiModel(
    val data: PlayLeaderboardInfoUiModel,
    val state: ResultState,
) {
    companion object {
        val Empty: LeaderboardUiModel
            get() = LeaderboardUiModel(
                data = PlayLeaderboardInfoUiModel(),
                state = ResultState.Loading,
            )
    }
}