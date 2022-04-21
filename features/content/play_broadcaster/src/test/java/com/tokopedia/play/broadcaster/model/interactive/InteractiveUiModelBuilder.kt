package com.tokopedia.play.broadcaster.model.interactive

import com.tokopedia.play.broadcaster.ui.model.interactive.InteractiveConfigUiModel
import com.tokopedia.play_common.model.dto.interactive.InteractiveType
import com.tokopedia.play_common.model.dto.interactive.PlayCurrentInteractiveModel
import com.tokopedia.play_common.model.dto.interactive.PlayInteractiveTimeStatus
import com.tokopedia.play_common.model.ui.PlayLeaderboardConfigUiModel
import com.tokopedia.play_common.model.ui.PlayLeaderboardInfoUiModel
import com.tokopedia.play_common.model.ui.PlayLeaderboardUiModel
import com.tokopedia.play_common.model.ui.PlayWinnerUiModel

/**
 * Created By : Jonathan Darwin on February 21, 2022
 */
class InteractiveUiModelBuilder {

    fun buildLeaderboardInfoModel(
        leaderboardWinners: List<PlayLeaderboardUiModel> = buildLeaderboardWinnerList(3, 3),
        totalParticipant: String = "1",
        config: PlayLeaderboardConfigUiModel = buildLeaderboardConfigModel(),
    ) = PlayLeaderboardInfoUiModel(
        leaderboardWinners = leaderboardWinners,
        totalParticipant = totalParticipant,
        config = config,
    )

    fun buildLeaderboardWinnerList(
        size: Int,
        winnerSize: Int,
    ) = List(size) {
        PlayLeaderboardUiModel(
            title = "Giveaway $it",
            winners = buildWinnerList(winnerSize),
            otherParticipantText = "",
            otherParticipant = 0,
        )
    }

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
        isActive: Boolean = true,
        nameGuidelineHeader: String = "",
        nameGuidelineDetail: String = "",
        timeGuidelineHeader: String = "",
        timeGuidelineDetail: String = "",
        durationInMs: Long = 1000,
        availableStartTimeInMs: List<Long> = List(5) { it.toLong() },
    ) = InteractiveConfigUiModel(
        isActive = isActive,
        nameGuidelineHeader = nameGuidelineHeader,
        nameGuidelineDetail = nameGuidelineDetail,
        timeGuidelineHeader = timeGuidelineHeader,
        timeGuidelineDetail = timeGuidelineDetail,
        durationInMs = durationInMs,
        availableStartTimeInMs = availableStartTimeInMs,
    )

    fun buildCurrentInteractiveModel(
        id: Long = 0L,
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