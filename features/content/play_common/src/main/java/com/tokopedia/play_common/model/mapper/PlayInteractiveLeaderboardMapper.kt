package com.tokopedia.play_common.model.mapper

import com.tokopedia.play_common.domain.model.interactive.GetInteractiveLeaderboardResponse
import com.tokopedia.play_common.domain.model.interactive.QuizResponse
import com.tokopedia.play_common.domain.usecase.interactive.GetLeaderboardSlotResponse
import com.tokopedia.play_common.model.ui.*
import com.tokopedia.play_common.transformer.HtmlTextTransformer
import com.tokopedia.play_common.view.game.quiz.PlayQuizOptionState
import javax.inject.Inject

/**
 * Created by jegul on 02/07/21
 */
class PlayInteractiveLeaderboardMapper @Inject constructor(private val decodeHtml: HtmlTextTransformer) {

    @OptIn(ExperimentalStdlibApi::class)
    fun mapNewPlayLeaderboard(
        response: GetLeaderboardSlotResponse,
        isChatAllowed: () -> Boolean
    ): List<LeaderboardGameUiModel> = buildList {
        response.data.slots.forEach {
            //Header
            add(
                LeaderboardGameUiModel.Header(
                    id = it.interactiveId,
                    reward = if (getLeaderboardType(it) == LeadeboardType.Quiz) "" else decodeHtml.transform(it.reward),
                    leaderBoardType = getLeaderboardType(it),
                    title = if (getLeaderboardType(it) == LeadeboardType.Quiz) decodeHtml.transform(
                        it.question
                    ) else it.title
                )
            )

            // Quiz if any
            if (it.choices.isNotEmpty()) addAll(mapChoices(it.choices, it.userChoice))

            //Winner if any
            if (it.winner.isNotEmpty()) addAll(
                mapInteractiveWinnerNew(
                    title = it.title,
                    winnersResponse = it.winner,
                    isChatAllowed = isChatAllowed
                )
            ) // need to add topChat

            //Footer
                add(
                    LeaderboardGameUiModel.Footer(
                        otherParticipantText = it.otherParticipantCountText,
                        otherParticipant = it.otherParticipantCount.toLong(),
                        leaderBoardType = getLeaderboardType(it),
                        totalParticipant = it.winner.size.toLong(),
                        emptyLeaderBoardCopyText = it.emptyLeaderboardCopyText,
                        id = it.interactiveId,
                    )
                )
        }
    }


    /***
     * Change to typename to make sure
     */
    private fun getLeaderboardType(leaderboardsResponse: GetLeaderboardSlotResponse.SlotData): LeadeboardType {
        return when (leaderboardsResponse.type) {
            "PlayInteractiveViewerLeaderboardGiveaway" -> LeadeboardType.Giveaway
            "PlayInteractiveViewerLeaderboardQuiz" -> LeadeboardType.Quiz
            else -> LeadeboardType.Unknown
        }
    }

    private fun mapChoices(
        choices: List<QuizResponse.Choice>,
        userPicksId: String
    ): List<LeaderboardGameUiModel.QuizOption> {
        return choices.mapIndexed { index, item: QuizResponse.Choice ->
            LeaderboardGameUiModel.QuizOption(
                option =
                QuizChoicesUiModel(
                    index,
                    item.id,
                    decodeHtml.transform(item.text),
                    if (item.id == userPicksId)
                        PlayQuizOptionState.Answered(isCorrect = item.isCorrect ?: false)
                    else
                        PlayQuizOptionState.Other(isCorrect = item.isCorrect ?: false)
                )
            )
        }
    }

    private fun mapInteractiveWinnerNew(
        title: String,
        winnersResponse: List<GetInteractiveLeaderboardResponse.Winner>,
        topChatResponseRaw: String = "",
        isChatAllowed: () -> Boolean,
    ) = winnersResponse.mapIndexed { index, winner ->
        LeaderboardGameUiModel.Winner(
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

    companion object {
        private const val FORMAT_WINNER_NAME = "{{winner_name}}"
        private const val FORMAT_TOTAL_PARTICIPANT = "{{total_participant}}"
        private const val FORMAT_FIRST_NAME = "{{first_name}}"
        private const val FORMAT_TITLE = "{{title}}"
    }
}
