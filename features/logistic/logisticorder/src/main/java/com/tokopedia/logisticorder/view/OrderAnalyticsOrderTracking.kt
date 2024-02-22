package com.tokopedia.logisticorder.view

/**
 * @author anggaprasetiyo on 18/07/18.
 */
object OrderAnalyticsOrderTracking {
    // tech debt
    // this analytic is never hit anymore since find new driver
    // feature is currently unavailable in android's tracking page
    fun eventViewLabelTungguRetry(countDownDuration: String, orderId: String) {
//        sendGeneralEvent(
//            ConstantTransactionAnalytics.EventName.VIEW_SOM,
//            ConstantTransactionAnalytics.EventCategory.TRACK_SOM,
//            ConstantTransactionAnalytics.EventAction.VIEW_TUNGGU_CARI_DRIVER,
//            "$countDownDuration - $orderId"
//        )
    }

    fun eventViewButtonCariDriver(orderId: String) {
//        sendGeneralEvent(
//            ConstantTransactionAnalytics.EventName.VIEW_SOM,
//            ConstantTransactionAnalytics.EventCategory.TRACK_SOM,
//            ConstantTransactionAnalytics.EventAction.VIEW_BUTTON_CARI_DRIVER,
//            orderId
//        )
    }

    fun eventClickButtonCariDriver(orderId: String) {
//        sendGeneralEvent(
//            ConstantTransactionAnalytics.EventName.VIEW_SOM,
//            ConstantTransactionAnalytics.EventCategory.TRACK_SOM,
//            ConstantTransactionAnalytics.EventAction.CLICK_BUTTON_CARI_DRIVER,
//            orderId
//        )
    }
}
