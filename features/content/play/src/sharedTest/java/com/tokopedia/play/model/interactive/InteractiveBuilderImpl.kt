package com.tokopedia.play.model.interactive

import com.tokopedia.play.view.uimodel.recom.interactive.LeaderboardUiModel
import com.tokopedia.play_common.model.dto.interactive.InteractiveUiModel
import com.tokopedia.play_common.model.result.ResultState
import com.tokopedia.play_common.model.ui.LeadeboardType
import com.tokopedia.play_common.model.ui.PlayLeaderboardConfigUiModel
import com.tokopedia.play_common.model.ui.PlayLeaderboardInfoUiModel
import com.tokopedia.play_common.model.ui.PlayLeaderboardUiModel
import com.tokopedia.play_common.model.ui.PlayWinnerUiModel
import com.tokopedia.play_common.model.ui.QuizChoicesUiModel
import com.tokopedia.play_common.view.game.quiz.PlayQuizOptionState

/**
 * Created by kenny.hadisaputra on 11/05/22
 */
class InteractiveBuilderImpl : InteractiveBuilder {

    override fun buildLeaderboard(
        data: PlayLeaderboardInfoUiModel,
        state: ResultState,
    ) = LeaderboardUiModel(
        data = data,
        state = state,
    )

    override fun buildLeaderboardInfo(
        leaderboardWinners: List<PlayLeaderboardUiModel>,
        totalParticipant: String,
        config: PlayLeaderboardConfigUiModel,
    ) = PlayLeaderboardInfoUiModel(
        leaderboardWinners = leaderboardWinners,
        totalParticipant = totalParticipant,
        config = config,
    )

    override fun buildLeaderboardConfig(
        sellerMessage: String,
        winnerMessage: String,
        winnerDetail: String,
        loserMessage: String,
        loserDetail: String,
    ) = PlayLeaderboardConfigUiModel(
        sellerMessage = sellerMessage,
        winnerMessage = winnerMessage,
        winnerDetail = winnerDetail,
        loserMessage = loserMessage,
        loserDetail = loserDetail,
    )

    override fun buildLeaderboardDetails(
        title: String,
        winners: List<PlayWinnerUiModel>,
        choices: List<QuizChoicesUiModel>,
        otherParticipantText: String,
        otherParticipant: Long,
        emptyLeaderBoardCopyText: String,
        reward: String,
        leaderBoardType: LeadeboardType,
    ) = PlayLeaderboardUiModel(
        title = title,
        winners = winners,
        choices = choices,
        otherParticipantText = otherParticipantText,
        otherParticipant = otherParticipant,
        emptyLeaderBoardCopyText = emptyLeaderBoardCopyText,
        reward = reward,
        leaderBoardType = leaderBoardType,
        id = "1"
    )

    override fun buildWinner(
        rank: Int,
        id: String,
        name: String,
        imageUrl: String,
        allowChat: () -> Boolean,
        topChatMessage: String,
    ) = PlayWinnerUiModel(
        rank = rank,
        id = id,
        name = name,
        imageUrl = imageUrl,
        allowChat = allowChat,
        topChatMessage = topChatMessage,
    )

    override fun buildQuizChoices(
        index: Int,
        id: String,
        text: String,
        type: PlayQuizOptionState,
        isLoading: Boolean,
        interactiveId: String,
        interactiveTitle: String,
    ) = QuizChoicesUiModel(
        index = index,
        id = id,
        text = text,
        type = type,
        isLoading = isLoading,
        interactiveId = interactiveId,
        interactiveTitle = interactiveTitle,
    )

    override fun buildGiveaway(
        id: String,
        title: String,
        waitingDuration: Long,
        status: InteractiveUiModel.Giveaway.Status,
    ) = InteractiveUiModel.Giveaway(
        id = id,
        title = title,
        waitingDuration = waitingDuration,
        status = status,
    )

    override fun buildQuiz(
        id: String,
        title: String,
        waitingDuration: Long,
        status: InteractiveUiModel.Quiz.Status,
        listOfChoices: List<QuizChoicesUiModel>,
        reward: String,
    ) = InteractiveUiModel.Quiz(
        id = id,
        title = title,
        waitingDuration = waitingDuration,
        status = status,
        listOfChoices = listOfChoices,
        reward = reward,
    )
}