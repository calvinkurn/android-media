package com.tokopedia.product.estimasiongkir.util

import com.tokopedia.product.detail.common.ProductTrackingConstant
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

/**
 * Created by Yehezkiel on 02/03/21
 */
object ProductDetailShippingTracking {

    fun onPelajariTokoCabangClicked(userId: String) {
        val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_COURIER,
                ProductTrackingConstant.Category.PRODUCT_DETAIL_PAGE_SHIPPING,
                ProductTrackingConstant.Action.CLICK_PELAJARI_TOKO_CABANG,
                "")
        mapEvent[ProductTrackingConstant.Tracking.KEY_BUSINESS_UNIT] = ProductTrackingConstant.Tracking.BUSINESS_UNIT_PDP
        mapEvent[ProductTrackingConstant.Tracking.KEY_CURRENT_SITE] = ProductTrackingConstant.Tracking.CURRENT_SITE
        mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT] = userId
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

}