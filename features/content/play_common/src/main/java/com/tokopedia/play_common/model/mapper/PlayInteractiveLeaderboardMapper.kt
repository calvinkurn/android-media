package com.tokopedia.play_common.model.mapper

import com.tokopedia.play_common.domain.model.interactive.ChannelQuiz
import com.tokopedia.play_common.domain.model.interactive.GetInteractiveLeaderboardResponse
import com.tokopedia.play_common.domain.usecase.interactive.GetLeaderboardSlotResponse
import com.tokopedia.play_common.model.ui.*
import com.tokopedia.play_common.view.game.quiz.PlayQuizOptionState
import javax.inject.Inject

/**
 * Created by jegul on 02/07/21
 */
class PlayInteractiveLeaderboardMapper @Inject constructor() {

    /***
     * New leaderboard
     */
    fun mapNewLeaderboard(response: GetLeaderboardSlotResponse, isChatAllowed: () -> Boolean) =
        PlayLeaderboardInfoUiModel(
            leaderboardWinners = mapNewLeaderboardInteractive(response.data, isChatAllowed),
        )

    private fun mapNewLeaderboardInteractive(
        leaderboardsResponse: List<GetLeaderboardSlotResponse.SlotData>,
        isChatAllowed: () -> Boolean
    ): List<PlayLeaderboardUiModel> = leaderboardsResponse.map {
        PlayLeaderboardUiModel(
            title = it.title,
            winners = mapInteractiveWinner(
                title = it.title,
                winnersResponse = it.winner,
                isChatAllowed = isChatAllowed
            ),
            otherParticipantText = it.otherParticipantCountText,
            otherParticipant = it.otherParticipantCount.toLong(),
            choices = mapChoices(it.choices)
        )
    }

    fun mapChoices(choices: List<ChannelQuiz.Choices>): List<QuizChoicesUiModel> {
        val alphabet = getAlphabet(choices.size)
        return choices.mapIndexed { index: Int, item: ChannelQuiz.Choices ->
            QuizChoicesUiModel.Complete(
                item.choicesID,
                item.choicesText,
                PlayQuizOptionState.Default(alphabet = alphabet[index])
            )
        }
    }

    private fun getAlphabet(size: Int): List<Char> {
        val array = mutableListOf<Char>()
        var init = 'A'
        while (init <= 'Z') {
            array.add(init)
            init++
        }
        return array
    }

    /***
     * Old leaderboard
     */
    fun mapLeaderboard(response: GetInteractiveLeaderboardResponse, isChatAllowed: () -> Boolean) =
        PlayLeaderboardInfoUiModel(
            leaderboardWinners = mapLeaderboardWinner(
                response.data.data,
                response.data.config.topChatMessage,
                isChatAllowed
            ),
            totalParticipant = response.data.summary.totalParticipant.toString(),
            config = mapLeaderboardConfig(response),
        )

    private fun mapLeaderboardWinner(
        leaderboardsResponse: List<GetInteractiveLeaderboardResponse.Data>,
        topChatResponseRaw: String,
        isChatAllowed: () -> Boolean
    ): List<PlayLeaderboardUiModel> = leaderboardsResponse.map {
        PlayLeaderboardUiModel(
            title = it.title,
            winners = mapInteractiveWinner(it.title, it.winner, topChatResponseRaw, isChatAllowed),
            otherParticipantText = it.otherParticipantCountText,
            otherParticipant = it.otherParticipantCount.toLong()
        )
    }

    private fun mapInteractiveWinner(
        title: String,
        winnersResponse: List<GetInteractiveLeaderboardResponse.Winner>,
        topChatResponseRaw: String = "",
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
                .replace(
                    FORMAT_TOTAL_PARTICIPANT,
                    response.data.summary.totalParticipant.toString()
                ),
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