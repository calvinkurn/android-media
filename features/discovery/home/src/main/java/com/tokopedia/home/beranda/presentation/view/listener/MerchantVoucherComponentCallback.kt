package com.tokopedia.home.beranda.presentation.view.listener

import com.tokopedia.home.analytics.v2.MerchantVoucherTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.listener.MerchantVoucherComponentListener
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.merchantvoucher.MerchantVoucherDetailClicked
import com.tokopedia.home_component.model.merchantvoucher.MerchantVoucherProductClicked
import com.tokopedia.home_component.model.merchantvoucher.MerchantVoucherShopClicked

/**
 * Created by dhaba
 */
class MerchantVoucherComponentCallback(val homeCategoryListener: HomeCategoryListener) :
    MerchantVoucherComponentListener {

    override fun onMerchantImpressed(channel: ChannelModel, parentPos: Int) {
        TODO("Not yet implemented")
    }

    override fun onProductClicked(merchantVoucherProductClicked: MerchantVoucherProductClicked) {
        val tracking = MerchantVoucherTracking.getClickProduct(merchantVoucherProductClicked)
        homeCategoryListener.sendTrackingBundle(tracking.first, tracking.second)
        homeCategoryListener.onDynamicChannelClicked(merchantVoucherProductClicked.productAppLink)
    }

    override fun onViewAllClicked(headerName: String, seeMoreAppLink: String) {
        val tracking = MerchantVoucherTracking.getClickViewAll(headerName)
        homeCategoryListener.sendTrackingBundle(tracking.first, tracking.second)
        homeCategoryListener.onDynamicChannelClicked(seeMoreAppLink)
    }

    override fun onShopClicked(merchantVoucherShopClicked: MerchantVoucherShopClicked) {
        val tracking = MerchantVoucherTracking.getShopClicked(merchantVoucherShopClicked)
        homeCategoryListener.sendTrackingBundle(tracking.first, tracking.second)
        homeCategoryListener.onDynamicChannelClicked(merchantVoucherShopClicked.shopAppLink)
    }

    override fun getUserId(): String {
        return homeCategoryListener.userId
    }

    override fun onVoucherDetailClicked(merchantVoucherDetailClicked: MerchantVoucherDetailClicked) {
        val tracking = MerchantVoucherTracking.getClickVoucherDetail(merchantVoucherDetailClicked)
        homeCategoryListener.sendTrackingBundle(tracking.first, tracking.second)
        homeCategoryListener.onDynamicChannelClicked(merchantVoucherDetailClicked.productAppLink)
    }
}