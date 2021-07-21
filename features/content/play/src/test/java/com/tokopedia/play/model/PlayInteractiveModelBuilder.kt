package com.tokopedia.play.model

import com.tokopedia.play_common.model.ui.PlayLeaderboardConfigUiModel
import com.tokopedia.play_common.model.ui.PlayLeaderboardInfoUiModel
import com.tokopedia.play_common.model.ui.PlayLeaderboardUiModel
import com.tokopedia.play_common.model.ui.PlayWinnerUiModel

/**
 * Created by jegul on 15/07/21
 */
class PlayInteractiveModelBuilder {

    fun buildLeaderboardInfo(
            leaderboardWinners: List<PlayLeaderboardUiModel> = emptyList(),
            totalParticipant: String = "",
            config: PlayLeaderboardConfigUiModel = PlayLeaderboardConfigUiModel()
    ) = PlayLeaderboardInfoUiModel(
            leaderboardWinners = leaderboardWinners,
            totalParticipant = totalParticipant,
            config = config
    )

    fun buildLeaderboard(
            title: String = "",
            winners: List<PlayWinnerUiModel> = emptyList(),
            otherParticipantText: String = ""
    ) = PlayLeaderboardUiModel(
            title = title,
            winners = winners,
            otherParticipantText = otherParticipantText
    )

    fun buildWinner(
            rank: Int = 1,
            id: String = "",
            name: String = "",
            imageUrl: String = "",
            allowChat: () -> Boolean = { false },
            topChatMessage: String = ""
    ) = PlayWinnerUiModel(
            rank = rank,
            id = id,
            name = name,
            imageUrl = imageUrl,
            allowChat = allowChat,
            topChatMessage = topChatMessage
    )
}