package com.tokopedia.dilayanitokopedia.data.analytics

import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

/**
 * Created by irpan on 22/05/23.
 */
object DtHomepageAnalytics : BaseTrackerConst() {

    private const val EVENT_IMPRESS_PRODUCT_CARD_DT = "view_item_list"
    private const val ACTION_IMPRESSION_PRODUCT_CARD_DT = "impression - product cards dt"

    private const val EVENT_CLICK_PRODUCT_CARD_DT = "select_content"
    private const val ACTION_CLICK_PRODUCT_CARD_DT = "click - product cards dt"

    private const val BUSINESS_UNIT_LOGISTIC_FULFILLMENT = "Logistic | Fulfillment"
    private const val CATEGORY_LOGISTIC_FULFILLMENT = "homepage dilayani tokopedia"

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/3827
    // Tracker ID: 42969
    fun sendImpressionProductCardsDtEvent() {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(EVENT_IMPRESS_PRODUCT_CARD_DT)
                .appendEventCategory(CATEGORY_LOGISTIC_FULFILLMENT)
                .appendEventAction(ACTION_IMPRESSION_PRODUCT_CARD_DT)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC_FULFILLMENT)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .build()
        )
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/3827
    // Tracker ID: 43344
    fun sendClickProductCardsDtEvent() {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(EVENT_CLICK_PRODUCT_CARD_DT)
                .appendEventCategory(CATEGORY_LOGISTIC_FULFILLMENT)
                .appendEventAction(ACTION_CLICK_PRODUCT_CARD_DT)
                .appendEventLabel("")
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC_FULFILLMENT)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .build()
        )
    }
}
