package com.tokopedia.selleronboarding.old.analytic

import com.tokopedia.selleronboarding.analytic.SellerOnboardingAnalyticConstants.CLICK_LOGIN
import com.tokopedia.selleronboarding.analytic.SellerOnboardingAnalyticConstants.EVENT_CLICK_LOGIN
import com.tokopedia.selleronboarding.analytic.SellerOnboardingAnalyticConstants.KEY_BUSINESS_UNIT
import com.tokopedia.selleronboarding.analytic.SellerOnboardingAnalyticConstants.KEY_CLIENT_ID
import com.tokopedia.selleronboarding.analytic.SellerOnboardingAnalyticConstants.KEY_CURRENT_SITE
import com.tokopedia.selleronboarding.analytic.SellerOnboardingAnalyticConstants.KEY_EVENT
import com.tokopedia.selleronboarding.analytic.SellerOnboardingAnalyticConstants.KEY_EVENT_ACTION
import com.tokopedia.selleronboarding.analytic.SellerOnboardingAnalyticConstants.KEY_EVENT_CATEGORY
import com.tokopedia.selleronboarding.analytic.SellerOnboardingAnalyticConstants.KEY_EVENT_LABEL
import com.tokopedia.selleronboarding.analytic.SellerOnboardingAnalyticConstants.KEY_IS_LOGGED_IN_STATUS
import com.tokopedia.selleronboarding.analytic.SellerOnboardingAnalyticConstants.KEY_SCREEN_NAME
import com.tokopedia.selleronboarding.analytic.SellerOnboardingAnalyticConstants.KEY_SESSION_IRIS
import com.tokopedia.selleronboarding.analytic.SellerOnboardingAnalyticConstants.KEY_USER_ID
import com.tokopedia.selleronboarding.analytic.SellerOnboardingAnalyticConstants.ONBOARDING_SELLER
import com.tokopedia.selleronboarding.analytic.SellerOnboardingAnalyticConstants.ONBOARDING_SELLER_PAGE
import com.tokopedia.selleronboarding.analytic.SellerOnboardingAnalyticConstants.OPEN_SCREEN
import com.tokopedia.selleronboarding.analytic.SellerOnboardingAnalyticConstants.PHYSICAL_GOODS
import com.tokopedia.selleronboarding.analytic.SellerOnboardingAnalyticConstants.SCREEN
import com.tokopedia.selleronboarding.analytic.SellerOnboardingAnalyticConstants.TOKOPEDIA_SELLER
import com.tokopedia.selleronboarding.analytic.SellerOnboardingAnalyticConstants.VALUE_FALSE
import com.tokopedia.track.TrackApp

/**
 * Created By @ilhamsuaib on 16/04/20
 */

/**
 * Seller Onboarding Tracker
 * Data Layer : https://docs.google.com/spreadsheets/d/1AZjuQ_dg25EvEEWmE8MPMo0f1_DT4IyZPaNpt4cxidA/edit#gid=868838969
 * */
object SellerOnboardingAnalytic {

    fun sendEventOpenScreen(slidePosition: Int, irisSessionId: String, clientId: String) {
        val mIrisSessionId = if (irisSessionId.isNotBlank()) "{$irisSessionId}" else ""
        val mClientId = if (clientId.isNotBlank()) "{$clientId}" else ""
        val event = mutableMapOf<String, Any>(
                KEY_EVENT to OPEN_SCREEN,
                KEY_SCREEN_NAME to "$ONBOARDING_SELLER $slidePosition",
                KEY_IS_LOGGED_IN_STATUS to VALUE_FALSE,
                KEY_CLIENT_ID to mClientId,
                KEY_SESSION_IRIS to mIrisSessionId,
                KEY_USER_ID to "",
                KEY_BUSINESS_UNIT to PHYSICAL_GOODS
        )

        sendGeneralEvent(eventMap = event)
    }

    /**
     * @param position start with 0
     * */
    fun sendEventClickOpenApp(position: Int, irisSessionId: String, clientId: String) {
        val mIrisSessionId = if (irisSessionId.isNotBlank()) "{$irisSessionId}" else ""
        val mClientId = if (clientId.isNotBlank()) "{$clientId}" else ""
        val positionStr = "${convertOrdinal(position.plus(1))} $SCREEN"
        val event = createMap(
                event = EVENT_CLICK_LOGIN,
                category = ONBOARDING_SELLER_PAGE,
                action = arrayOf(CLICK_LOGIN, positionStr).joinToString(" - "),
                label = ""
        )
        event[KEY_SCREEN_NAME] = "$ONBOARDING_SELLER ${position.plus(1)}"
        event[KEY_CURRENT_SITE] = TOKOPEDIA_SELLER
        event[KEY_CLIENT_ID] = mClientId
        event[KEY_SESSION_IRIS] = mIrisSessionId
        event[KEY_USER_ID] = ""
        event[KEY_BUSINESS_UNIT] = PHYSICAL_GOODS

        sendGeneralEvent(event)
    }

    private fun createMap(event: String, category: String, action: String, label: String): MutableMap<String, Any> {
        return mutableMapOf(
                KEY_EVENT to event,
                KEY_EVENT_CATEGORY to category,
                KEY_EVENT_ACTION to action,
                KEY_EVENT_LABEL to label
        )
    }

    private fun sendGeneralEvent(eventMap: MutableMap<String, Any>) {
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    private fun convertOrdinal(i: Int): String? {
        val sufixes = arrayOf("th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th")
        return when (i % 100) {
            11, 12, 13 -> i.toString() + "th"
            else -> i.toString() + sufixes[i % 10]
        }
    }
}