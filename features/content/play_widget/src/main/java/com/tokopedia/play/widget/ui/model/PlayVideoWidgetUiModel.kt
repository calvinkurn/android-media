package com.tokopedia.play.widget.ui.model

/**
 * Created by kenny.hadisaputra on 19/10/23
 */
data class PlayVideoWidgetUiModel(
    val id: String,
    val totalView: String,
    val title: String,
    val avatarUrl: String,
    val partnerName: String,
    val coverUrl: String,
    val videoUrl: String = "",
    val badgeUrl: String = "",
    val isMuted: Boolean = true,
    val isLive: Boolean = false,
)
