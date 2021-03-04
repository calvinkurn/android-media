package com.tokopedia.play.model

import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.uimodel.recom.PlayChannelInfoUiModel

/**
 * Created by jegul on 09/02/21
 */
class PlayChannelInfoModelBuilder {

    fun buildChannelInfo(
            channelType: PlayChannelType = PlayChannelType.Live,
            backgroundUrl: String = "https://www.tokopedia.com"
    ) = PlayChannelInfoUiModel(
            channelType = channelType,
            backgroundUrl = backgroundUrl
    )
}