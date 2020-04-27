package com.tokopedia.play.view.uimodel

import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play_common.state.TokopediaPlayVideoState

/**
 * Created by jegul on 16/12/19
 */
data class VideoPropertyUiModel(
        val type: PlayChannelType,
        val state: TokopediaPlayVideoState
)