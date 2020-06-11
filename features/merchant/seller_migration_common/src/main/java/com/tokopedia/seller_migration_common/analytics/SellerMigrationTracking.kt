package com.tokopedia.seller_migration_common.analytics

import com.tokopedia.track.TrackApp

object SellerMigrationTracking {

    fun eventOnClickAccountTicker(userId: String) {
        trackSellerMigrationEvent(SellerMigrationTrackingMap(SellerMigrationTrackingConstants.EVENT_CLICK_ACCOUNT_TICKER, "", userId))
    }

    fun eventOnClickChatTicker(userId: String) {
        trackSellerMigrationEvent(SellerMigrationTrackingMap(SellerMigrationTrackingConstants.EVENT_CLICK_CHAT_TICKER,"", userId))
    }

    fun eventOnClickProductTicker(userId: String) {
        trackSellerMigrationEvent(SellerMigrationTrackingMap(SellerMigrationTrackingConstants.EVENT_CLICK_PRODUCT_TICKER,"", userId))
    }

    fun eventOnClickReviewTicker(userId: String) {
        trackSellerMigrationEvent(SellerMigrationTrackingMap(SellerMigrationTrackingConstants.EVENT_CLICK_REVIEW_TICKER,"", userId))
    }

    fun eventGoToSellerApp(userId: String, eventAction: String) {
        trackSellerMigrationEvent(SellerMigrationTrackingMap(eventAction, SellerMigrationTrackingConstants.EVENT_LABEL_TO_SELLER_APP, userId))
    }

    fun eventGoToPlayStore(userId: String, eventAction: String) {
        trackSellerMigrationEvent(SellerMigrationTrackingMap(eventAction, SellerMigrationTrackingConstants.EVENT_LABEL_TO_APP_STORE, userId))
    }

    private fun trackSellerMigrationEvent(sellerMigrationTrackingMap: SellerMigrationTrackingMap) {
        TrackApp.getInstance().gtm.sendGeneralEvent(sellerMigrationTrackingMap.dataTracking)
    }
}