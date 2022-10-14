package com.tokopedia.play.model.interactive

import com.tokopedia.play.view.uimodel.recom.interactive.LeaderboardUiModel
import com.tokopedia.play_common.model.dto.interactive.InteractiveUiModel
import com.tokopedia.play_common.model.result.ResultState
import com.tokopedia.play_common.model.ui.*
import com.tokopedia.play_common.view.game.quiz.PlayQuizOptionState
import java.util.*

/**
 * Created by kenny.hadisaputra on 11/05/22
 */
interface InteractiveBuilder {

    /**
     * LeaderBoard
     */

    fun buildLeaderboard(
        data: List<LeaderboardGameUiModel> = emptyList(),
        state: ResultState = ResultState.Success,
    ): LeaderboardUiModel

    fun buildLeaderBoardContent(data: List<LeaderboardGameUiModel>): List<LeaderboardGameUiModel>

    fun buildHeader(
        title: String ="",
        reward: String = "",
        endsIn: Calendar? = null,
        leaderBoardType: LeadeboardType = LeadeboardType.Unknown,
        id: String = "",
    ): LeaderboardGameUiModel.Header

    fun buildWinner(
        rank: Int = 1,
        id: String = "",
        name: String = "",
        imageUrl: String = "",
        allowChat: () -> Boolean = { false },
        topChatMessage: String = "",
    ): LeaderboardGameUiModel.Winner

    fun buildFooter(
        id: String = "1",
        totalParticipant: Long = 0L,
        leaderBoardType: LeadeboardType = LeadeboardType.Unknown,
        otherParticipantText: String,
        otherParticipant: Long,
        emptyLeaderBoardCopyText: String = "",
    ): LeaderboardGameUiModel.Footer


    /**
     * Interactive
     */
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
    ): InteractiveUiModel.Quiz
}
