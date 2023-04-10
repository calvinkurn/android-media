package com.tokopedia.content.common.analytic.entrypoint

import android.os.Bundle
import com.tokopedia.content.common.analytic.utils.KEY_BUSINESS_UNIT
import com.tokopedia.content.common.analytic.utils.KEY_CREATIVE_NAME
import com.tokopedia.content.common.analytic.utils.KEY_CREATIVE_SLOT
import com.tokopedia.content.common.analytic.utils.KEY_CURRENT_SITE
import com.tokopedia.content.common.analytic.utils.KEY_EVENT
import com.tokopedia.content.common.analytic.utils.KEY_EVENT_ACTION
import com.tokopedia.content.common.analytic.utils.KEY_EVENT_CATEGORY
import com.tokopedia.content.common.analytic.utils.KEY_EVENT_LABEL
import com.tokopedia.content.common.analytic.utils.KEY_ITEM_ID
import com.tokopedia.content.common.analytic.utils.KEY_ITEM_NAME
import com.tokopedia.content.common.analytic.utils.KEY_PROMO_VIEW
import com.tokopedia.content.common.analytic.utils.KEY_SESSION_IRIS
import com.tokopedia.content.common.analytic.utils.KEY_TRACKER_ID
import com.tokopedia.content.common.analytic.utils.KEY_TRACK_BUSINESS_UNIT
import com.tokopedia.content.common.analytic.utils.KEY_TRACK_CATEGORY_GROUP_CHAT_ROOM
import com.tokopedia.content.common.analytic.utils.KEY_TRACK_CATEGORY_PLAY
import com.tokopedia.content.common.analytic.utils.KEY_TRACK_CATEGORY_SHOP_PAGE_SELLER
import com.tokopedia.content.common.analytic.utils.KEY_TRACK_CLICK_EVENT_SELLER
import com.tokopedia.content.common.analytic.utils.KEY_TRACK_VIEW_EVENT_SELLER
import com.tokopedia.content.common.analytic.utils.KEY_TRACK_VIEW_ITEM
import com.tokopedia.content.common.analytic.utils.KEY_USER_ID
import com.tokopedia.content.common.analytic.utils.currentSite
import com.tokopedia.content.common.analytic.utils.getTrackerId
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * MyNakama Tracker:
 * MA: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3781
 * SA: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3780
 *
 * Cassava:
 * play_bro_performance_dashboard_entry_point.json
 **/
class PlayPerformanceDashboardEntryPointAnalyticImpl @Inject constructor(
    private val userSession: UserSessionInterface,
) : PlayPerformanceDashboardEntryPointAnalytic {

    /**
     * row 1
     **/
    override fun onViewBottomSheetContentCard(authorId: String, authorType: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_VIEW_EVENT_SELLER,
                KEY_EVENT_ACTION to "view - bottom sheet content card",
                KEY_EVENT_CATEGORY to KEY_TRACK_CATEGORY_SHOP_PAGE_SELLER,
                KEY_EVENT_LABEL to "$authorId - $authorType",
                KEY_CURRENT_SITE to currentSite,
                KEY_USER_ID to userSession.userId,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_TRACKER_ID to getTrackerId("41752", "41736")
            )
        )
    }

    /**
     * row 2
     **/
    override fun onClickReportPageEntryPointShopPage(authorId: String, authorType: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_EVENT_SELLER,
                KEY_EVENT_ACTION to "click - report page entry point shop page",
                KEY_EVENT_CATEGORY to KEY_TRACK_CATEGORY_SHOP_PAGE_SELLER,
                KEY_EVENT_LABEL to "$authorId - $authorType",
                KEY_CURRENT_SITE to currentSite,
                KEY_USER_ID to userSession.userId,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_TRACKER_ID to getTrackerId("41753", "41737")
            )
        )
    }

    /**
     * row 3
     **/
    override fun onClickPerformanceDashboardEntryPointNative(authorId: String, authorType: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_EVENT_SELLER,
                KEY_EVENT_ACTION to "click - performance dashboard entry point native",
                KEY_EVENT_CATEGORY to KEY_TRACK_CATEGORY_SHOP_PAGE_SELLER,
                KEY_EVENT_LABEL to "$authorId - $authorType",
                KEY_CURRENT_SITE to currentSite,
                KEY_USER_ID to userSession.userId,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_TRACKER_ID to getTrackerId("", "41738")
            )
        )
    }

    /**
     * row 4
     **/
    override fun onViewBottomSheetContentCardShopPageChannel(authorId: String, authorType: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_VIEW_EVENT_SELLER,
                KEY_EVENT_ACTION to "view - bottom sheet content card shop page channel",
                KEY_EVENT_CATEGORY to KEY_TRACK_CATEGORY_SHOP_PAGE_SELLER,
                KEY_EVENT_LABEL to "$authorId - $authorType",
                KEY_CURRENT_SITE to currentSite,
                KEY_USER_ID to userSession.userId,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_TRACKER_ID to getTrackerId("41755", "41739")
            )
        )
    }

    /**
     * row 5
     **/
    override fun onClickReportPageEntryPoint(authorId: String, authorType: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_EVENT_SELLER,
                KEY_EVENT_ACTION to "click - report page entry point",
                KEY_EVENT_CATEGORY to KEY_TRACK_CATEGORY_SHOP_PAGE_SELLER,
                KEY_EVENT_LABEL to "$authorId - $authorType",
                KEY_CURRENT_SITE to currentSite,
                KEY_USER_ID to userSession.userId,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_TRACKER_ID to getTrackerId("41756", "41740")
            )
        )
    }

    /**
     * row 6
     **/
    override fun onClickPerformanceDashboardEntryPointShortsPageChanel(
        authorId: String,
        authorType: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_EVENT_SELLER,
                KEY_EVENT_ACTION to "click - performance dashboard entry point shop page channel",
                KEY_EVENT_CATEGORY to KEY_TRACK_CATEGORY_SHOP_PAGE_SELLER,
                KEY_EVENT_LABEL to "$authorId - $authorType",
                KEY_CURRENT_SITE to currentSite,
                KEY_USER_ID to userSession.userId,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_TRACKER_ID to getTrackerId("41757", "41741")
            )
        )
    }

    /**
     * row 7
     **/
    override fun onClickPerformanceDashboardEntryPointChannelPage(
        authorId: String,
        authorType: String,
        channelId: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_EVENT_SELLER,
                KEY_EVENT_ACTION to "click - performance dashboard entry point channel page",
                KEY_EVENT_CATEGORY to KEY_TRACK_CATEGORY_GROUP_CHAT_ROOM,
                KEY_EVENT_LABEL to "$authorId - $authorType - $channelId",
                KEY_CURRENT_SITE to currentSite,
                KEY_USER_ID to userSession.userId,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_TRACKER_ID to getTrackerId("41758", "41742")
            )
        )
    }

    /**
     * row 8
     **/
    override fun onClickReportPageEntryPointGroupChatRoom(
        authorId: String,
        authorType: String,
        channelId: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_EVENT_SELLER,
                KEY_EVENT_ACTION to "click - report page entry point",
                KEY_EVENT_CATEGORY to KEY_TRACK_CATEGORY_GROUP_CHAT_ROOM,
                KEY_EVENT_LABEL to "$authorId - $authorType - $channelId",
                KEY_CURRENT_SITE to currentSite,
                KEY_USER_ID to userSession.userId,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_TRACKER_ID to getTrackerId("41759", "41743")
            )
        )
    }

    /**
     * row 9
     **/
    override fun onClickPerformanceDashboardEntryPointReportPage(
        authorId: String,
        authorType: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_EVENT_SELLER,
                KEY_EVENT_ACTION to "click - performance dashboard entry point report page",
                KEY_EVENT_CATEGORY to KEY_TRACK_CATEGORY_PLAY,
                KEY_EVENT_LABEL to "$authorId - $authorType",
                KEY_CURRENT_SITE to currentSite,
                KEY_USER_ID to userSession.userId,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_TRACKER_ID to getTrackerId("41760", "41744")
            )
        )
    }

    /**
     * row 10
     **/
    override fun onClickPerformanceDashboardEntryPointShopPage(
        authorId: String,
        authorType: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_EVENT_SELLER,
                KEY_EVENT_ACTION to "click - performance dashboard entry point shop page",
                KEY_EVENT_CATEGORY to KEY_TRACK_CATEGORY_SHOP_PAGE_SELLER,
                KEY_EVENT_LABEL to "$authorId - $authorType",
                KEY_CURRENT_SITE to currentSite,
                KEY_USER_ID to userSession.userId,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_TRACKER_ID to getTrackerId("41754", "41745")
            )
        )
    }

    /**
     * row 11
     **/
    override fun onClickPerformanceDashboardEntryPointPrepPage(
        authorId: String,
        authorType: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_EVENT_SELLER,
                KEY_EVENT_ACTION to "click - performance dashboard entry point prep page",
                KEY_EVENT_CATEGORY to KEY_TRACK_CATEGORY_PLAY,
                KEY_EVENT_LABEL to "$authorId - $authorType",
                KEY_CURRENT_SITE to currentSite,
                KEY_USER_ID to userSession.userId,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_TRACKER_ID to getTrackerId("41762", "41746")
            )
        )
    }

    /**
     * row 12
     **/
    override fun onViewCoachMarkPerformanceDashboardPrepPage(authorId: String, authorType: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_VIEW_EVENT_SELLER,
                KEY_EVENT_ACTION to "view - coachmark performance dashboard prep page",
                KEY_EVENT_CATEGORY to KEY_TRACK_CATEGORY_PLAY,
                KEY_EVENT_LABEL to "$authorId - $authorType",
                KEY_CURRENT_SITE to currentSite,
                KEY_USER_ID to userSession.userId,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_TRACKER_ID to getTrackerId("41763", "41747")
            )
        )
    }

    /**
     * row 13
     **/
    override fun onClickCloseCoachMarkPerformanceDashboardPrepPage(
        authorId: String,
        authorType: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_EVENT_SELLER,
                KEY_EVENT_ACTION to "click - close coachmark performance dashboard prep page",
                KEY_EVENT_CATEGORY to KEY_TRACK_CATEGORY_PLAY,
                KEY_EVENT_LABEL to "$authorId - $authorType",
                KEY_CURRENT_SITE to currentSite,
                KEY_USER_ID to userSession.userId,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_TRACKER_ID to getTrackerId("41764", "41748")
            )
        )
    }

    /**
     * row 14
     **/
    override fun onViewPerformanceDashboardEntryPointPrepPage(
        authorId: String,
        authorType: String
    ) {
        val bundle = Bundle()
        bundle.putString(KEY_EVENT, KEY_TRACK_VIEW_ITEM)
        bundle.putString(KEY_EVENT_ACTION, "view - performance dashboard entry point prep page")
        bundle.putString(KEY_EVENT_CATEGORY, KEY_TRACK_CATEGORY_PLAY)
        bundle.putString(KEY_EVENT_LABEL, "$authorId - $authorType")
        bundle.putString(KEY_CURRENT_SITE, currentSite)
        bundle.putString(KEY_USER_ID, userSession.userId)
        bundle.putString(KEY_BUSINESS_UNIT, KEY_TRACK_BUSINESS_UNIT)
        bundle.putString(KEY_SESSION_IRIS, TrackApp.getInstance().gtm.irisSessionId)
        bundle.putString(KEY_TRACKER_ID, getTrackerId("41765", "41749"))
        bundle.putString(KEY_CREATIVE_NAME, "creative name")
        bundle.putString(KEY_CREATIVE_SLOT, "creative slot")
        bundle.putString(KEY_ITEM_ID, "item id")
        bundle.putString(KEY_ITEM_NAME, "item name")
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            KEY_PROMO_VIEW, bundle
        )
    }

}
