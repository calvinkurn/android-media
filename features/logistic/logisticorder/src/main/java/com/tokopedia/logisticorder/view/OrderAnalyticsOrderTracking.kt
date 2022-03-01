package com.tokopedia.logisticorder.view

import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics
import com.tokopedia.purchase_platform.common.analytics.TransactionAnalytics

/**
 * @author anggaprasetiyo on 18/07/18.
 */
object OrderAnalyticsOrderTracking : TransactionAnalytics() {
    fun eventViewLabelTungguRetry(countDownDuration: String, orderId: String) {
        sendGeneralEvent(
                ConstantTransactionAnalytics.EventName.VIEW_SOM,
                ConstantTransactionAnalytics.EventCategory.TRACK_SOM,
                ConstantTransactionAnalytics.EventAction.VIEW_TUNGGU_CARI_DRIVER,
                "$countDownDuration - $orderId"
        )
    }

    fun eventViewButtonCariDriver(orderId: String) {
        sendGeneralEvent(
                ConstantTransactionAnalytics.EventName.VIEW_SOM,
                ConstantTransactionAnalytics.EventCategory.TRACK_SOM,
                ConstantTransactionAnalytics.EventAction.VIEW_BUTTON_CARI_DRIVER,
                orderId
        )
    }

    fun eventClickButtonCariDriver(orderId: String) {
        sendGeneralEvent(
                ConstantTransactionAnalytics.EventName.VIEW_SOM,
                ConstantTransactionAnalytics.EventCategory.TRACK_SOM,
                ConstantTransactionAnalytics.EventAction.CLICK_BUTTON_CARI_DRIVER,
                orderId
        )
    }
}