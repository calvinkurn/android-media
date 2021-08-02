package com.tokopedia.play.analytic.interactive

import com.tokopedia.play.view.type.PlayChannelType

/**
 * Created by jegul on 09/07/21
 */
interface PlayInteractiveAnalytic {

    fun clickFollowShopInteractive(channelId: String, channelType: PlayChannelType)

    fun clickWinnerBadge(channelId: String, channelType: PlayChannelType)

    fun clickTapTap(channelId: String, channelType: PlayChannelType)
}