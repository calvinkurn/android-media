package com.tokopedia.home_component.listener

import com.tokopedia.home_component.model.ChannelModel

/**
 * Created by dhaba
 */
interface MerchantVoucherComponentListener  {
    fun onShopClicked(shopAppLink: String)
    fun onMerchantImpressed(channel: ChannelModel, parentPos: Int)
    fun onProductClicked(channel: ChannelModel, parentPos: Int)
    //for see more card click
    fun onSeeMoreCardClicked(channel: ChannelModel, appLink: String)
}