package com.tokopedia.seller_migration_common.analytics

import com.tokopedia.seller_migration_common.analytics.SellerMigrationTrackingConstants.EVENT_CATEGORY_MIGRATION_PAGE
import com.tokopedia.seller_migration_common.analytics.SellerMigrationTrackingConstants.EVENT_CLICK_SELLER_MIGRATION
import com.tokopedia.seller_migration_common.analytics.SellerMigrationTrackingConstants.EVENT_CLICK_SELLER_NOTIFICATION
import com.tokopedia.seller_migration_common.analytics.SellerMigrationTrackingConstants.EVENT_CLICK_SHOP_ACCOUNT
import com.tokopedia.seller_migration_common.analytics.SellerMigrationTrackingConstants.EVENT_CLICK_TOKOPEDIA_SELLER
import com.tokopedia.seller_migration_common.analytics.SellerMigrationTrackingConstants.EVENT_CONTENT_FEED_SHOP_PAGE
import com.tokopedia.seller_migration_common.analytics.SellerMigrationTrackingConstants.EVENT_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.seller_migration_common.analytics.SellerMigrationTrackingConstants.KEY_CUSTOM_DIMENSION_BUSINESS_UNIT
import com.tokopedia.seller_migration_common.analytics.SellerMigrationTrackingConstants.KEY_CUSTOM_DIMENSION_CURRENT_SITE
import com.tokopedia.seller_migration_common.analytics.SellerMigrationTrackingConstants.TRACKING_USER_ID
import com.tokopedia.seller_migration_common.analytics.SellerMigrationTrackingConstants.VALUE_CUSTOM_DIMENSION_BUSINESS_UNIT_PG
import com.tokopedia.seller_migration_common.analytics.SellerMigrationTrackingConstants.VALUE_CUSTOM_DIMENSION_CURRENT_SITE
import com.tokopedia.seller_migration_common.analytics.SellerMigrationTrackingConstants.VALUE_SETTINGS
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

object SellerMigrationTracking {

    fun eventGoToSellerApp(userId: String, eventAction: String) {
        trackSellerMigrationEvent(SellerMigrationTrackingMap(eventAction, SellerMigrationTrackingConstants.EVENT_LABEL_TO_SELLER_APP, userId))
    }

    fun eventGoToPlayStore(userId: String, eventAction: String) {
        trackSellerMigrationEvent(SellerMigrationTrackingMap(eventAction, SellerMigrationTrackingConstants.EVENT_LABEL_TO_APP_STORE, userId))
    }

    fun eventUserRedirection(eventName: String, eventCategory: String, eventAction: String, eventLabel: String, screenName: String, userId: String, bu: String) {
        val data = mutableMapOf(
                TrackAppUtils.EVENT to eventName,
                TrackAppUtils.EVENT_CATEGORY to eventCategory,
                TrackAppUtils.EVENT_ACTION to eventAction,
                TrackAppUtils.EVENT_LABEL to eventLabel,
                "screenName" to screenName,
                KEY_CUSTOM_DIMENSION_CURRENT_SITE to VALUE_CUSTOM_DIMENSION_CURRENT_SITE,
                TRACKING_USER_ID to userId
        )

        if (bu.isNotBlank()) {
            data[KEY_CUSTOM_DIMENSION_BUSINESS_UNIT] = bu
        }

        TrackApp.getInstance().gtm.sendGeneralEvent(data.toMap())
    }

    fun eventClickSellerFeatureTab(tabName: String, screenName: String, userId: String) {
        val data = mapOf(
                TrackAppUtils.EVENT to EVENT_CLICK_SELLER_MIGRATION,
                TrackAppUtils.EVENT_CATEGORY to EVENT_CATEGORY_MIGRATION_PAGE,
                TrackAppUtils.EVENT_ACTION to "click $tabName tab",
                TrackAppUtils.EVENT_LABEL to "",
                "screenName" to screenName,
                KEY_CUSTOM_DIMENSION_CURRENT_SITE to VALUE_CUSTOM_DIMENSION_CURRENT_SITE,
                TRACKING_USER_ID to userId,
                KEY_CUSTOM_DIMENSION_BUSINESS_UNIT to VALUE_CUSTOM_DIMENSION_BUSINESS_UNIT_PG
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun eventClickGoToPlayStore(screenName: String, userId: String) {
        val data = mapOf(
                TrackAppUtils.EVENT to EVENT_CLICK_SELLER_MIGRATION,
                TrackAppUtils.EVENT_CATEGORY to EVENT_CATEGORY_MIGRATION_PAGE,
                TrackAppUtils.EVENT_ACTION to "click tokopedia seller",
                TrackAppUtils.EVENT_LABEL to "",
                "screenName" to screenName,
                KEY_CUSTOM_DIMENSION_CURRENT_SITE to VALUE_CUSTOM_DIMENSION_CURRENT_SITE,
                TRACKING_USER_ID to userId,
                KEY_CUSTOM_DIMENSION_BUSINESS_UNIT to VALUE_CUSTOM_DIMENSION_BUSINESS_UNIT_PG
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun eventClickLearnMore(screenName: String, userId: String) {
        val data = mapOf(
                TrackAppUtils.EVENT to EVENT_CLICK_SELLER_MIGRATION,
                TrackAppUtils.EVENT_CATEGORY to EVENT_CATEGORY_MIGRATION_PAGE,
                TrackAppUtils.EVENT_ACTION to "click here - learn more",
                TrackAppUtils.EVENT_LABEL to "",
                "screenName" to screenName,
                KEY_CUSTOM_DIMENSION_CURRENT_SITE to VALUE_CUSTOM_DIMENSION_CURRENT_SITE,
                TRACKING_USER_ID to userId,
                KEY_CUSTOM_DIMENSION_BUSINESS_UNIT to VALUE_CUSTOM_DIMENSION_BUSINESS_UNIT_PG
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun trackOpenScreenSellerMigration(screenName: String) {
        val customDimension = mapOf(
                KEY_CUSTOM_DIMENSION_CURRENT_SITE to VALUE_CUSTOM_DIMENSION_CURRENT_SITE,
                KEY_CUSTOM_DIMENSION_BUSINESS_UNIT to VALUE_CUSTOM_DIMENSION_BUSINESS_UNIT_PG
        )
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName, customDimension)
    }

    private fun trackSellerMigrationEvent(sellerMigrationTrackingMap: SellerMigrationTrackingMap) {
        TrackApp.getInstance().gtm.sendGeneralEvent(sellerMigrationTrackingMap.dataTracking)
    }

    fun trackClickShopAccount(userId: String) {
        val data = mapOf(
                TrackAppUtils.EVENT to EVENT_CLICK_SHOP_ACCOUNT,
                TrackAppUtils.EVENT_CATEGORY to EVENT_CONTENT_FEED_SHOP_PAGE,
                TrackAppUtils.EVENT_ACTION to EVENT_CLICK_TOKOPEDIA_SELLER,
                TrackAppUtils.EVENT_LABEL to "",
                KEY_CUSTOM_DIMENSION_CURRENT_SITE to EVENT_TOKOPEDIA_MARKET_PLACE,
                TRACKING_USER_ID to userId,
                KEY_CUSTOM_DIMENSION_BUSINESS_UNIT to VALUE_CUSTOM_DIMENSION_BUSINESS_UNIT_PG
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun trackClickNotificationSeller(userId: String) {
        val data = mapOf(
                TrackAppUtils.EVENT to EVENT_CLICK_SHOP_ACCOUNT,
                TrackAppUtils.EVENT_CATEGORY to  VALUE_SETTINGS,
                TrackAppUtils.EVENT_ACTION to EVENT_CLICK_SELLER_NOTIFICATION,
                TrackAppUtils.EVENT_LABEL to "",
                KEY_CUSTOM_DIMENSION_CURRENT_SITE to EVENT_TOKOPEDIA_MARKET_PLACE,
                TRACKING_USER_ID to userId,
                KEY_CUSTOM_DIMENSION_BUSINESS_UNIT to VALUE_CUSTOM_DIMENSION_BUSINESS_UNIT_PG
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }
}