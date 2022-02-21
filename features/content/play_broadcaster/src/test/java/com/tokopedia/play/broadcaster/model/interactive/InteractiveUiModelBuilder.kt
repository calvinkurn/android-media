package com.tokopedia.play.broadcaster.model.interactive

import com.tokopedia.play_common.model.ui.PlayLeaderboardConfigUiModel
import com.tokopedia.play_common.model.ui.PlayLeaderboardInfoUiModel
import com.tokopedia.play_common.model.ui.PlayLeaderboardUiModel
import com.tokopedia.play_common.model.ui.PlayWinnerUiModel

/**
 * Created By : Jonathan Darwin on February 21, 2022
 */
class InteractiveUiModelBuilder {

    fun buildLeaderboardInfoModel(
        leaderboardWinners: List<PlayLeaderboardUiModel> = buildLeaderboardWinnerList(3),
        totalParticipant: String = "1",
        config: PlayLeaderboardConfigUiModel = buildConfigModel(),
    ) = PlayLeaderboardInfoUiModel(
        leaderboardWinners = leaderboardWinners,
        totalParticipant = totalParticipant,
        config = config,
    )

    fun buildLeaderboardWinnerList(
        size: Int,
    ) = List(size) {
        PlayLeaderboardUiModel(
            title = "Giveaway $it",
            winners = buildWinnerList(it),
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

    fun buildConfigModel(
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
}