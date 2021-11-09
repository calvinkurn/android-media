package com.tokopedia.play.analytic.partner

import com.tokopedia.play.view.type.PlayChannelType

/**
 * Created by jegul on 06/07/21
 */
interface PlayPartnerAnalytic {

    fun clickFollowShop(channelId: String, channelType: PlayChannelType, shopId: String, action: String)

    fun clickShop(channelId: String, channelType: PlayChannelType, shopId: String)
}