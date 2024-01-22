package com.tokopedia.play.widget.ui.model

import kotlin.time.Duration

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
    val isLive: Boolean = false,
    val isAutoPlay: Boolean = false,
    val duration: Duration = Duration.INFINITE,
    val shopAppLink: String = ""
) {
    companion object {
        val Empty = PlayVideoWidgetUiModel(
            id = "",
            totalView = "",
            title = "",
            avatarUrl = "",
            partnerName = "",
            coverUrl = ""
        )
    }
}
