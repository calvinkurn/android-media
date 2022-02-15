package com.tokopedia.home.beranda.presentation.view.listener

import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.listener.MerchantVoucherComponentListener
import com.tokopedia.home_component.model.ChannelModel

/**
 * Created by dhaba
 */
class MerchantVoucherComponentCallback(val homeCategoryListener: HomeCategoryListener) :
    MerchantVoucherComponentListener {

    override fun onMerchantImpressed(channel: ChannelModel, parentPos: Int) {
        TODO("Not yet implemented")
    }

    override fun onProductClicked(productAppLink: String) {
        homeCategoryListener.onDynamicChannelClicked(productAppLink)
    }

    override fun onSeeMoreCardClicked(channel: ChannelModel, appLink: String) {
        TODO("Not yet implemented")
    }

    override fun onShopClicked(shopAppLink: String) {
        homeCategoryListener.onDynamicChannelClicked(shopAppLink)
    }
}