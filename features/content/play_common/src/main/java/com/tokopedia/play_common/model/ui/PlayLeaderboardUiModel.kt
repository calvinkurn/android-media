package com.tokopedia.play_common.model.ui

import com.tokopedia.kotlin.model.ImpressHolder
import java.util.Calendar


/**
 * Created by mzennis on 30/06/21.
 */

sealed class LeaderboardGameUiModel {
    data class Header(
        val title: String,
        val reward: String = "", //only for Giveaway
        val endsIn: Calendar? = null,
        val leaderBoardType: LeadeboardType = LeadeboardType.Unknown,
        val id: String,
        val impressHolder: ImpressHolder = ImpressHolder()
    ) : LeaderboardGameUiModel()

    data class Winner(
        val rank: Int,
        val id: String,
        val name: String,
        val imageUrl: String,
        val allowChat: () -> Boolean,
        val topChatMessage: String,
    ) : LeaderboardGameUiModel()

    data class QuizOption(val option: QuizChoicesUiModel) : LeaderboardGameUiModel()
    data class Footer(
        val id: String,
        val totalParticipant: Long = 0L,
        val leaderBoardType: LeadeboardType = LeadeboardType.Unknown,
        val otherParticipantText: String,
        val otherParticipant: Long,
        val emptyLeaderBoardCopyText: String = "",
    ) : LeaderboardGameUiModel()
}

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
    val loserDetail: String = "",
)

enum class LeadeboardType {
    Giveaway,
    Quiz,
    Polling,
    Unknown,
}
