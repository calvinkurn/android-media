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

/**
 * Fetching from network
 */
data class PlayLeaderboardInfoUiModel(
        val leaderboardWinners: List<PlayLeaderboardUiModel> = emptyList(),
        val totalParticipant: String = "",
        val config: PlayLeaderboardConfigUiModel = PlayLeaderboardConfigUiModel()
)

/***
 * For leaderboard view type
 */
data class PlayLeaderboardUiModel(
    val title: String,
    val winners: List<PlayWinnerUiModel>,
    val choices: List<QuizChoicesUiModel> = emptyList(), //opt = not empty when QUIZ, soon Polling
    val otherParticipantText: String,
    val otherParticipant: Long,
    val emptyLeaderBoardCopyText: String = "",
    val reward: String = "",
    val leaderBoardType: LeadeboardType = LeadeboardType.Unknown
)

/***
 * For inside leaderboard view type; winner
 */
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
        val loserDetail: String = ""
)

enum class LeadeboardType{
    Giveaway,
    Quiz,
    Polling,
    Unknown,
}
