package com.tokopedia.play.broadcaster.model.interactive

import com.tokopedia.play.broadcaster.ui.model.interactive.GiveawayConfigUiModel
import com.tokopedia.play.broadcaster.ui.model.interactive.InteractiveConfigUiModel
import com.tokopedia.play.broadcaster.ui.model.interactive.QuizConfigUiModel
import com.tokopedia.play_common.model.dto.interactive.InteractiveType
import com.tokopedia.play_common.model.dto.interactive.GameUiModel
import com.tokopedia.play_common.model.dto.interactive.PlayCurrentInteractiveModel
import com.tokopedia.play_common.model.dto.interactive.PlayInteractiveTimeStatus
import com.tokopedia.play_common.model.ui.LeaderboardGameUiModel
import com.tokopedia.play_common.model.ui.PlayLeaderboardConfigUiModel
import com.tokopedia.play_common.model.ui.PlayWinnerUiModel
import com.tokopedia.play_common.model.ui.QuizChoicesUiModel

/**
 * Created By : Jonathan Darwin on February 21, 2022
 */
class InteractiveUiModelBuilder {

    fun buildLeaderboardInfoModel(
       list : List<LeaderboardGameUiModel>
    ) = list

    fun buildWinnerList(
        size: Int
    ) = List(size) {
        buildWinnerModel(rank = it)
    }

    fun buildWinnerModel(
        rank: Int = 1,
        id: String = "",
        name: String = "",
        imageUrl: String = "",
        allowChat: () -> Boolean = { false },
        topChatMessage: String = "",
    ) = PlayWinnerUiModel(
        rank = rank,
        id = id,
        name = name,
        imageUrl = imageUrl,
        allowChat = allowChat,
        topChatMessage = topChatMessage,
    )

    fun buildLeaderboardConfigModel(
        sellerMessage: String = "",
        winnerMessage: String = "",
        winnerDetail: String = "",
        loserMessage: String = "",
        loserDetail: String = "",
    ) = PlayLeaderboardConfigUiModel(
        sellerMessage = sellerMessage,
        winnerMessage = winnerMessage,
        winnerDetail = winnerDetail,
        loserMessage = loserMessage,
        loserDetail = loserDetail,
    )

    fun buildInteractiveConfigModel(
        giveawayConfig: GiveawayConfigUiModel = buildGiveawayConfig(),
        quizConfig: QuizConfigUiModel = buildQuizConfig(),
    ) = InteractiveConfigUiModel(
        giveawayConfig = giveawayConfig,
        quizConfig = quizConfig,
    )

    fun buildGiveawayConfig(
        isActive: Boolean = true,
        nameGuidelineHeader: String = "",
        nameGuidelineDetail: String = "",
        timeGuidelineHeader: String = "",
        timeGuidelineDetail: String = "",
        durationInMs: Long = 1000,
        availableStartTimeInMs: List<Long> = List(5) { it.toLong() },
    ) = GiveawayConfigUiModel(
        isActive = isActive,
        nameGuidelineHeader = nameGuidelineHeader,
        nameGuidelineDetail = nameGuidelineDetail,
        timeGuidelineHeader = timeGuidelineHeader,
        timeGuidelineDetail = timeGuidelineDetail,
        durationInMs = durationInMs,
        availableStartTimeInMs = availableStartTimeInMs,
    )

    fun buildQuizConfig(
        isActive: Boolean = true,
        maxTitleLength: Int = 50,
        maxChoicesCount: Int = 3,
        minChoicesCount: Int = 1,
        maxChoiceLength: Int = 5,
        availableStartTimeInMs: List<Long> = List(5) { it.toLong() },
        eligibleStartTimeInMs: List<Long> = List(5) { it.toLong() },
        showPrizeCoachMark: Boolean = true,
        isGiftActive: Boolean = false,
    ) = QuizConfigUiModel(
        isActive = isActive,
        maxTitleLength = maxTitleLength,
        maxChoicesCount = maxChoicesCount,
        minChoicesCount = minChoicesCount,
        maxChoiceLength = maxChoiceLength,
        availableStartTimeInMs = availableStartTimeInMs,
        eligibleStartTimeInMs = eligibleStartTimeInMs,
        isGiftActive = isGiftActive,
    )

    fun buildGiveaway(
        id: String = "",
        title: String = "",
        waitingDuration: Long = 200L,
        status: GameUiModel.Giveaway.Status = GameUiModel.Giveaway.Status.Unknown,
    ) = GameUiModel.Giveaway(
        id = id,
        title = title,
        waitingDuration = waitingDuration,
        status = status,
    )

    fun buildQuiz(
        id: String = "",
        title: String = "",
        waitingDuration: Long = 200L,
        status: GameUiModel.Quiz.Status = GameUiModel.Quiz.Status.Unknown,
        listOfChoices: List<QuizChoicesUiModel> = emptyList(),
    ) = GameUiModel.Quiz(
        id = id,
        title = title,
        waitingDuration = waitingDuration,
        status = status,
        listOfChoices = listOfChoices,
    )

    fun buildCurrentInteractiveModel(
        id: String = "",
        type: InteractiveType = InteractiveType.Unknown,
        title: String = "",
        timeStatus: PlayInteractiveTimeStatus = PlayInteractiveTimeStatus.Unknown,
        endGameDelayInMs: Long = 0L
    ) = PlayCurrentInteractiveModel(
        id = id,
        type = type,
        title = title,
        timeStatus = timeStatus,
        endGameDelayInMs = endGameDelayInMs,
    )
}
