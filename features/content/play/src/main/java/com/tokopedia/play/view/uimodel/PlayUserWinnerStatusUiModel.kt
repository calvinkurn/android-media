package com.tokopedia.play.view.uimodel

/**
 * Created By : Jonathan Darwin on October 04, 2021
 */
data class PlayUserWinnerStatusUiModel(
    val channelId: Long,
    val interactiveId: Long,
    val userId: Long,
    val name: String,
    val imageUrl: String,
    val winnerTitle: String,
    val winnerText: String,
    val loserTitle: String,
    val loserText: String
)