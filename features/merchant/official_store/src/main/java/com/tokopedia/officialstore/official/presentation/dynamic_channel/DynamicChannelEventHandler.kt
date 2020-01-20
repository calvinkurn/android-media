package com.tokopedia.officialstore.official.presentation.dynamic_channel

import android.view.View
import com.tokopedia.design.countdown.CountDownView
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Channel

interface DynamicChannelEventHandler : CountDownView.CountDownListener {
    // Lego layout event handlers
    fun onClickLegoHeaderActionText(applink: String): View.OnClickListener
    fun onClickLegoImage(channelData: Channel, position: Int): View.OnClickListener
    fun legoImpression(channelData: Channel)

    // Flash Sale layout event handlers
    fun onClickFlashSaleActionText(applink: String): View.OnClickListener
    fun onClickFlashSaleImage(channelData: Channel, position: Int): View.OnClickListener
    fun flashSaleImpression(channelData: Channel)

    // Thematic layout event handlers
    fun onClickMixActionText(applink: String): View.OnClickListener
    fun onClickMixBanner(channelData: Channel): View.OnClickListener
    fun onClickMixImage(channelData: Channel, position: Int): View.OnClickListener
    fun mixImageImpression(channelData: Channel)
    fun mixBannerImpression(channelData: Channel)
}
