package com.tokopedia.play.view.uimodel

import com.google.gson.annotations.SerializedName

/**
 * Created By : Jonathan Darwin on October 04, 2021
 */
data class PlayUserWinnerStatusUiModel(
    val channelId: Long = 0L,
    val interactiveId: Long = 0L,
    val userId: Long = 0L,
    val name: String = "",
    val imageUrl: String = "",
    val winnerTitle: String = "",
    val winnerText: String = "",
    val loserTitle: String = "",
    val loserText: String = ""
)