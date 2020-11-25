package com.tokopedia.seller_migration_common.analytics

class SellerMigrationTrackingMap(action: String, label: String, userId: String) {

    private var tracking: Map<String, String> = HashMap()

    val dataTracking: Map<String, String> get() = tracking

    init {
        with(SellerMigrationTrackingConstants) {
            tracking = mapOf(
                    TRACKING_EVENT to EVENT_CLICK_SELLER_MIGRATION,
                    TRACKING_EVENT_CATEGORY to EVENT_CATEGORY_SELLER_MIGRATION,
                    TRACKING_EVENT_ACTION to action,
                    TRACKING_EVENT_LABEL to label,
                    TRACKING_USER_ID to String.format(USER_ID_VALUE,userId)
            )
        }
    }

}