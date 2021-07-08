package com.tokopedia.play_common.model.mapper

import com.tokopedia.config.GlobalConfig
import com.tokopedia.play_common.domain.model.interactive.GetInteractiveLeaderboardResponse
import com.tokopedia.play_common.model.ui.PlayLeaderboardConfigUiModel
import com.tokopedia.play_common.model.ui.PlayLeaderboardInfoUiModel
import com.tokopedia.play_common.model.ui.PlayLeaderboardUiModel
import com.tokopedia.play_common.model.ui.PlayWinnerUiModel
import javax.inject.Inject

/**
 * Created by jegul on 02/07/21
 */
class PlayInteractiveLeaderboardMapper @Inject constructor() {

    fun mapLeaderboard(response: GetInteractiveLeaderboardResponse, isChatAllowed: () -> Boolean) = PlayLeaderboardInfoUiModel(
            leaderboardWinner = mapLeaderboardWinner(response.data.data, isChatAllowed),
            totalParticipant = response.data.summary.totalParticipant.toString(),
            config = mapLeaderboardConfig(response.data.config)
    )

    private fun mapLeaderboardWinner(leaderboardsResponse: List<GetInteractiveLeaderboardResponse.Data>, isChatAllowed: () -> Boolean): List<PlayLeaderboardUiModel> = leaderboardsResponse.map {
        PlayLeaderboardUiModel(
                title = it.title,
                winners = mapInteractiveWinner(it.winner, isChatAllowed),
                otherParticipantText = it.otherParticipantCountText
        )
    }

    private fun mapInteractiveWinner(winnersResponse: List<GetInteractiveLeaderboardResponse.Winner>, isChatAllowed: () -> Boolean) = winnersResponse.mapIndexed { index, winner ->
        PlayWinnerUiModel(
                rank = index + 1,
                id = winner.userID.toString(),
                name = winner.userName,
                imageUrl = winner.imageUrl,
                allowChat = isChatAllowed
        )
    }

    private fun mapLeaderboardConfig(configResponse: GetInteractiveLeaderboardResponse.Config) = PlayLeaderboardConfigUiModel(
            sellerMessage = configResponse.sellerMessage,
            winnerMessage = configResponse.winnerMessage,
            winnerDetail = configResponse.winnerDetail,
            loserMessage = configResponse.loserMessage,
            loserDetail = configResponse.loserDetail,
    )
}