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
interface InteractiveBuilder {

    fun buildLeaderboard(
        data: PlayLeaderboardInfoUiModel = buildLeaderboardInfo(),
        state: ResultState = ResultState.Success,
    ): LeaderboardUiModel

    fun buildLeaderboardInfo(
        leaderboardWinners: List<PlayLeaderboardUiModel> = emptyList(),
        totalParticipant: String = "",
        config: PlayLeaderboardConfigUiModel = buildLeaderboardConfig()
    ): PlayLeaderboardInfoUiModel

    fun buildLeaderboardConfig(
        sellerMessage: String = "",
        winnerMessage: String = "",
        winnerDetail: String = "",
        loserMessage: String = "",
        loserDetail: String = "",
    ): PlayLeaderboardConfigUiModel

    fun buildLeaderboardDetails(
        title: String = "",
        winners: List<PlayWinnerUiModel> = emptyList(),
        choices: List<QuizChoicesUiModel> = emptyList(), //opt = not empty when QUIZ, soon Polling
        otherParticipantText: String = "",
        otherParticipant: Long = 0L,
        emptyLeaderBoardCopyText: String = "",
        reward: String = "",
        leaderBoardType: LeadeboardType = LeadeboardType.Unknown,
    ): PlayLeaderboardUiModel

    fun buildWinner(
        rank: Int = 1,
        id: String = "",
        name: String = "",
        imageUrl: String = "",
        allowChat: () -> Boolean = { false },
        topChatMessage: String = "",
    ): PlayWinnerUiModel

    fun buildQuizChoices(
        index: Int = 0,
        id: String = "",
        text: String = "",
        type: PlayQuizOptionState = PlayQuizOptionState.Unknown,
        isLoading: Boolean = false,
        interactiveId: String = "",
        interactiveTitle: String = "",
    ): QuizChoicesUiModel

    fun buildGiveaway(
        id: String = "",
        title: String = "",
        waitingDuration: Long = 200L,
        status: InteractiveUiModel.Giveaway.Status = InteractiveUiModel.Giveaway.Status.Unknown,
    ): InteractiveUiModel.Giveaway

    fun buildQuiz(
        id: String = "",
        title: String = "",
        waitingDuration: Long = 200L,
        status: InteractiveUiModel.Quiz.Status = InteractiveUiModel.Quiz.Status.Unknown,
        listOfChoices: List<QuizChoicesUiModel> = emptyList(),
        reward: String = "",
    ): InteractiveUiModel.Quiz
}