package com.tokopedia.checkout.analytics

import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventCategory
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventName
import com.tokopedia.purchase_platform.common.analytics.TransactionAnalytics

object CheckoutScheduleDeliveryAnalytics : TransactionAnalytics() {

    // event action
    private const val VIEW_SCHEDULED_DELIVERY_WIDGET_ON_TOKOPEDIA_NOW = "view scheduled delivery widget on tokopedia now"

    // tracker id
    private const val VIEW_SCHEDULED_DELIVERY_WIDGET_ON_TOKOPEDIA_NOW_TRACKER_ID = "40258"

    fun sendViewScheduledDeliveryWidgetOnTokopediaNowEvent() {
        val gtmData = getGtmData(
            EventName.VIEW_PP_IRIS,
            EventCategory.COURIER_SELECTION,
            VIEW_SCHEDULED_DELIVERY_WIDGET_ON_TOKOPEDIA_NOW,
            ""
        )

        gtmData[ConstantTransactionAnalytics.ExtraKey.BUSINESS_UNIT] =
            ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ConstantTransactionAnalytics.ExtraKey.CURRENT_SITE] =
            ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        gtmData[ConstantTransactionAnalytics.ExtraKey.TRACKER_ID] =
            VIEW_SCHEDULED_DELIVERY_WIDGET_ON_TOKOPEDIA_NOW_TRACKER_ID

        sendGeneralEvent(gtmData)
    }
}
