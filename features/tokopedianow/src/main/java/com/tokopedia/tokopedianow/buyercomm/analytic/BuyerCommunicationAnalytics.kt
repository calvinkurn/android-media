package com.tokopedia.tokopedianow.buyercomm.analytic

import com.tokopedia.tokopedianow.buyercomm.analytic.BuyerCommunicationAnalytics.Action.ACTION_IMPRESSION_BOTTOMSHEET
import com.tokopedia.tokopedianow.buyercomm.analytic.BuyerCommunicationAnalytics.Action.ACTION_IMPRESSION_BUYER_COMMUNICATION
import com.tokopedia.tokopedianow.buyercomm.analytic.BuyerCommunicationAnalytics.Action.ACTION_CLICK_CHEVRON_BUTTON
import com.tokopedia.tokopedianow.buyercomm.analytic.BuyerCommunicationAnalytics.Action.ACTION_CLICK_CLOSE_BOTTOMSHEET
import com.tokopedia.tokopedianow.buyercomm.analytic.BuyerCommunicationAnalytics.Action.ACTION_CLICK_TERMS_AND_CONDITION
import com.tokopedia.tokopedianow.buyercomm.analytic.BuyerCommunicationAnalytics.TrackerId.TRACKER_ID_IMPRESSION_BOTTOMSHEET
import com.tokopedia.tokopedianow.buyercomm.analytic.BuyerCommunicationAnalytics.TrackerId.TRACKER_ID_IMPRESSION_BUYER_COMMUNICATION
import com.tokopedia.tokopedianow.buyercomm.analytic.BuyerCommunicationAnalytics.TrackerId.TRACKER_ID_CLICK_CHEVRON_BUTTON
import com.tokopedia.tokopedianow.buyercomm.analytic.BuyerCommunicationAnalytics.TrackerId.TRACKER_ID_CLICK_CLOSE_BOTTOMSHEET
import com.tokopedia.tokopedianow.buyercomm.analytic.BuyerCommunicationAnalytics.TrackerId.TRACKER_ID_CLICK_TERMS_AND_CONDITION
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.CATEGORY.EVENT_CATEGORY_TOKONOW_HOMEPAGE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_GROCERIES
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_GROCERIES
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_TRACKER_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_WAREHOUSE_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_GROCERIES
import com.tokopedia.tokopedianow.common.domain.model.WarehouseData
import com.tokopedia.track.builder.Tracker

object BuyerCommunicationAnalytics {

    internal object Action {
        const val ACTION_IMPRESSION_BUYER_COMMUNICATION = "impression - buyer communication"
        const val ACTION_CLICK_CHEVRON_BUTTON = "click - chevron button"
        const val ACTION_IMPRESSION_BOTTOMSHEET = "impression - bottom sheet"
        const val ACTION_CLICK_TERMS_AND_CONDITION = "click - syarat dan ketentuan button"
        const val ACTION_CLICK_CLOSE_BOTTOMSHEET = "click - close button"
    }

    internal object TrackerId {
        const val TRACKER_ID_IMPRESSION_BUYER_COMMUNICATION = "46103"
        const val TRACKER_ID_CLICK_CHEVRON_BUTTON = "46104"
        const val TRACKER_ID_IMPRESSION_BOTTOMSHEET = "46105"
        const val TRACKER_ID_CLICK_TERMS_AND_CONDITION = "46106"
        const val TRACKER_ID_CLICK_CLOSE_BOTTOMSHEET = "46107"
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4158
    // Tracker ID: 46103
    fun sendImpressionBuyerCommunicationEvent(
        thematicName: String,
        warehouses: List<WarehouseData>
    ) {
        val warehouseIds = getWarehouseIds(warehouses)
        val eventLabel = "$thematicName - $warehouseIds"

        Tracker.Builder()
            .setEvent(EVENT_VIEW_GROCERIES)
            .setEventAction(ACTION_IMPRESSION_BUYER_COMMUNICATION)
            .setEventCategory(EVENT_CATEGORY_TOKONOW_HOMEPAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(KEY_TRACKER_ID, TRACKER_ID_IMPRESSION_BUYER_COMMUNICATION)
            .setBusinessUnit(BUSINESS_UNIT_GROCERIES)
            .setCurrentSite("")
            .setCustomProperty(KEY_WAREHOUSE_ID, warehouseIds)
            .build()
            .send()
    }


    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4158
    // Tracker ID: 46104
    fun sendClickChevronButtonEvent(warehouses: List<WarehouseData>) {
        val warehouseIds = getWarehouseIds(warehouses)

        Tracker.Builder()
            .setEvent(EVENT_CLICK_GROCERIES)
            .setEventAction(ACTION_CLICK_CHEVRON_BUTTON)
            .setEventCategory(EVENT_CATEGORY_TOKONOW_HOMEPAGE)
            .setEventLabel(warehouseIds)
            .setCustomProperty(KEY_TRACKER_ID, TRACKER_ID_CLICK_CHEVRON_BUTTON)
            .setBusinessUnit(BUSINESS_UNIT_GROCERIES)
            .setCurrentSite("")
            .setCustomProperty(KEY_WAREHOUSE_ID, warehouseIds)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4158
    // Tracker ID: 46105
    fun sendImpressionBottomSheetEvent(warehouses: List<WarehouseData>) {
        val warehouseIds = getWarehouseIds(warehouses)

        Tracker.Builder()
            .setEvent(EVENT_VIEW_GROCERIES)
            .setEventAction(ACTION_IMPRESSION_BOTTOMSHEET)
            .setEventCategory(EVENT_CATEGORY_TOKONOW_HOMEPAGE)
            .setEventLabel(warehouseIds)
            .setCustomProperty(KEY_TRACKER_ID, TRACKER_ID_IMPRESSION_BOTTOMSHEET)
            .setBusinessUnit(BUSINESS_UNIT_GROCERIES)
            .setCurrentSite("")
            .setCustomProperty(KEY_WAREHOUSE_ID, warehouseIds)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4158
    // Tracker ID: 46106
    fun sendClickTermsAndConditionButtonEvent(warehouses: List<WarehouseData>) {
        val warehouseIds = getWarehouseIds(warehouses)

        Tracker.Builder()
            .setEvent(EVENT_CLICK_GROCERIES)
            .setEventAction(ACTION_CLICK_TERMS_AND_CONDITION)
            .setEventCategory(EVENT_CATEGORY_TOKONOW_HOMEPAGE)
            .setEventLabel(warehouseIds)
            .setCustomProperty(KEY_TRACKER_ID, TRACKER_ID_CLICK_TERMS_AND_CONDITION)
            .setBusinessUnit(BUSINESS_UNIT_GROCERIES)
            .setCurrentSite("")
            .setCustomProperty(KEY_WAREHOUSE_ID, warehouseIds)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4158
    // Tracker ID: 46107
    fun sendClickCloseButtonEvent(warehouses: List<WarehouseData>) {
        val warehouseIds = getWarehouseIds(warehouses)

        Tracker.Builder()
            .setEvent(EVENT_CLICK_GROCERIES)
            .setEventAction(ACTION_CLICK_CLOSE_BOTTOMSHEET)
            .setEventCategory(EVENT_CATEGORY_TOKONOW_HOMEPAGE)
            .setEventLabel(warehouseIds)
            .setCustomProperty(KEY_TRACKER_ID, TRACKER_ID_CLICK_CLOSE_BOTTOMSHEET)
            .setBusinessUnit(BUSINESS_UNIT_GROCERIES)
            .setCurrentSite("")
            .setCustomProperty(KEY_WAREHOUSE_ID, warehouseIds)
            .build()
            .send()
    }

    private fun getWarehouseIds(warehouses: List<WarehouseData>): String {
        return warehouses.joinToString(" - ") { it.warehouseId }
    }
}
