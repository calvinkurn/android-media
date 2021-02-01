package com.tokopedia.ordermanagement.snapshot.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

/**
 * Created by fwidjaja on 2/1/21.
 */
object SnapshotAnalytics {
    private const val CLICK_BOM = "openScreen"
    private const val BOM_PRODUCT_SNAPSHOT_PAGE = "bom product snapshot page"
    private const val CLICK_PRODUCT_NAME = "click product name"
    private const val CLICK_SHOP_PAGE = "click shop page"
    private const val CURRENT_SITE = "currentSite"
    private const val BUSINESS_UNIT = "businessUnit"
    private const val USER_ID = "userId"
    private const val PRODUCT_ID = "productId"
    private const val SHOP_ID = "shopId"
    private const val MAIN_APP_ANDROID = "main app android"
    private const val MARKETPLACE = "marketplace"

    fun clickLihatHalamanProduk(productId: String, userId: String) {
        val event = TrackAppUtils.gtmData(CLICK_BOM,
                BOM_PRODUCT_SNAPSHOT_PAGE, CLICK_PRODUCT_NAME, "")
        event[CURRENT_SITE] = MAIN_APP_ANDROID
        event[USER_ID] = userId
        event[PRODUCT_ID] = productId
        event[BUSINESS_UNIT] = MARKETPLACE

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickShopPage(shopId: String, userId: String) {
        val event = TrackAppUtils.gtmData(CLICK_BOM,
                BOM_PRODUCT_SNAPSHOT_PAGE, CLICK_SHOP_PAGE, "")
        event[CURRENT_SITE] = MAIN_APP_ANDROID
        event[USER_ID] = userId
        event[SHOP_ID] = shopId
        event[BUSINESS_UNIT] = MARKETPLACE

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }
}