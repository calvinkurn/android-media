package com.tokopedia.play_common.model.ui


/**
 * Created by mzennis on 30/06/21.
 */
data class PlayLeaderboardUiModel(
    val title: String,
    val winners: List<PlayWinnerUiModel>,
    val otherParticipantText: String
)

data class PlayWinnerUiModel(
    val rank: Int,
    val id: String,
    val name: String,
    val imageUrl: String,
    val allowChat: Boolean
)