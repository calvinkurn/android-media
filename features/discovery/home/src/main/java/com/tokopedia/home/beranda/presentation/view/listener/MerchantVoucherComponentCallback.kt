package com.tokopedia.home.beranda.presentation.view.listener

import com.tokopedia.home.analytics.v2.MerchantVoucherTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.listener.MerchantVoucherComponentListener
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselMerchantVoucherDataModel
import com.tokopedia.track.TrackApp

/**
 * Created by dhaba
 */
class MerchantVoucherComponentCallback(val homeCategoryListener: HomeCategoryListener) :
    MerchantVoucherComponentListener {

    override fun onMerchantImpressed(element: CarouselMerchantVoucherDataModel, horizontalPosition: Int) {
        homeCategoryListener.getTrackingQueueObj()?.putEETracking(
                MerchantVoucherTracking.getMerchantVoucherView(element, horizontalPosition) as HashMap<String, Any>)
    }

    override fun onProductClicked(element: CarouselMerchantVoucherDataModel, horizontalPosition: Int) {
        val tracking = MerchantVoucherTracking.getClickProduct(element, horizontalPosition)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(tracking.first, tracking.second)
        homeCategoryListener.onDynamicChannelClicked(element.productAppLink)
    }

    override fun onViewAllCardClicked(channelId: String, headerName: String, seeMoreAppLink: String, userId: String, campaignCode: String) {
        val tracking = MerchantVoucherTracking.getClickViewAllCard(headerName, userId, campaignCode)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(tracking.first, tracking.second)
        homeCategoryListener.onDynamicChannelClicked(seeMoreAppLink)
    }

    override fun onViewAllClicked(
        channelId: String,
        headerName: String,
        seeMoreAppLink: String,
        userId: String,
        campaignCode: String
    ) {
        val tracking = MerchantVoucherTracking.getClickViewAll(headerName, userId, campaignCode)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(tracking.first, tracking.second)
        homeCategoryListener.onDynamicChannelClicked(seeMoreAppLink)
    }

    override fun onShopClicked(element: CarouselMerchantVoucherDataModel, horizontalPosition: Int) {
        val tracking = MerchantVoucherTracking.getShopClicked(element, horizontalPosition)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(tracking.first, tracking.second)
        homeCategoryListener.onDynamicChannelClicked(element.shopAppLink)
    }

    override fun getUserId(): String {
        return homeCategoryListener.userId
    }
}
