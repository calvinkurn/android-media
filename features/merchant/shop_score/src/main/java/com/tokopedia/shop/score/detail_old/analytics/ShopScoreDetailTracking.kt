package com.tokopedia.shop.score.detail_old.analytics

import com.tokopedia.shop.score.common.analytics.ShopScoreTrackingConstant
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.ContextAnalytics

object ShopScoreDetailTracking {
    private val tracker: ContextAnalytics by lazy { TrackApp.getInstance().gtm }

    fun clickHereTickerOldShopScoreDetail(userId: String, typeShop: String) {
        val mapData = mapOf(
                TrackAppUtils.EVENT to ShopScoreTrackingConstant.CLICK_SHOP_SCORE,
                TrackAppUtils.EVENT_CATEGORY to ShopScoreTrackingConstant.OLD_PERFORMA_TOKO_PAGE,
                TrackAppUtils.EVENT_ACTION to ShopScoreTrackingConstant.CLICK_LEARN_MORE,
                TrackAppUtils.EVENT_LABEL to typeShop,
                ShopScoreTrackingConstant.BUSSINESS_UNIT to ShopScoreTrackingConstant.PHYSICAL_GOODS,
                ShopScoreTrackingConstant.CURRENT_SITE to ShopScoreTrackingConstant.TOKOPEDIA_SELLER,
                ShopScoreTrackingConstant.USER_ID to userId
        )
        tracker.sendGeneralEvent(mapData)
    }
}