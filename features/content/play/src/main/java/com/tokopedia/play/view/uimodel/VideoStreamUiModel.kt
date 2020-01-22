package com.tokopedia.play.view.uimodel

import com.tokopedia.play.view.type.PlayVideoType

/**
 * Created by jegul on 16/12/19
 */
data class VideoStreamUiModel(
        val uriString: String,
        val videoType: PlayVideoType,
        val isActive: Boolean
)