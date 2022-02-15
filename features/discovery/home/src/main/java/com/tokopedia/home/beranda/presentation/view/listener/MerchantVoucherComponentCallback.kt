package com.tokopedia.home.beranda.presentation.view.listener

import com.tokopedia.home.analytics.v2.MerchantVoucherTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.listener.MerchantVoucherComponentListener
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.merchantvoucher.MerchantVoucherDetailClicked
import com.tokopedia.home_component.model.merchantvoucher.MerchantVoucherShopClicked

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

    override fun onShopClicked(merchantVoucherShopClicked: MerchantVoucherShopClicked) {
        homeCategoryListener.onDynamicChannelClicked(merchantVoucherShopClicked.shopAppLink)
        val tracking = MerchantVoucherTracking.getShopClicked(merchantVoucherShopClicked)
        homeCategoryListener.sendTrackingBundle(tracking.first, tracking.second)
    }

    override fun getUserId(): String {
        return homeCategoryListener.userId
    }

    override fun onVoucherDetailClicked(merchantVoucherDetailClicked: MerchantVoucherDetailClicked) {
        homeCategoryListener.onDynamicChannelClicked(merchantVoucherDetailClicked.productAppLink)
        val tracking = MerchantVoucherTracking.getClickVoucherDetail(merchantVoucherDetailClicked)
        homeCategoryListener.sendTrackingBundle(tracking.first, tracking.second)
    }
}