package com.tokopedia.play.widget.ui.model

import com.tokopedia.play_common.types.PlayVideoType


/**
 * Created by mzennis on 05/10/20.
 */
data class PlayWidgetCardVideoUiModel(
        val id: String,
        val coverUrl: String,
        val videoUrl: String,
        val isLive: Boolean
)