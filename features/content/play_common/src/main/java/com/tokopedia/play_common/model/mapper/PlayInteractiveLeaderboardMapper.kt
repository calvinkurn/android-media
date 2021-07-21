package com.tokopedia.play_common.model.mapper

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
            leaderboardWinners = mapLeaderboardWinner(response.data.data, response.data.config.topChatMessage, isChatAllowed),
            totalParticipant = response.data.summary.totalParticipant.toString(),
            config = mapLeaderboardConfig(response)
    )

    private fun mapLeaderboardWinner(
            leaderboardsResponse: List<GetInteractiveLeaderboardResponse.Data>,
            topChatResponseRaw: String,
            isChatAllowed: () -> Boolean
    ): List<PlayLeaderboardUiModel> = leaderboardsResponse.map {
        PlayLeaderboardUiModel(
                title = it.title,
                winners = mapInteractiveWinner(it.title, it.winner, topChatResponseRaw, isChatAllowed),
                otherParticipantText = it.otherParticipantCountText
        )
    }

    private fun mapInteractiveWinner(
            title: String,
            winnersResponse: List<GetInteractiveLeaderboardResponse.Winner>,
            topChatResponseRaw: String,
            isChatAllowed: () -> Boolean,
    ) = winnersResponse.mapIndexed { index, winner ->
        PlayWinnerUiModel(
                rank = index + 1,
                id = winner.userID.toString(),
                name = winner.userName,
                imageUrl = winner.imageUrl,
                allowChat = isChatAllowed,
                topChatMessage = topChatResponseRaw
                        .replace(FORMAT_FIRST_NAME, winner.userName)
                        .replace(FORMAT_TITLE, title)
        )
    }

    private fun mapLeaderboardConfig(response: GetInteractiveLeaderboardResponse): PlayLeaderboardConfigUiModel {
        val configResponse = response.data.config
        val currentInteractive = response.data.data.firstOrNull()
        val currentInteractiveWinner = currentInteractive?.winner?.firstOrNull()
        val winnerName = currentInteractiveWinner?.userName.orEmpty()

        return PlayLeaderboardConfigUiModel(
                sellerMessage = configResponse.sellerMessage
                        .replace(FORMAT_WINNER_NAME, winnerName)
                        .replace(FORMAT_TOTAL_PARTICIPANT, response.data.summary.totalParticipant.toString()),
                winnerMessage = configResponse.winnerMessage,
                winnerDetail = configResponse.winnerDetail,
                loserMessage = configResponse.loserMessage
                        .replace(FORMAT_WINNER_NAME, winnerName),
                loserDetail = configResponse.loserDetail
        )
    }

    companion object {
        private const val FORMAT_WINNER_NAME = "{{winner_name}}"
        private const val FORMAT_TOTAL_PARTICIPANT = "{{total_participant}}"
        private const val FORMAT_FIRST_NAME = "{{first_name}}"
        private const val FORMAT_TITLE = "{{title}}"
    }
}