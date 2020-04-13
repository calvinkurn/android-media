package com.tokopedia.officialstore.official.presentation.dynamic_channel

import com.tokopedia.officialstore.official.data.model.dynamic_channel.Channel
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Grid


interface OfficialStoreFlashSaleCardListener {
    //flash sale card listener
    fun onFlashSaleCardImpressed(position: Int, channel: Channel)
    fun onFlashSaleCardClicked(position: Int, channel: Channel, grid: Grid, applink: String)
}