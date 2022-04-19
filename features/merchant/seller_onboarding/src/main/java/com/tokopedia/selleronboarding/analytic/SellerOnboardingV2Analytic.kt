package com.tokopedia.selleronboarding.analytic

import com.tokopedia.selleronboarding.analytic.SellerOnboardingAnalyticConstants.CLICK_LOGIN_PAGE_5
import com.tokopedia.selleronboarding.analytic.SellerOnboardingAnalyticConstants.CLICK_NEXT_PAGE
import com.tokopedia.selleronboarding.analytic.SellerOnboardingAnalyticConstants.CLICK_ONBOARDING_SELLER
import com.tokopedia.selleronboarding.analytic.SellerOnboardingAnalyticConstants.CLICK_SKIP_PAGE
import com.tokopedia.selleronboarding.analytic.SellerOnboardingAnalyticConstants.IMPRESSION_PAGE
import com.tokopedia.selleronboarding.analytic.SellerOnboardingAnalyticConstants.KEY_BUSINESS_UNIT
import com.tokopedia.selleronboarding.analytic.SellerOnboardingAnalyticConstants.KEY_CURRENT_SITE
import com.tokopedia.selleronboarding.analytic.SellerOnboardingAnalyticConstants.KEY_EVENT
import com.tokopedia.selleronboarding.analytic.SellerOnboardingAnalyticConstants.KEY_EVENT_ACTION
import com.tokopedia.selleronboarding.analytic.SellerOnboardingAnalyticConstants.KEY_EVENT_CATEGORY
import com.tokopedia.selleronboarding.analytic.SellerOnboardingAnalyticConstants.KEY_EVENT_LABEL
import com.tokopedia.selleronboarding.analytic.SellerOnboardingAnalyticConstants.ONBOARDING_SELLER_PAGE_V2
import com.tokopedia.selleronboarding.analytic.SellerOnboardingAnalyticConstants.PHYSICAL_GOODS
import com.tokopedia.selleronboarding.analytic.SellerOnboardingAnalyticConstants.TOKOPEDIA_SELLER
import com.tokopedia.selleronboarding.analytic.SellerOnboardingAnalyticConstants.VIEW_ONBOARDING_IRIS
import com.tokopedia.track.TrackApp

object SellerOnboardingV2Analytic {

    fun sendEventClickNextPage(position: Int) {
        val event = mutableMapOf<String, Any>(
            KEY_EVENT to CLICK_ONBOARDING_SELLER,
            KEY_EVENT_CATEGORY to ONBOARDING_SELLER_PAGE_V2,
            KEY_EVENT_ACTION to "$CLICK_NEXT_PAGE $position",
            KEY_EVENT_LABEL to "",
            KEY_BUSINESS_UNIT to PHYSICAL_GOODS,
            KEY_CURRENT_SITE to TOKOPEDIA_SELLER
        )
        sendGeneralEvent(event)
    }

    fun sendEventClickSkipPage(position: Int) {
        val event = mutableMapOf<String, Any>(
            KEY_EVENT to CLICK_ONBOARDING_SELLER,
            KEY_EVENT_CATEGORY to ONBOARDING_SELLER_PAGE_V2,
            KEY_EVENT_ACTION to "$CLICK_SKIP_PAGE $position",
            KEY_EVENT_LABEL to "",
            KEY_BUSINESS_UNIT to PHYSICAL_GOODS,
            KEY_CURRENT_SITE to TOKOPEDIA_SELLER
        )
        sendGeneralEvent(event)
    }

    fun sendEventClickGetIn() {
        val event = mutableMapOf<String, Any>(
            KEY_EVENT to CLICK_ONBOARDING_SELLER,
            KEY_EVENT_CATEGORY to ONBOARDING_SELLER_PAGE_V2,
            KEY_EVENT_ACTION to CLICK_LOGIN_PAGE_5,
            KEY_EVENT_LABEL to "",
            KEY_BUSINESS_UNIT to PHYSICAL_GOODS,
            KEY_CURRENT_SITE to TOKOPEDIA_SELLER
        )
        sendGeneralEvent(event)
    }

    fun sendEventImpressionOnboarding(page: Int) {
        val event = mutableMapOf<String, Any>(
            KEY_EVENT to VIEW_ONBOARDING_IRIS,
            KEY_EVENT_CATEGORY to ONBOARDING_SELLER_PAGE_V2,
            KEY_EVENT_ACTION to "$IMPRESSION_PAGE $page",
            KEY_EVENT_LABEL to "",
            KEY_BUSINESS_UNIT to PHYSICAL_GOODS,
            KEY_CURRENT_SITE to TOKOPEDIA_SELLER
        )
        sendGeneralEvent(event)
    }

    private fun sendGeneralEvent(eventMap: MutableMap<String, Any>) {
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }
}