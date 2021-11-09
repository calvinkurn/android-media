package com.tokopedia.play_common.model.ui


/**
 * Created by mzennis on 30/06/21.
 */
sealed class PlayLeaderboardWrapperUiModel {
    object Loading: PlayLeaderboardWrapperUiModel()
    object Error: PlayLeaderboardWrapperUiModel()
    data class Success(val data: PlayLeaderboardInfoUiModel): PlayLeaderboardWrapperUiModel()
    object Unknown: PlayLeaderboardWrapperUiModel()
}

data class PlayLeaderboardInfoUiModel(
        val leaderboardWinners: List<PlayLeaderboardUiModel> = emptyList(),
        val totalParticipant: String = "",
        val config: PlayLeaderboardConfigUiModel = PlayLeaderboardConfigUiModel()
)

data class PlayLeaderboardUiModel(
    val title: String,
    val winners: List<PlayWinnerUiModel>,
    val otherParticipantText: String,
    val otherParticipant: Long
)

data class PlayWinnerUiModel(
    val rank: Int,
    val id: String,
    val name: String,
    val imageUrl: String,
    val allowChat: () -> Boolean,
    val topChatMessage: String,
)

data class PlayLeaderboardConfigUiModel(
        val sellerMessage: String = "",
        val winnerMessage: String = "",
        val winnerDetail: String = "",
        val loserMessage: String = "",
        val loserDetail: String = "",
)