package com.tokopedia.officialstore.official.presentation.dynamic_channel

import android.view.View
import com.tokopedia.design.countdown.CountDownView
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Channel

interface DynamicChannelEventHandler : CountDownView.CountDownListener {
    fun onClickLegoHeaderActionText(applink: String): View.OnClickListener
    fun onClickLegoImage(channelData: Channel, position: Int): View.OnClickListener
}
