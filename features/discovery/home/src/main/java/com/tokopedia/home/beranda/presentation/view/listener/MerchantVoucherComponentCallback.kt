package com.tokopedia.home.beranda.presentation.view.listener

import com.tokopedia.home.analytics.v2.MerchantVoucherTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.listener.MerchantVoucherComponentListener
import com.tokopedia.home_component.model.merchantvoucher.MerchantVoucherImpressed
import com.tokopedia.home_component.model.merchantvoucher.MerchantVoucherShopClicked
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselMerchantVoucherDataModel
import com.tokopedia.track.TrackApp

/**
 * Created by dhaba
 */
class MerchantVoucherComponentCallback(val homeCategoryListener: HomeCategoryListener) :
    MerchantVoucherComponentListener {

    override fun onMerchantImpressed(merchantVoucherImpressed: MerchantVoucherImpressed) {
        homeCategoryListener.getTrackingQueueObj()?.putEETracking(
                MerchantVoucherTracking.getMerchantVoucherView(merchantVoucherImpressed) as HashMap<String, Any>)
    }

    override fun onProductClicked(element: CarouselMerchantVoucherDataModel, horizontalPosition: Int) {
        val tracking = MerchantVoucherTracking.getClickProduct(element, horizontalPosition)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(tracking.first, tracking.second)
        homeCategoryListener.onDynamicChannelClicked(element.productAppLink)
    }

    override fun onViewAllClicked(headerName: String, seeMoreAppLink: String, userId: String) {
        val tracking = MerchantVoucherTracking.getClickViewAll(headerName, userId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(tracking.first, tracking.second)
        homeCategoryListener.onDynamicChannelClicked(seeMoreAppLink)
    }

    override fun onShopClicked(merchantVoucherShopClicked: MerchantVoucherShopClicked) {
        val tracking = MerchantVoucherTracking.getShopClicked(merchantVoucherShopClicked)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(tracking.first, tracking.second)
        homeCategoryListener.onDynamicChannelClicked(merchantVoucherShopClicked.shopAppLink)
    }

    override fun getUserId(): String {
        return homeCategoryListener.userId
    }
}