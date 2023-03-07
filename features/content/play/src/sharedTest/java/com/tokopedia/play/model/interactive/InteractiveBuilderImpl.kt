package com.tokopedia.play.model.interactive

import com.tokopedia.play.view.uimodel.recom.interactive.LeaderboardUiModel
import com.tokopedia.play_common.model.dto.interactive.GameUiModel
import com.tokopedia.play_common.model.result.ResultState
import com.tokopedia.play_common.view.game.quiz.PlayQuizOptionState
import com.tokopedia.play_common.model.ui.LeadeboardType
import com.tokopedia.play_common.model.ui.LeaderboardGameUiModel
import com.tokopedia.play_common.model.ui.QuizChoicesUiModel
import java.util.Calendar

/**
 * Created by kenny.hadisaputra on 11/05/22
 */
class InteractiveBuilderImpl : InteractiveBuilder {

    override fun buildLeaderboard(
        data: List<LeaderboardGameUiModel>,
        state: ResultState,
    ) = LeaderboardUiModel(
        data = data,
        state = state,
    )

    override fun buildLeaderBoardContent(data: List<LeaderboardGameUiModel>): List<LeaderboardGameUiModel> = data

    override fun buildHeader(
        title: String,
        reward: String,
        endsIn: Calendar?,
        leaderBoardType: LeadeboardType,
        id: String
    ): LeaderboardGameUiModel.Header = LeaderboardGameUiModel.Header(
        title, reward, endsIn, leaderBoardType, id
    )

    override fun buildWinner(
        rank: Int,
        id: String,
        name: String,
        imageUrl: String,
        allowChat: () -> Boolean,
        topChatMessage: String,
    ) = LeaderboardGameUiModel.Winner(
        rank = rank,
        id = id,
        name = name,
        imageUrl = imageUrl,
        allowChat = allowChat,
        topChatMessage = topChatMessage,
    )

    override fun buildFooter(
        id: String,
        totalParticipant: Long,
        leaderBoardType: LeadeboardType,
        otherParticipantText: String,
        otherParticipant: Long,
        emptyLeaderBoardCopyText: String,
    ): LeaderboardGameUiModel.Footer = LeaderboardGameUiModel.Footer(id, totalParticipant, leaderBoardType, otherParticipantText, otherParticipant, emptyLeaderBoardCopyText)

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
        status: GameUiModel.Giveaway.Status,
    ) = GameUiModel.Giveaway(
        id = id,
        title = title,
        waitingDuration = waitingDuration,
        status = status,
    )

    override fun buildQuiz(
        id: String,
        title: String,
        waitingDuration: Long,
        status: GameUiModel.Quiz.Status,
        listOfChoices: List<QuizChoicesUiModel>,
    ) = GameUiModel.Quiz(
        id = id,
        title = title,
        waitingDuration = waitingDuration,
        status = status,
        listOfChoices = listOfChoices,
    )
}
