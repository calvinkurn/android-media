package com.tokopedia.play.view.uimodel

import com.tokopedia.play.view.type.*
import com.tokopedia.play_common.state.PlayVideoState

/**
 * Created by jegul on 18/03/20
 */
data class StateHelperUiModel(
        val shouldShowPinned: Boolean,
        val channelType: PlayChannelType,
        val videoPlayer: VideoPlayerUiModel,
        val bottomInsets: Map<BottomInsetsType, BottomInsetsState>,
        val screenOrientation: ScreenOrientation,
        val videoOrientation: VideoOrientation,
        val videoState: PlayVideoState
)