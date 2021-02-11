package com.tokopedia.ordermanagement.snapshot.analytics

import com.tokopedia.config.GlobalConfig
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

/**
 * Created by fwidjaja on 2/1/21.
 */
object SnapshotAnalytics {
    private const val CLICK_BOM = "clickBOM"
    private const val CLICK_SOM = "clickSOM"
    private const val BOM_PRODUCT_SNAPSHOT_PAGE = "bom - product snapshot page"
    private const val SOM_PRODUCT_SNAPSHOT_PAGE = "som - product snapshot page"
    private const val CLICK_PRODUCT_PAGE = "click product page"
    private const val CLICK_SHOP_PAGE = "click shop page"
    private const val CURRENT_SITE = "currentSite"
    private const val BUSINESS_UNIT = "businessUnit"
    private const val USER_ID = "userId"
    private const val PRODUCT_ID = "productId"
    private const val SHOP_ID = "shopId"
    private const val TOKOPEDIA_MARKETPLACE = "tokopediamarketplace"
    private const val MAIN_APP_ANDROID = "main app android"
    private const val SELLER_APP_ANDROID_SOM = "android-sellerapp"
    private const val MAIN_APP_ANDROID_SOM = "android-mainapp"
    private const val MARKETPLACE = "marketplace"
    private const val BUSINESS_UNIT_SOM = "Seller Order Management"


    fun clickLihatHalamanProduk(productId: String, userId: String) {
        val event = TrackAppUtils.gtmData(CLICK_BOM,
                BOM_PRODUCT_SNAPSHOT_PAGE, CLICK_PRODUCT_PAGE, "")
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[USER_ID] = userId
        event[PRODUCT_ID] = productId
        event[BUSINESS_UNIT] = MARKETPLACE

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickShopPage(shopId: String, userId: String) {
        val event = TrackAppUtils.gtmData(CLICK_BOM,
                BOM_PRODUCT_SNAPSHOT_PAGE, CLICK_SHOP_PAGE, "")
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[USER_ID] = userId
        event[SHOP_ID] = shopId
        event[BUSINESS_UNIT] = MARKETPLACE

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickSeeProductPageFromSOM(productId: String, userId: String) {
        val event = TrackAppUtils.gtmData(CLICK_SOM, SOM_PRODUCT_SNAPSHOT_PAGE,
                CLICK_PRODUCT_PAGE, "")
        if(GlobalConfig.isSellerApp()) {
            event[CURRENT_SITE] = SELLER_APP_ANDROID_SOM
        } else {
            event[CURRENT_SITE] = MAIN_APP_ANDROID_SOM
        }
        event[USER_ID] = userId
        event[PRODUCT_ID] = productId
        event[BUSINESS_UNIT] = BUSINESS_UNIT_SOM
        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickShopPageFromSOM(shopId: String, userId: String) {
        val event = TrackAppUtils.gtmData(CLICK_SOM, SOM_PRODUCT_SNAPSHOT_PAGE,
                CLICK_SHOP_PAGE, "")
        if(GlobalConfig.isSellerApp()) {
            event[CURRENT_SITE] = SELLER_APP_ANDROID_SOM
        } else {
            event[CURRENT_SITE] = MAIN_APP_ANDROID_SOM
        }
        event[USER_ID] = userId
        event[SHOP_ID] = shopId
        event[BUSINESS_UNIT] = BUSINESS_UNIT_SOM
        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }
}