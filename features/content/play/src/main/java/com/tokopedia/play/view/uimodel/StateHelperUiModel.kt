package com.tokopedia.play.view.uimodel

import com.tokopedia.play.view.type.BottomInsetsState
import com.tokopedia.play.view.type.BottomInsetsType
import com.tokopedia.play.view.type.PlayChannelType

/**
 * Created by jegul on 18/03/20
 */
data class StateHelperUiModel(
        val shouldShowPinned: Boolean,
        val channelType: PlayChannelType,
        val bottomInsets: Map<BottomInsetsType, BottomInsetsState>
)