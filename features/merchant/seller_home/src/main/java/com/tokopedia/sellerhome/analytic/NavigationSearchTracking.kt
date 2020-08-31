package com.tokopedia.sellerhome.analytic

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.sellerhome.analytic.TrackingConstant.BUSINESS_UNIT
import com.tokopedia.sellerhome.analytic.TrackingConstant.CLICK_SEARCH
import com.tokopedia.sellerhome.analytic.TrackingConstant.CLICK_SEARCH_BUTTON
import com.tokopedia.sellerhome.analytic.TrackingConstant.CURRENT_SITE
import com.tokopedia.sellerhome.analytic.TrackingConstant.EVENT
import com.tokopedia.sellerhome.analytic.TrackingConstant.EVENT_ACTION
import com.tokopedia.sellerhome.analytic.TrackingConstant.EVENT_CATEGORY
import com.tokopedia.sellerhome.analytic.TrackingConstant.EVENT_LABEL
import com.tokopedia.sellerhome.analytic.TrackingConstant.GLOBAL_SEARCH
import com.tokopedia.sellerhome.analytic.TrackingConstant.PHYSICAL_GOODS
import com.tokopedia.sellerhome.analytic.TrackingConstant.SCREEN_NAME
import com.tokopedia.sellerhome.analytic.TrackingConstant.TOKOPEDIA_SELLER
import com.tokopedia.sellerhome.analytic.TrackingConstant.USER_ID


/**
 * GLOBAL SEARCH SELLER
 * https://docs.google.com/spreadsheets/d/1AZjuQ_dg25EvEEWmE8MPMo0f1_DT4IyZPaNpt4cxidA/edit#gid=312411040
 */
object NavigationSearchTracking {

    fun sendClickSearchMenuEvent(userId: String) {
        val event = DataLayer.mapOf(
                EVENT, CLICK_SEARCH,
                EVENT_CATEGORY, GLOBAL_SEARCH,
                EVENT_ACTION, CLICK_SEARCH_BUTTON,
                EVENT_LABEL, "",
                SCREEN_NAME, "",
                CURRENT_SITE, TOKOPEDIA_SELLER,
                USER_ID, userId,
                BUSINESS_UNIT, PHYSICAL_GOODS
        )
        TrackingHelper.sendGeneralEvent(event)
    }
}