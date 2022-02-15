package com.tokopedia.feedcomponent.analytics.tracker

import com.google.gson.Gson
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class PlayAnalyticsTracker @Inject constructor(
    private val irisSession: IrisSession,
    private val userSession: UserSessionInterface
) {

    //1
    fun clickOnVideoTabOnFeedPage(position: Int) {
        createAnalyticsData(
            EventName.CLICK_HOMEPAGE,
            EventAction.CLICK_FEED_TAB,
            EventCategory.CONTENT_FEED_TIMELINE,
            when (position) {
                0 -> EventLabel.UPDATE
                1 -> EventLabel.EXPLORE
                else -> EventLabel.VIDEO
            }
        )
    }

    //2
    fun impressOnContentHighlightWidgetInVideoTab(
        channelId: String,
        shopId: String,
        channelType: String
    ) {
        createAnalyticsData(
            EventName.VIEW_HOMEPAGE_IRIS,
            EventAction.IMPRESSION_CONTENT_HIGHLIGHT_WIDGET,
            EventCategory.CONTENT_FEED_TIMELINE_VIDEO,
            EventLabel.liveVodUpcoming(channelId, shopId, channelType)
        )
    }

    //3
    fun clickOnContentHighlightCardsInVideoTab(
        channelId: String,
        shopId: String,
        promotions: List<Any>, channelType: String
    ) {
        createAnalyticsData(
            EventName.SELECT_CONTENT,
            EventAction.CLICK_CONTENT_HIGHLIGHT_CARDS,
            EventCategory.CONTENT_FEED_TIMELINE_VIDEO,
            EventLabel.liveVodUpcoming(channelId, shopId, channelType),
            promotions
        )
    }

    //4
    fun impressOnLagiLiveContentCarouselWidget() {
        createAnalyticsData(
            EventName.VIEW_HOMEPAGE_IRIS,
            EventAction.IMPRESSION_CAROUSEL_WIDGET_LAGI_LIVE,
            EventCategory.CONTENT_FEED_TIMELINE_VIDEO,
            ""
        )
    }

    //5
    fun impressOnLagiLiveCarouselContentCards(
        channelId: String,
        shopId: String,
        promotions: List<Any>, channelType: String
    ) {
        createAnalyticsData(
            EventName.VIEW_ITEM,
            EventAction.IMPRESSION_CONTENT_CARDS_LAGI_LIVE,
            EventCategory.CONTENT_FEED_TIMELINE_VIDEO,
            EventLabel.liveVodUpcoming(channelId, shopId, channelType),
            promotions
        )
    }

    //6
    fun clickOnLagiLiveCarouselContentCards(
        channelId: String,
        shopId: String,
        promotions: List<Any>, channelType: String
    ) {
        createAnalyticsData(
            EventName.SELECT_CONTENT,
            EventAction.CLICK_CONTENT_CARDS_LAGI_LIVE,
            EventCategory.CONTENT_FEED_TIMELINE_VIDEO,
            EventLabel.liveVodUpcoming(channelId, shopId, channelType),
            promotions
        )
    }

    //7
    fun clickOnSeeAllOnLagiLiveCarousel() {
        createAnalyticsData(
            EventName.CLICK_HOMEPAGE,
            EventAction.CLICK_LIHAT_SEMUA_LAGI_LIVE,
            EventCategory.CONTENT_FEED_TIMELINE_VIDEO,
            ""
        )
    }

    //8
    fun impressOnContentCardsInContentListPageForLagiLive(
        channelId: String,
        shopId: String,
        promotions: List<Any>, channelType: String
    ) {
        createAnalyticsData(
            EventName.VIEW_ITEM,
            EventAction.IMPRESSION_CONTENT_CARDS,
            EventCategory.CONTENT_FEED_TIMELINE_VIDEO_CONTENT_LIST_PAGE,
            EventLabel.liveVodUpcomingFilterCategoryEntryPointCarousel(
                channelId,
                shopId,
                channelType
            ),
            promotions
        )
    }

    //9
    fun clickOnContentCardsInContentListPageForLagiLive(
        channelId: String,
        shopId: String,
        promotions: List<Any>, channelType: String
    ) {
        createAnalyticsData(
            EventName.SELECT_CONTENT,
            EventAction.CLICK_CONTENT_CARDS,
            EventCategory.CONTENT_FEED_TIMELINE_VIDEO_CONTENT_LIST_PAGE,
            EventLabel.liveVodUpcomingFilterCategoryEntryPointCarousel(
                channelId,
                shopId,
                channelType
            ),
            promotions
        )
    }

    //10
    fun impressOnFilterChipsInContentListPageForLagiLive(
        channelId: String,
        shopId: String,
        promotions: List<Any>, channelType: String
    ) {
        createAnalyticsData(
            EventName.VIEW_HOMEPAGE_IRIS,
            EventAction.IMPRESSION_FILTER_CHIPS,
            EventCategory.CONTENT_FEED_TIMELINE_VIDEO_CONTENT_LIST_PAGE,
            EventLabel.liveVodUpcomingFilterCategoryEntryPointCarousel(
                channelId,
                shopId,
                channelType
            ),
            promotions
        )
    }

    //11
    fun clickOnFilterChipsInContentListPageForLagiLive() {
        createAnalyticsData(
            EventName.CLICK_HOMEPAGE,
            EventAction.CLICK_FILTER_CHIPS,
            EventCategory.CONTENT_FEED_TIMELINE_VIDEO_CONTENT_LIST_PAGE,
            EventLabel.FILTER_CATEGORY_ENTRY_POINT_CAROUSEL_WIDGET
        )
    }

    //12
    fun impressOnFilterChipsInVideoTab() {
        createAnalyticsData(
            EventName.VIEW_HOMEPAGE_IRIS,
            EventAction.IMPRESSION_FILTER_CHIPS,
            EventCategory.CONTENT_FEED_TIMELINE_VIDEO,
            EventLabel.FILTER_CATEGORY
        )
    }

    //13
    fun clickOnFilterChipsInVideoTab() {
        createAnalyticsData(
            EventName.CLICK_HOMEPAGE,
            EventAction.CLICK_FILTER_CHIPS,
            EventCategory.CONTENT_FEED_TIMELINE_VIDEO,
            EventLabel.FILTER_CATEGORY
        )
    }

    //14
    fun impressOnContentCardsInVideoTabBelowTheChips(
        channelId: String,
        shopId: String,
        promotions: List<Any>, channelType: String
    ) {
        createAnalyticsData(
            EventName.VIEW_ITEM,
            EventAction.IMPRESSION_CONTENT_CARDS,
            EventCategory.CONTENT_FEED_TIMELINE_VIDEO,
            EventLabel.liveVodUpcomingFilterCategory(channelId, shopId, channelType),
            promotions
        )
    }

    //15
    fun clickOnContentCardsInVideoTabBelowTheChips(
        channelId: String,
        shopId: String,
        promotions: List<Any>, channelType: String
    ) {
        createAnalyticsData(
            EventName.SELECT_CONTENT,
            EventAction.CLICK_CONTENT_CARDS,
            EventCategory.CONTENT_FEED_TIMELINE_VIDEO,
            EventLabel.liveVodUpcomingFilterCategory(channelId, shopId, channelType),
            promotions
        )
    }

    //16
    fun impressOnUpcomingContentCarouselWidget() {
        createAnalyticsData(
            EventName.VIEW_HOMEPAGE_IRIS,
            EventAction.IMPRESSION_CAROUSEL_WIDGET_UPCOMING,
            EventCategory.CONTENT_FEED_TIMELINE_VIDEO,
            EventLabel.FILTER_CATEGORY
        )
    }

    //17
    fun impressOnUpcomingCarouselContentCards(
        channelId: String,
        shopId: String,
        promotions: List<Any>, channelType: String
    ) {
        createAnalyticsData(
            EventName.VIEW_ITEM,
            EventAction.IMPRESSION_CONTENT_CARDS_UPCOMING,
            EventCategory.CONTENT_FEED_TIMELINE_VIDEO,
            EventLabel.liveVodUpcomingFilterCategory(channelId, shopId, channelType),
            promotions
        )
    }

    //18
    fun clickOnUpcomingCarouselContentCards(
        channelId: String,
        shopId: String,
        promotions: List<Any>, channelType: String
    ) {
        createAnalyticsData(
            EventName.SELECT_CONTENT,
            EventAction.CLICK_CONTENT_CARDS_UPCOMING,
            EventCategory.CONTENT_FEED_TIMELINE_VIDEO,
            EventLabel.liveVodUpcomingFilterCategory(channelId, shopId, channelType),
            promotions
        )
    }

    //19
    fun clickOnSeeAllOnUpcomingCarousel() {
        createAnalyticsData(
            EventName.CLICK_HOMEPAGE,
            EventAction.CLICK_SEE_ALL_UPCOMING,
            EventCategory.CONTENT_FEED_TIMELINE_VIDEO,
            EventLabel.FILTER_CATEGORY
        )
    }

    //20
    fun visitVideoTabPageOnFeed(screenName: String) {
        createAnalyticsData(
            EventName.OPEN_SCREEN,
            null, null, null, isLoggedInStatus = userSession.isLoggedIn,
            screenName = screenName
        )
    }

    //21
    fun visitUpdateTabPageOnFeed(screenName: String) {
        createAnalyticsData(
            EventName.OPEN_SCREEN,
            null, null, null, isLoggedInStatus = userSession.isLoggedIn, screenName = screenName
        )
    }

    //22
    fun visitExploreTabPageOnFeed(screenName: String) {
        createAnalyticsData(
            EventName.OPEN_SCREEN,
            null, null, null, isLoggedInStatus = userSession.isLoggedIn, screenName = screenName
        )
    }

    //23
    fun impressOnContentHighlightCard(
        channelId: String,
        shopId: String,
        promotions: List<Any>,
        channelType: String
    ) {
        createAnalyticsData(
            EventName.VIEW_ITEM,
            EventAction.IMPRESSION_CONTENT_HIGHLIGHT_CARDS,
            EventCategory.CONTENT_FEED_TIMELINE_VIDEO,
            EventLabel.liveVodUpcoming(channelId, shopId, channelType),
            promotions
        )
    }

    //24
    fun clickOnRemindMeButtonOnPlayCardsWithinChip(
        channelId: String,
        shopId: String,
        promotions: List<Any>, channelType: String
    ) {
        createAnalyticsData(
            EventName.CLICK_HOMEPAGE,
            EventAction.CLICK_REMIND,
            EventCategory.CONTENT_FEED_TIMELINE_VIDEO,
            EventLabel.liveVodUpcomingFilterCategory(channelId, shopId, channelType),
            promotions
        )
    }

    //25
    fun clickOnUnRemindMeButtonOnPlayCardsWithinChip(
        channelId: String,
        shopId: String,
        promotions: List<Any>,
        channelType: String
    ) {
        createAnalyticsData(
            EventName.CLICK_HOMEPAGE,
            EventAction.CLICK_UNREMIND,
            EventCategory.CONTENT_FEED_TIMELINE_VIDEO,
            EventLabel.liveVodUpcomingFilterCategory(channelId, shopId, channelType),
            promotions
        )
    }

    //26
    fun clickOnRemindMeButtonOnPlayCardInUpcomingCarousel(
        channelId: String,
        shopId: String,
        promotions: List<Any>, channelType: String
    ) {
        createAnalyticsData(
            EventName.CLICK_HOMEPAGE,
            EventAction.CLICK_REMIND_UPCOMING,
            EventCategory.CONTENT_FEED_TIMELINE_VIDEO,
            EventLabel.liveVodUpcomingFilterCategory(channelId, shopId, channelType),
            promotions
        )
    }

    //27
    fun clickOnUnRemindMeButtonOnPlayCardInUpcomingCarousel(
        channelId: String,
        shopId: String,
        promotions: List<Any>, channelType: String
    ) {
        createAnalyticsData(
            EventName.CLICK_HOMEPAGE,
            EventAction.CLICK_UNREMIND_UPCOMING,
            EventCategory.CONTENT_FEED_TIMELINE_VIDEO,
            EventLabel.liveVodUpcomingFilterCategory(channelId, shopId, channelType),
            promotions
        )
    }


    private fun createAnalyticsData(
        eventName: String,
        eventCategory: String?,
        eventAction: String?,
        eventLabel: String?,
        list: List<Any>? = null,
        isLoggedInStatus: Boolean? = null,
        screenName: String? = null
    ) {
        val map = mutableMapOf<String, Any?>(
            TrackAppUtils.EVENT to eventName,
            TrackAppUtils.EVENT_ACTION to eventAction,
            TrackAppUtils.EVENT_CATEGORY to eventCategory,
            TrackAppUtils.EVENT_LABEL to eventLabel,
            PlayTrackerConst.BUSINESS_UNIT to PlayTrackerConst.CONTENT,
            PlayTrackerConst.CURRENT_SITE to PlayTrackerConst.TOKOPEDIA_MARKET_PLACE,
            PlayTrackerConst.SESSION_IRIS to irisSession.getSessionId(),
            PlayTrackerConst.USER_ID to userSession.userId,
            PlayTrackerConst.PROMOTIONS to getPromotions(list),
            PlayTrackerConst.IS_LOGGED_IN to isLoggedInStatus,
            PlayTrackerConst.SCREEN_NAME to screenName
        )

        trackGeneralEvent(map)
    }

    private fun getPromotions(list: List<Any>? = null): String? {
        if (list.isNullOrEmpty()) return null
        return Gson().toJson(list)
    }

    private fun trackGeneralEvent(params: Map<String, Any?>) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            params
        )
    }

    private object PlayTrackerConst {
        const val SCREEN_NAME = "screenName"
        const val IS_LOGGED_IN = "isLoggedInStatus"
        const val TOKOPEDIA_MARKET_PLACE = "tokopediamarketplace"
        const val CONTENT = "content"
        const val BUSINESS_UNIT = "businessUnit"
        const val CURRENT_SITE = "currentSite"
        const val PROMOTIONS = "promotions"
        const val SESSION_IRIS = "sessionIris"
        const val USER_ID = "userId"
    }

    private object EventName {
        const val VIEW_ITEM = "view_item"
        const val SELECT_CONTENT = "select_content"
        const val CLICK_HOMEPAGE = "clickHomepage"
        const val VIEW_HOMEPAGE_IRIS = "viewHomepageIris"
        const val PROMO_CLICK = "promoClick"
        const val PROMO_VIEW = "promoView"
        const val OPEN_SCREEN = "openScreen"
    }

    private object EventCategory {
        const val CONTENT_FEED_TIMELINE = "content feed timeline"
        const val CONTENT_FEED_TIMELINE_VIDEO = "content feed timeline - video"
        const val CONTENT_FEED_TIMELINE_VIDEO_CONTENT_LIST_PAGE =
            "content feed timeline - video - content list page"
        const val SCREEN_NAME_FEED_VIDEO_TAB = "screen name: /feed - video tab"
        const val SCREEN_NAME_FEED_UPDATE_TAB = "screen name: /feed - update tab"
        const val SCREEN_NAME_FEED_EXPLORE_TAB = "screen name: /feed - explore tab"
    }

    private object EventAction {
        const val CLICK_REMIND = "click - remind"
        const val CLICK_REMIND_UPCOMING = "click - remind - upcoming"
        const val CLICK_UNREMIND = "click - unremind"
        const val CLICK_UNREMIND_UPCOMING = "click - unremind - upcoming"
        const val CLICK_FEED_TAB = "click - feed tab"
        const val CLICK_CONTENT_HIGHLIGHT_CARDS = "click - content highlight cards"
        const val CLICK_CONTENT_CARD_AGAIN_LIVE = "click - content cards - again live"
        const val CLICK_SEE_ALL_UPCOMING = "click - see all - upcoming"
        const val CLICK_LIHAT_SEMUA_LAGI_LIVE = "click - lihat semua - lagi live"
        const val CLICK_SEE_ALL_AGAIN_LIVE = "click - see all - again live"
        const val CLICK_CONTENT_CARDS = "click - content cards"
        const val CLICK_CONTENT_CARDS_LAGI_LIVE = "click - content cards - lagi live"
        const val CLICK_CONTENT_CARDS_UPCOMING = "click - content cards - upcoming"
        const val CLICK_FILTER_CHIPS = "click - filter chips"

        const val IMPRESSION_CONTENT_HIGHLIGHT_WIDGET = "impression - content highlight widget"
        const val IMPRESSION_CONTENT_HIGHLIGHT_CARDS = "impression - content highlight cards"
        const val IMPRESSION_CAROUSEL_WIDGET_LAGI_LIVE = "impression - carousel widget - lagi live"
        const val IMPRESSION_CAROUSEL_WIDGET_AGAIN_LIVE =
            "impression - carousel widget - again live"
        const val IMPRESSION_CAROUSEL_WIDGET_UPCOMING = "impression - carousel widget - upcoming"
        const val IMPRESSION_CONTENT_CARDS_LAGI_LIVE = "impression - content cards - lagi live"
        const val IMPRESSION_CONTENT_CARDS_LIVE_AGAIN = "impression - content cards - live again"
        const val IMPRESSION_CONTENT_CARDS_UPCOMING = "impressions - content cards - upcoming"
        const val IMPRESSION_CONTENT_CARDS = "impressions - content cards"
        const val IMPRESSION_FILTER_CHIPS = "impression - filter chips"


        const val SCREEN_NAME_FEED_VIDEO_TAB = "screen name: /feed - video tab"
        const val SCREEN_NAME_FEED_UPDATE_TAB = "screen name: /feed - update tab"
        const val SCREEN_NAME_FEED_EXPLORE_TAB = "screen name: /feed - explore tab"

    }

    private object EventLabel {
        const val UPDATE = "update"
        const val EXPLORE = "explore"
        const val VIDEO = "video"
        fun liveVodUpcoming(channelId: String, shopId: String, type: String) =
            "{${channelId}} - {${shopId}} - {${type}}"

        fun liveVodUpcomingFilterCategory(channelId: String, shopId: String, type: String) =
            "{${channelId}} - {${shopId}} - {${type}} - {filter category}"

        fun liveVodUpcomingFilterCategoryEntryPointCarousel(
            channelId: String,
            shopId: String,
            type: String
        ) =
            "{${channelId}} - {${shopId}} - {${type}} - {filter category} - {entry point carousel widget}"

        const val FILTER_CATEGORY_ENTRY_POINT_CAROUSEL_WIDGET =
            "{filter category} - {entry point carousel widget}"
        const val FILTER_CATEGORY = "{filter category}"

        const val SCREEN_NAME_FEED_VIDEO_TAB = "screen name: /feed - video tab"
        const val SCREEN_NAME_FEED_UPDATE_TAB = "screen name: /feed - update tab"
        const val SCREEN_NAME_FEED_EXPLORE_TAB = "screen name: /feed - explore tab"
    }
}