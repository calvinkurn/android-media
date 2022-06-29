package com.tokopedia.play.analytic.tokonow

import com.tokopedia.play.view.type.PlayChannelType

/**
 * @author by astidhiyaa on 17/06/22
 */
interface PlayTokonowAnalytic {
    fun impressAddressWidget(channelId: String, channelType: PlayChannelType)
    fun impressChooseAddress(channelId: String, channelType: PlayChannelType)
    fun clickChooseAddress(channelId: String, channelType: PlayChannelType)
    fun clickInfoAddressWidget(channelId: String, channelType: PlayChannelType)
    fun impressInfoNow(channelId: String, channelType: PlayChannelType)
    fun clickInfoNow(channelId: String, channelType: PlayChannelType)

    fun impressNowToaster(channelId: String, channelType: PlayChannelType)
    fun clickLihatNowToaster(channelId: String, channelType: PlayChannelType)
}