package com.tokopedia.power_merchant.subscribe.analytics.tracking

import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 30/05/22.
 */

class MembershipDetailTracker @Inject constructor(
    private val userSession: UserSessionInterface
) {

    fun sendImpressionMembershipPageEvent(currentGrade: String) {
        val map = createEvent(
            event = TrackingConstant.VIEW_PG_IRIS,
            category = TrackingConstant.getPowerMerchantCategory(),
            action = TrackingConstant.IMPRESSION_DETAIL_KEANGGOTAAN_PAGE,
            label = currentGrade
        )

        TrackerUtils.sendEvent(map)
    }

    fun sendClickPmGradeTabEvent(tabLabel: String) {
        val map = createEvent(
            event = TrackingConstant.EVENT_CLICK_PG,
            category = TrackingConstant.getPowerMerchantCategory(),
            action = TrackingConstant.ACTION_CLICK_DETAIL_KEANGGOTAAN_PAGE,
            label = tabLabel
        )

        TrackerUtils.sendEvent(map)
    }

    fun sendClickLearnShopPerformanceEvent(currentGrade: String) {
        val map = createEvent(
            event = TrackingConstant.EVENT_CLICK_PG,
            category = TrackingConstant.getPowerMerchantCategory(),
            action = TrackingConstant.ACTION_CLICK_SHOP_PERFORMANCE,
            label = currentGrade
        )

        TrackerUtils.sendEvent(map)
    }

    private fun createEvent(
        event: String, category: String,
        action: String, label: String
    ): MutableMap<String, Any> {
        return TrackerUtils.createEvent(userSession, event, category, action, label)
    }
}