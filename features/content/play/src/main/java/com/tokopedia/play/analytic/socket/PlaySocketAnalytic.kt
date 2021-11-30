package com.tokopedia.play.analytic.socket

import com.tokopedia.play.view.type.PlayChannelType

/**
 * Created by jegul on 25/08/21
 */
interface PlaySocketAnalytic {

    fun socketError(channelId: String, channelType: PlayChannelType, error: String)
}