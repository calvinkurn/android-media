package com.tokopedia.tokopedianow.educationalinfo.analytics

import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_CONTENT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_CONTENT_IRIS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_PLAY
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.educationalinfo.analytics.EducationalInfoAnalytics.ACTION.EVENT_ACTION_CLICK_NOW_TERMS_AND_CONDITION_BOTTOMSHEET
import com.tokopedia.tokopedianow.educationalinfo.analytics.EducationalInfoAnalytics.ACTION.EVENT_ACTION_CLICK_VISIT_NOW_USP_BOTTOMSHEET
import com.tokopedia.tokopedianow.educationalinfo.analytics.EducationalInfoAnalytics.ACTION.EVENT_ACTION_VIEW_NOW_USP_BOTTOMSHEET
import com.tokopedia.tokopedianow.educationalinfo.analytics.EducationalInfoAnalytics.CATEGORY.EVENT_CATEGORY_GROUPCHAT_ROOM
import com.tokopedia.track.builder.Tracker

class EducationalInfoAnalytics {

    /**
     * NOW! USP BottomSheet Tracker
     * https://mynakama.tokopedia.com/datatracker/requestdetail/view/3126 (Play - row: 19-21)
     */

    object ACTION {
        const val EVENT_ACTION_VIEW_NOW_USP_BOTTOMSHEET = "view - now usp bottomsheet"
        const val EVENT_ACTION_CLICK_NOW_TERMS_AND_CONDITION_BOTTOMSHEET = "click - now syarat dan ketentuan bottomsheet"
        const val EVENT_ACTION_CLICK_VISIT_NOW_USP_BOTTOMSHEET = "click - kunjungi now usp bottomsheet"
    }

    object CATEGORY {
        const val EVENT_CATEGORY_GROUPCHAT_ROOM = "groupchat room"
    }

    fun impressUspBottomSheet(channelId: String?, state: String?) {
        Tracker.Builder()
            .setBusinessUnit(BUSINESS_UNIT_PLAY)
            .setEventCategory(EVENT_CATEGORY_GROUPCHAT_ROOM)
            .setCurrentSite(CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            .setEvent(EVENT_VIEW_CONTENT_IRIS)
            .setEventAction(EVENT_ACTION_VIEW_NOW_USP_BOTTOMSHEET)
            .setEventLabel("$channelId - $state")
            .build()
            .send()
    }

    fun clickTermsAndConditions(channelId: String?, state: String?) {
        Tracker.Builder()
            .setBusinessUnit(BUSINESS_UNIT_PLAY)
            .setEventCategory(EVENT_CATEGORY_GROUPCHAT_ROOM)
            .setCurrentSite(CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            .setEvent(EVENT_CLICK_CONTENT)
            .setEventAction(EVENT_ACTION_CLICK_NOW_TERMS_AND_CONDITION_BOTTOMSHEET)
            .setEventLabel("$channelId - $state")
            .build()
            .send()
    }

    fun clickVisitNowBottomSheet(channelId: String?, state: String?) {
        Tracker.Builder()
            .setBusinessUnit(BUSINESS_UNIT_PLAY)
            .setEventCategory(EVENT_CATEGORY_GROUPCHAT_ROOM)
            .setCurrentSite(CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            .setEvent(EVENT_CLICK_CONTENT)
            .setEventAction(EVENT_ACTION_CLICK_VISIT_NOW_USP_BOTTOMSHEET)
            .setEventLabel("$channelId - $state")
            .build()
            .send()
    }

}