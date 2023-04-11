package com.tokopedia.content.common.analytic.entrypoint

import com.tokopedia.content.common.analytic.utils.KEY_BUSINESS_UNIT
import com.tokopedia.content.common.analytic.utils.KEY_CURRENT_SITE
import com.tokopedia.content.common.analytic.utils.KEY_ECOMMERCE
import com.tokopedia.content.common.analytic.utils.KEY_EVENT
import com.tokopedia.content.common.analytic.utils.KEY_EVENT_ACTION
import com.tokopedia.content.common.analytic.utils.KEY_EVENT_CATEGORY
import com.tokopedia.content.common.analytic.utils.KEY_EVENT_LABEL
import com.tokopedia.content.common.analytic.utils.KEY_PROMOTIONS
import com.tokopedia.content.common.analytic.utils.KEY_PROMO_VIEW
import com.tokopedia.content.common.analytic.utils.KEY_SESSION_IRIS
import com.tokopedia.content.common.analytic.utils.KEY_TRACKER_ID
import com.tokopedia.content.common.analytic.utils.KEY_TRACK_BUSINESS_UNIT
import com.tokopedia.content.common.analytic.utils.KEY_TRACK_CATEGORY_GROUP_CHAT_ROOM
import com.tokopedia.content.common.analytic.utils.KEY_TRACK_CATEGORY_PLAY
import com.tokopedia.content.common.analytic.utils.KEY_TRACK_CATEGORY_SHOP_PAGE_SELLER
import com.tokopedia.content.common.analytic.utils.KEY_TRACK_CLICK_EVENT_SELLER
import com.tokopedia.content.common.analytic.utils.KEY_TRACK_VIEW_EVENT_SELLER
import com.tokopedia.content.common.analytic.utils.KEY_TYPE_CONTENT_SELLER
import com.tokopedia.content.common.analytic.utils.KEY_USER_ID
import com.tokopedia.content.common.analytic.utils.convertToPromotion
import com.tokopedia.content.common.analytic.utils.currentSite
import com.tokopedia.content.common.analytic.utils.getAccountType
import com.tokopedia.content.common.analytic.utils.getTrackerId
import com.tokopedia.content.common.analytic.utils.sessionIris
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.trackingoptimizer.model.EventModel
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
    private val trackingQueue: TrackingQueue,
) : PlayPerformanceDashboardEntryPointAnalytic {
    
    private val userSessionId = userSession.userId 

    /**
     * row 1
     **/
    override fun onViewBottomSheetContentCard(authorId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_VIEW_EVENT_SELLER,
                KEY_EVENT_ACTION to "view - bottom sheet content card",
                KEY_EVENT_CATEGORY to KEY_TRACK_CATEGORY_SHOP_PAGE_SELLER,
                KEY_EVENT_LABEL to "$authorId - ${getAccountType(KEY_TYPE_CONTENT_SELLER)}",
                KEY_CURRENT_SITE to currentSite,
                KEY_USER_ID to userSessionId,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_SESSION_IRIS to sessionIris,
                KEY_TRACKER_ID to getTrackerId("41752", "41736")
            )
        )
    }

    /**
     * row 2
     **/
    override fun onClickReportPageEntryPointShopPage(authorId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_EVENT_SELLER,
                KEY_EVENT_ACTION to "click - report page entry point shop page",
                KEY_EVENT_CATEGORY to KEY_TRACK_CATEGORY_SHOP_PAGE_SELLER,
                KEY_EVENT_LABEL to "$authorId - ${getAccountType(KEY_TYPE_CONTENT_SELLER)}",
                KEY_CURRENT_SITE to currentSite,
                KEY_USER_ID to userSessionId,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_SESSION_IRIS to sessionIris,
                KEY_TRACKER_ID to getTrackerId("41753", "41737")
            )
        )
    }

    /**
     * row 3
     **/
    override fun onClickPerformanceDashboardEntryPointNative(authorId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_EVENT_SELLER,
                KEY_EVENT_ACTION to "click - performance dashboard entry point native",
                KEY_EVENT_CATEGORY to KEY_TRACK_CATEGORY_SHOP_PAGE_SELLER,
                KEY_EVENT_LABEL to "$authorId - ${getAccountType(KEY_TYPE_CONTENT_SELLER)}",
                KEY_CURRENT_SITE to currentSite,
                KEY_USER_ID to userSessionId,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_SESSION_IRIS to sessionIris,
                KEY_TRACKER_ID to getTrackerId("", "41738")
            )
        )
    }

    /**
     * row 4
     **/
    override fun onViewBottomSheetContentCardShopPageChannel(authorId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_VIEW_EVENT_SELLER,
                KEY_EVENT_ACTION to "view - bottom sheet content card shop page channel",
                KEY_EVENT_CATEGORY to KEY_TRACK_CATEGORY_SHOP_PAGE_SELLER,
                KEY_EVENT_LABEL to "$authorId - ${getAccountType(KEY_TYPE_CONTENT_SELLER)}",
                KEY_CURRENT_SITE to currentSite,
                KEY_USER_ID to userSessionId,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_SESSION_IRIS to sessionIris,
                KEY_TRACKER_ID to getTrackerId("41755", "41739")
            )
        )
    }

    /**
     * row 5
     **/
    override fun onClickReportPageEntryPoint(authorId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_EVENT_SELLER,
                KEY_EVENT_ACTION to "click - report page entry point",
                KEY_EVENT_CATEGORY to KEY_TRACK_CATEGORY_SHOP_PAGE_SELLER,
                KEY_EVENT_LABEL to "$authorId - ${getAccountType(KEY_TYPE_CONTENT_SELLER)}",
                KEY_CURRENT_SITE to currentSite,
                KEY_USER_ID to userSessionId,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_SESSION_IRIS to sessionIris,
                KEY_TRACKER_ID to getTrackerId("41756", "41740")
            )
        )
    }

    /**
     * row 6
     **/
    override fun onClickPerformanceDashboardEntryPointShortsPageChanel(authorId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_EVENT_SELLER,
                KEY_EVENT_ACTION to "click - performance dashboard entry point shop page channel",
                KEY_EVENT_CATEGORY to KEY_TRACK_CATEGORY_SHOP_PAGE_SELLER,
                KEY_EVENT_LABEL to "$authorId - ${getAccountType(KEY_TYPE_CONTENT_SELLER)}",
                KEY_CURRENT_SITE to currentSite,
                KEY_USER_ID to userSessionId,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_SESSION_IRIS to sessionIris,
                KEY_TRACKER_ID to getTrackerId("41757", "41741")
            )
        )
    }

    /**
     * row 7
     **/
    override fun onClickPerformanceDashboardEntryPointChannelPage(authorId: String, channelId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_EVENT_SELLER,
                KEY_EVENT_ACTION to "click - performance dashboard entry point channel page",
                KEY_EVENT_CATEGORY to KEY_TRACK_CATEGORY_GROUP_CHAT_ROOM,
                KEY_EVENT_LABEL to "$authorId - ${getAccountType(KEY_TYPE_CONTENT_SELLER)} - $channelId",
                KEY_CURRENT_SITE to currentSite,
                KEY_USER_ID to userSessionId,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_SESSION_IRIS to sessionIris,
                KEY_TRACKER_ID to getTrackerId("41758", "41742")
            )
        )
    }

    /**
     * row 8
     **/
    override fun onClickReportPageEntryPointGroupChatRoom(authorId: String, channelId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_EVENT_SELLER,
                KEY_EVENT_ACTION to "click - report page entry point",
                KEY_EVENT_CATEGORY to KEY_TRACK_CATEGORY_GROUP_CHAT_ROOM,
                KEY_EVENT_LABEL to "$authorId - ${getAccountType(KEY_TYPE_CONTENT_SELLER)} - $channelId",
                KEY_CURRENT_SITE to currentSite,
                KEY_USER_ID to userSessionId,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_SESSION_IRIS to sessionIris,
                KEY_TRACKER_ID to getTrackerId("41759", "41743")
            )
        )
    }

    /**
     * row 9
     **/
    override fun onClickPerformanceDashboardEntryPointReportPage(authorId: String,) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_EVENT_SELLER,
                KEY_EVENT_ACTION to "click - performance dashboard entry point report page",
                KEY_EVENT_CATEGORY to KEY_TRACK_CATEGORY_PLAY,
                KEY_EVENT_LABEL to "$authorId - ${getAccountType(KEY_TYPE_CONTENT_SELLER)}",
                KEY_CURRENT_SITE to currentSite,
                KEY_USER_ID to userSessionId,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_SESSION_IRIS to sessionIris,
                KEY_TRACKER_ID to getTrackerId("41760", "41744")
            )
        )
    }

    /**
     * row 10
     **/
    override fun onClickPerformanceDashboardEntryPointShopPage(authorId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_EVENT_SELLER,
                KEY_EVENT_ACTION to "click - performance dashboard entry point shop page",
                KEY_EVENT_CATEGORY to KEY_TRACK_CATEGORY_SHOP_PAGE_SELLER,
                KEY_EVENT_LABEL to "$authorId - ${getAccountType(KEY_TYPE_CONTENT_SELLER)}",
                KEY_CURRENT_SITE to currentSite,
                KEY_USER_ID to userSessionId,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_SESSION_IRIS to sessionIris,
                KEY_TRACKER_ID to getTrackerId("41754", "41745")
            )
        )
    }

    /**
     * row 11
     **/
    override fun onClickPerformanceDashboardEntryPointPrepPage(authorId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_EVENT_SELLER,
                KEY_EVENT_ACTION to "click - performance dashboard entry point prep page",
                KEY_EVENT_CATEGORY to KEY_TRACK_CATEGORY_PLAY,
                KEY_EVENT_LABEL to "$authorId - ${getAccountType(KEY_TYPE_CONTENT_SELLER)}",
                KEY_CURRENT_SITE to currentSite,
                KEY_USER_ID to userSessionId,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_SESSION_IRIS to sessionIris,
                KEY_TRACKER_ID to getTrackerId("41762", "41746")
            )
        )
    }

    /**
     * row 12
     **/
    override fun onViewCoachMarkPerformanceDashboardPrepPage(authorId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_VIEW_EVENT_SELLER,
                KEY_EVENT_ACTION to "view - coachmark performance dashboard prep page",
                KEY_EVENT_CATEGORY to KEY_TRACK_CATEGORY_PLAY,
                KEY_EVENT_LABEL to "$authorId - ${getAccountType(KEY_TYPE_CONTENT_SELLER)}",
                KEY_CURRENT_SITE to currentSite,
                KEY_USER_ID to userSessionId,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_SESSION_IRIS to sessionIris,
                KEY_TRACKER_ID to getTrackerId("41763", "41747")
            )
        )
    }

    /**
     * row 13
     **/
    override fun onClickCloseCoachMarkPerformanceDashboardPrepPage(authorId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_EVENT_SELLER,
                KEY_EVENT_ACTION to "click - close coachmark performance dashboard prep page",
                KEY_EVENT_CATEGORY to KEY_TRACK_CATEGORY_PLAY,
                KEY_EVENT_LABEL to "$authorId - ${getAccountType(KEY_TYPE_CONTENT_SELLER)}",
                KEY_CURRENT_SITE to currentSite,
                KEY_USER_ID to userSessionId,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_SESSION_IRIS to sessionIris,
                KEY_TRACKER_ID to getTrackerId("41764", "41748")
            )
        )
    }

    /**
     * row 14
     **/
    override fun onViewPerformanceDashboardEntryPointPrepPage(
        authorId: String,
        creativeSlot: Int,
    ) {
        trackingQueue.putEETracking(
            EventModel(
                event = KEY_PROMO_VIEW,
                action = "view - performance dashboard entry point prep page",
                category = KEY_TRACK_CATEGORY_PLAY,
                label = "$authorId - ${getAccountType(KEY_TYPE_CONTENT_SELLER)}",
            ),
            hashMapOf(
                KEY_ECOMMERCE to hashMapOf(
                    KEY_PROMO_VIEW to hashMapOf(
                        KEY_PROMOTIONS to listOf(
                            convertToPromotion(
                                creativeName = "performance dashboard entry point",
                                creativeSlot = creativeSlot,
                                itemId = "0",
                                itemName = "performance dashboard entry point prep page",
                            )
                        )
                    )
                )
            ),
            hashMapOf(
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_CURRENT_SITE to currentSite,
                KEY_SESSION_IRIS to sessionIris,
                KEY_USER_ID to userSessionId,
                KEY_TRACKER_ID to getTrackerId("41765", "41749")
            )
        )
        trackingQueue.sendAll()
    }

}
