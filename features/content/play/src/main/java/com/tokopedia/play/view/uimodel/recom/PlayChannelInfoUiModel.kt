package com.tokopedia.play.view.uimodel.recom

import com.tokopedia.play.view.type.PlayChannelType

/**
 * Created by jegul on 08/02/21
 */
data class PlayChannelInfoUiModel(
        val channelType: PlayChannelType,
        val backgroundUrl: String
)