package com.tokopedia.play.view.uimodel.recom

import com.tokopedia.play.view.type.PlayChannelType

/**
 * Created by jegul on 08/02/21
 */
data class PlayChannelInfoUiModel(
        val id: String = "",
        val channelType: PlayChannelType = PlayChannelType.Unknown,
        val backgroundUrl: String = ""
)