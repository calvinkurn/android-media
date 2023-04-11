package com.tokopedia.officialstore.official.presentation.listener

import com.tokopedia.home_component.listener.MerchantVoucherComponentListener
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselMerchantVoucherDataModel
import com.tokopedia.officialstore.analytics.OSMerchantVoucherTracking
import com.tokopedia.officialstore.official.presentation.dynamic_channel.DynamicChannelEventHandler
import com.tokopedia.track.TrackApp

/**
 * Created by dhaba
 */
class OSMerchantVoucherCallback(private val dcEventHandler: DynamicChannelEventHandler): MerchantVoucherComponentListener {
    override fun onViewAllCardClicked(channelId: String, headerName: String, seeMoreAppLink: String, userId: String, campaignCode: String) {
        val tracking = OSMerchantVoucherTracking.getClickViewAll(channelId, headerName, userId, dcEventHandler.getOSCategory()?.title ?: "")
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(tracking.first, tracking.second)
        dcEventHandler.goToApplink(seeMoreAppLink)
    }

    override fun onViewAllClicked(
        channelId: String,
        headerName: String,
        seeMoreAppLink: String,
        userId: String,
        campaignCode: String
    ) {
        onViewAllCardClicked(channelId, headerName, seeMoreAppLink, userId, campaignCode)
    }

    override fun onShopClicked(element: CarouselMerchantVoucherDataModel, horizontalPosition: Int) {
        val tracking = OSMerchantVoucherTracking.getShopClicked(
            element,
            horizontalPosition,
            dcEventHandler.getOSCategory()?.title ?: ""
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(tracking.first, tracking.second)
        dcEventHandler.goToApplink(element.shopAppLink)
    }

    override fun onMerchantImpressed(element: CarouselMerchantVoucherDataModel, horizontalPosition: Int) {
        dcEventHandler.getTrackingObject()?.trackingQueueObj?.putEETracking(
            OSMerchantVoucherTracking.getMerchantVoucherView(
                element,
                horizontalPosition,
                dcEventHandler.getOSCategory()?.title ?: ""
            ) as HashMap<String, Any>
        )
    }

    override fun onProductClicked(element: CarouselMerchantVoucherDataModel, horizontalPosition: Int) {
        val tracking = OSMerchantVoucherTracking.getClickProduct(element, horizontalPosition, dcEventHandler.getOSCategory()?.title ?: "")
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(tracking.first, tracking.second)
        dcEventHandler.goToApplink(element.productAppLink)
    }

    override fun getUserId(): String = dcEventHandler.getUserId()
}
