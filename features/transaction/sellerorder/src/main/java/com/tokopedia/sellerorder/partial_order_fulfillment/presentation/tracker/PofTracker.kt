package com.tokopedia.sellerorder.partial_order_fulfillment.presentation.tracker

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import javax.inject.Inject

class PofTracker @Inject constructor(

) {

    companion object {
        private const val EVENT_NAME_CLICK_PG = "clickPG"

        private const val EVENT_CATEGORY_SOM = "som"

        private const val EVENT_ACTION_CLICK_DESCRIPTION_LEARN_MORE = "click pelajari selengkapnya - pof"
        private const val EVENT_ACTION_CLICK_REDUCE_QUANTITY = "click kurang item - pof"
        private const val EVENT_ACTION_CLICK_INCREASE_QUANTITY = "click tambah item - pof"
        private const val EVENT_ACTION_CLICK_OPEN_SUMMARY_BOTTOM_SHEET = "click total harga baru - pof"
        private const val EVENT_ACTION_CLICK_RESET = "click reset - pof"
        private const val EVENT_ACTION_CLICK_SEND = "click konfirmasi - pof"

        private const val BUSINESS_UNIT_PHYSICAL_GOODS = "Physical Goods"

        private const val CURRENT_SITE_TOKOPEDIA_MARKETPLACE = "tokopediamarketplace"

        private const val TRACKER_ID_CLICK_DESCRIPTION_LEARN_MORE = "48670"
        private const val TRACKER_ID_CLICK_REDUCE_QUANTITY = "48672"
        private const val TRACKER_ID_CLICK_INCREASE_QUANTITY = "48671"
        private const val TRACKER_ID_CLICK_OPEN_SUMMARY_BOTTOM_SHEET = "48673"
        private const val TRACKER_ID_CLICK_RESET = "48674"
        private const val TRACKER_ID_CLICK_SEND = "48675"

        private const val CUSTOM_DIMENSION_BUSINESS_UNIT = "businessUnit"
        private const val CUSTOM_DIMENSION_CURRENT_SITE = "currentSite"
        private const val CUSTOM_DIMENSION_TRACKER_ID = "trackerId"
    }

    fun trackClickDescriptionLearnMore() {
        TrackApp
            .getInstance()
            .gtm
            .sendGeneralEvent(
                mapOf(
                    TrackAppUtils.EVENT to EVENT_NAME_CLICK_PG,
                    TrackAppUtils.EVENT_CATEGORY to EVENT_CATEGORY_SOM,
                    TrackAppUtils.EVENT_ACTION to EVENT_ACTION_CLICK_DESCRIPTION_LEARN_MORE,
                    TrackAppUtils.EVENT_LABEL to "",
                    CUSTOM_DIMENSION_BUSINESS_UNIT to BUSINESS_UNIT_PHYSICAL_GOODS,
                    CUSTOM_DIMENSION_CURRENT_SITE to CURRENT_SITE_TOKOPEDIA_MARKETPLACE,
                    CUSTOM_DIMENSION_TRACKER_ID to TRACKER_ID_CLICK_DESCRIPTION_LEARN_MORE
                )
            )
    }

    fun trackClickReduceQuantity() {
        TrackApp
            .getInstance()
            .gtm
            .sendGeneralEvent(
                mapOf(
                    TrackAppUtils.EVENT to EVENT_NAME_CLICK_PG,
                    TrackAppUtils.EVENT_CATEGORY to EVENT_CATEGORY_SOM,
                    TrackAppUtils.EVENT_ACTION to EVENT_ACTION_CLICK_REDUCE_QUANTITY,
                    TrackAppUtils.EVENT_LABEL to "",
                    CUSTOM_DIMENSION_BUSINESS_UNIT to BUSINESS_UNIT_PHYSICAL_GOODS,
                    CUSTOM_DIMENSION_CURRENT_SITE to CURRENT_SITE_TOKOPEDIA_MARKETPLACE,
                    CUSTOM_DIMENSION_TRACKER_ID to TRACKER_ID_CLICK_REDUCE_QUANTITY
                )
            )
    }

    fun trackClickIncreaseQuantity() {
        TrackApp
            .getInstance()
            .gtm
            .sendGeneralEvent(
                mapOf(
                    TrackAppUtils.EVENT to EVENT_NAME_CLICK_PG,
                    TrackAppUtils.EVENT_CATEGORY to EVENT_CATEGORY_SOM,
                    TrackAppUtils.EVENT_ACTION to EVENT_ACTION_CLICK_INCREASE_QUANTITY,
                    TrackAppUtils.EVENT_LABEL to "",
                    CUSTOM_DIMENSION_BUSINESS_UNIT to BUSINESS_UNIT_PHYSICAL_GOODS,
                    CUSTOM_DIMENSION_CURRENT_SITE to CURRENT_SITE_TOKOPEDIA_MARKETPLACE,
                    CUSTOM_DIMENSION_TRACKER_ID to TRACKER_ID_CLICK_INCREASE_QUANTITY
                )
            )
    }

    fun trackClickOpenPofSummaryBottomSheet() {
        TrackApp
            .getInstance()
            .gtm
            .sendGeneralEvent(
                mapOf(
                    TrackAppUtils.EVENT to EVENT_NAME_CLICK_PG,
                    TrackAppUtils.EVENT_CATEGORY to EVENT_CATEGORY_SOM,
                    TrackAppUtils.EVENT_ACTION to EVENT_ACTION_CLICK_OPEN_SUMMARY_BOTTOM_SHEET,
                    TrackAppUtils.EVENT_LABEL to "",
                    CUSTOM_DIMENSION_BUSINESS_UNIT to BUSINESS_UNIT_PHYSICAL_GOODS,
                    CUSTOM_DIMENSION_CURRENT_SITE to CURRENT_SITE_TOKOPEDIA_MARKETPLACE,
                    CUSTOM_DIMENSION_TRACKER_ID to TRACKER_ID_CLICK_OPEN_SUMMARY_BOTTOM_SHEET
                )
            )
    }

    fun trackClickReset() {
        TrackApp
            .getInstance()
            .gtm
            .sendGeneralEvent(
                mapOf(
                    TrackAppUtils.EVENT to EVENT_NAME_CLICK_PG,
                    TrackAppUtils.EVENT_CATEGORY to EVENT_CATEGORY_SOM,
                    TrackAppUtils.EVENT_ACTION to EVENT_ACTION_CLICK_RESET,
                    TrackAppUtils.EVENT_LABEL to "",
                    CUSTOM_DIMENSION_BUSINESS_UNIT to BUSINESS_UNIT_PHYSICAL_GOODS,
                    CUSTOM_DIMENSION_CURRENT_SITE to CURRENT_SITE_TOKOPEDIA_MARKETPLACE,
                    CUSTOM_DIMENSION_TRACKER_ID to TRACKER_ID_CLICK_RESET
                )
            )
    }

    fun trackClickSend() {
        TrackApp
            .getInstance()
            .gtm
            .sendGeneralEvent(
                mapOf(
                    TrackAppUtils.EVENT to EVENT_NAME_CLICK_PG,
                    TrackAppUtils.EVENT_CATEGORY to EVENT_CATEGORY_SOM,
                    TrackAppUtils.EVENT_ACTION to EVENT_ACTION_CLICK_SEND,
                    TrackAppUtils.EVENT_LABEL to "",
                    CUSTOM_DIMENSION_BUSINESS_UNIT to BUSINESS_UNIT_PHYSICAL_GOODS,
                    CUSTOM_DIMENSION_CURRENT_SITE to CURRENT_SITE_TOKOPEDIA_MARKETPLACE,
                    CUSTOM_DIMENSION_TRACKER_ID to TRACKER_ID_CLICK_SEND
                )
            )
    }
}
