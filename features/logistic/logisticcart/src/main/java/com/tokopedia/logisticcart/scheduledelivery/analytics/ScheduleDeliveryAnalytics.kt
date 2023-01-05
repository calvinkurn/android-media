package com.tokopedia.logisticcart.scheduledelivery.analytics

import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventCategory
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics.EventName
import com.tokopedia.purchase_platform.common.analytics.TransactionAnalytics

object ScheduleDeliveryAnalytics : TransactionAnalytics() {

    // event action
    private const val CHOOSE_SCHEDULED_DELIVERY_OPTION_RADIO_BUTTON_ON_TOKOPEDIA_NOW = "choose scheduled delivery option radio button on tokopedia now"
    private const val CLICK_2_JAM_TIBA_RADIO_BUTTON_ON_TOKOPEDIA_NOW = "click 2 jam tiba radio button on tokopedia now"
    private const val CLICK_ARROW_IN_SCHEDULED_DELIVERY_OPTIONS_ON_TOKOPEDIA_NOW = "click arrow in scheduled delivery options on tokopedia now"
    private const val VIEW_UNAVAILABLE_SCHEDULED_DELIVERY = "view unavailable scheduled delivery"

    // tracker id
    private const val CHOOSE_SCHEDULED_DELIVERY_OPTION_RADIO_BUTTON_ON_TOKOPEDIA_NOW_TRACKER_ID = "40259"
    private const val CLICK_2_JAM_TIBA_RADIO_BUTTON_ON_TOKOPEDIA_NOW_TRACKER_ID = "40260"
    private const val CLICK_ARROW_IN_SCHEDULED_DELIVERY_OPTIONS_ON_TOKOPEDIA_NOW_TRACKER_ID = "40261"
    private const val VIEW_UNAVAILABLE_SCHEDULED_DELIVERY_TRACKER_ID = "40262"

    fun sendChooseScheduledDeliveryOptionRadioButtonOnTokopediaNowEvent() {
        val gtmData = getGtmData(
            "",
            EventCategory.COURIER_SELECTION,
            CHOOSE_SCHEDULED_DELIVERY_OPTION_RADIO_BUTTON_ON_TOKOPEDIA_NOW,
            ""
        )

        gtmData[ConstantTransactionAnalytics.ExtraKey.BUSINESS_UNIT] =
            ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ConstantTransactionAnalytics.ExtraKey.CURRENT_SITE] =
            ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        gtmData[ConstantTransactionAnalytics.ExtraKey.TRACKER_ID] =
            CHOOSE_SCHEDULED_DELIVERY_OPTION_RADIO_BUTTON_ON_TOKOPEDIA_NOW_TRACKER_ID

        sendGeneralEvent(gtmData)
    }

    fun sendClickJamTibaRadioButtonOnTokopediaNowEvent() {
        val gtmData = getGtmData(
            EventName.CLICK_PP,
            EventCategory.COURIER_SELECTION,
            CLICK_2_JAM_TIBA_RADIO_BUTTON_ON_TOKOPEDIA_NOW,
            ""
        )

        gtmData[ConstantTransactionAnalytics.ExtraKey.BUSINESS_UNIT] =
            ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ConstantTransactionAnalytics.ExtraKey.CURRENT_SITE] =
            ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        gtmData[ConstantTransactionAnalytics.ExtraKey.TRACKER_ID] =
            CLICK_2_JAM_TIBA_RADIO_BUTTON_ON_TOKOPEDIA_NOW_TRACKER_ID

        sendGeneralEvent(gtmData)
    }

    fun sendClickArrowInScheduledDeliveryOptionsOnTokopediaNowEvent() {
        val gtmData = getGtmData(
            EventName.CLICK_PP,
            EventCategory.COURIER_SELECTION,
            CLICK_ARROW_IN_SCHEDULED_DELIVERY_OPTIONS_ON_TOKOPEDIA_NOW,
            ""
        )

        gtmData[ConstantTransactionAnalytics.ExtraKey.BUSINESS_UNIT] =
            ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ConstantTransactionAnalytics.ExtraKey.CURRENT_SITE] =
            ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        gtmData[ConstantTransactionAnalytics.ExtraKey.TRACKER_ID] =
            CLICK_ARROW_IN_SCHEDULED_DELIVERY_OPTIONS_ON_TOKOPEDIA_NOW_TRACKER_ID

        sendGeneralEvent(gtmData)
    }

    fun sendViewUnavailableScheduledDeliveryEvent() {
        val gtmData = getGtmData(
            EventName.VIEW_PP_IRIS,
            EventCategory.COURIER_SELECTION,
            VIEW_UNAVAILABLE_SCHEDULED_DELIVERY,
            ""
        )

        gtmData[ConstantTransactionAnalytics.ExtraKey.BUSINESS_UNIT] =
            ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ConstantTransactionAnalytics.ExtraKey.CURRENT_SITE] =
            ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        gtmData[ConstantTransactionAnalytics.ExtraKey.TRACKER_ID] =
            VIEW_UNAVAILABLE_SCHEDULED_DELIVERY_TRACKER_ID

        sendGeneralEvent(gtmData)
    }
}
