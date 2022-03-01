package com.tokopedia.videoTabComponent.analytics.tracker

import com.google.gson.Gson
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.trackingoptimizer.constant.Constant.Companion.ECOMMERCE
import com.tokopedia.user.session.UserSessionInterface
import java.util.HashMap
import javax.inject.Inject

class PlayAnalyticsTracker @Inject constructor(
    private val irisSession: IrisSession,
    private val trackingQueue: TrackingQueue,
    private val userSession: UserSessionInterface
) {

    data class AnalyticsPromo(
        val promoClick: PromoClick
    ) {
        data class PromoClick(
            val promotions: List<Promotions>
        ) {
            data class Promotions(
                val creative: String,
                val id: String, //channel id
                val name: String,
                val position: String
            )
        }
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
        channelId: String, shopId: String, promotions: List<String>,
        channelType: String, position: Int
    ) {
        createAnalyticsData(
            EventName.PROMO_CLICK,
            EventAction.CLICK_CONTENT_HIGHLIGHT_CARDS,
            EventCategory.CONTENT_FEED_TIMELINE_VIDEO,
            EventLabel.liveVodUpcoming(channelId, shopId, channelType),
            getPromotions(channelId, FEED_VIDEO_TAB_HIGHLIGHT_CARDS, position, promotions)
        )
    }

    //4
    fun impressOnLagiLiveContentCarouselWidget() {
        createAnalyticsData(
            EventName.PROMO_VIEW,
            EventAction.IMPRESSION_CAROUSEL_WIDGET_AGAIN_LIVE,
            EventCategory.CONTENT_FEED_TIMELINE_VIDEO,
            ""
        )
    }

    //5
    fun impressOnLagiLiveCarouselContentCards(
        channelId: String, shopId: String,
        promotions: List<String>, channelType: String, position: Int
    ) {
        createAnalyticsData(
            EventName.PROMO_VIEW,
            EventAction.IMPRESSION_CONTENT_CARDS_AGAIN_LIVE,
            EventCategory.CONTENT_FEED_TIMELINE_VIDEO,
            EventLabel.liveVodUpcoming(channelId, shopId, channelType),
            getPromotions(channelId, FEED_VIDEO_TAB_CONTENT_CARDS_AGAIN_LIVE, position, promotions)
        )
    }

    //6
    fun clickOnLagiLiveCarouselContentCards(
        channelId: String,
        shopId: String,
        promotions: List<String>, channelType: String, position: Int
    ) {
        createAnalyticsData(
            EventName.PROMO_CLICK,
            EventAction.CLICK_CONTENT_CARD_AGAIN_LIVE,
            EventCategory.CONTENT_FEED_TIMELINE_VIDEO,
            EventLabel.liveVodUpcoming(channelId, shopId, channelType),
            getPromotions(channelId, FEED_VIDEO_TAB_CONTENT_CARDS_AGAIN_LIVE, position, promotions)
        )
    }

    //7
    fun clickOnSeeAllOnLagiLiveCarousel(widgetType: String, filterCategory: String) {
        createAnalyticsData(
            EventName.CLICK_HOMEPAGE,
            EventAction.CLICK_SEE_ALL_AGAIN_LIVE,
            EventCategory.CONTENT_FEED_TIMELINE_VIDEO,
            if (widgetType == EventLabel.WIDGET_UPCOMING) filterCategory else ""
        )
    }

    //8
    fun impressOnContentCardsInContentListPageForLagiLive(
        channelId: String, shopId: String,
        promotions: List<String>, channelType: String, filterCategory: String, position: Int
    ) {
        createAnalyticsData(
            EventName.PROMO_VIEW,
            EventAction.IMPRESSION_CONTENT_CARDS,
            EventCategory.CONTENT_FEED_TIMELINE_VIDEO_CONTENT_LIST_PAGE,
            EventLabel.liveVodUpcomingFilterCategoryEntryPointCarousel(
                channelId,
                shopId,
                channelType,
                filterCategory
            ),
            getPromotions(
                channelId, FEED_VIDEO_TAB_CONTENT_LIST_PAGE_CONTENT_CARDS, position, promotions
            )
        )
    }

    //9
    fun clickOnContentCardsInContentListPageForLagiLive(
        channelId: String, shopId: String,
        promotions: List<String>, channelType: String, filterCategory: String, position: Int
    ) {
        createAnalyticsData(
            EventName.PROMO_CLICK,
            EventAction.CLICK_CONTENT_CARDS,
            EventCategory.CONTENT_FEED_TIMELINE_VIDEO_CONTENT_LIST_PAGE,
            EventLabel.liveVodUpcomingFilterCategoryEntryPointCarousel(
                channelId,
                shopId,
                channelType,
                filterCategory
            ),
            getPromotions(
                channelId, FEED_VIDEO_TAB_CONTENT_LIST_PAGE_CONTENT_CARDS, position, promotions
            )
        )
    }

    //10//will be used later
    fun impressOnFilterChipsInContentListPageForLagiLive(
        channelId: String, shopId: String,
        promotions: List<String>, channelType: String, filterCategory: String
    ) {
        createAnalyticsData(
            EventName.VIEW_HOMEPAGE_IRIS,
            EventAction.IMPRESSION_FILTER_CHIPS,
            EventCategory.CONTENT_FEED_TIMELINE_VIDEO_CONTENT_LIST_PAGE,
            EventLabel.liveVodUpcomingFilterCategoryEntryPointCarousel(
                channelId, shopId, channelType, filterCategory
            )
        )
    }

    //11//will be used later
    fun clickOnFilterChipsInContentListPageForLagiLive() {
        createAnalyticsData(
            EventName.CLICK_HOMEPAGE,
            EventAction.CLICK_FILTER_CHIPS,
            EventCategory.CONTENT_FEED_TIMELINE_VIDEO_CONTENT_LIST_PAGE,
            EventLabel.FILTER_CATEGORY_ENTRY_POINT_CAROUSEL_WIDGET
        )
    }

    //12
    fun impressOnFilterChipsInVideoTab(filterCategory: String) {
        createAnalyticsData(
            EventName.CLICK_HOMEPAGE,
            EventAction.IMPRESSION_FILTER_CHIPS,
            EventCategory.CONTENT_FEED_TIMELINE_VIDEO,
            EventLabel.getFilterCategory(filterCategory)
        )
    }

    //13
    fun clickOnFilterChipsInVideoTab(filterCategory: String) {
        createAnalyticsData(
            EventName.CLICK_HOMEPAGE,
            EventAction.CLICK_FILTER_CHIPS,
            EventCategory.CONTENT_FEED_TIMELINE_VIDEO,
            EventLabel.getFilterCategory(filterCategory)
        )
    }

    //14
    fun impressOnContentCardsInVideoTabBelowTheChips(
        channelId: String, shopId: String,
        promotions: List<String>, channelType: String, filterCategory: String, position: Int
    ) {
        createAnalyticsData(
            EventName.PROMO_VIEW,
            EventAction.IMPRESSION_CONTENT_CARDS,
            EventCategory.CONTENT_FEED_TIMELINE_VIDEO,
            EventLabel.liveVodUpcomingFilterCategory(
                channelId,
                shopId,
                channelType,
                filterCategory
            ),
            getPromotions(channelId, FEED_VIDEO_TAB_CONTENT_CARDS_AGAIN_LIVE, position, promotions)
        )
    }

    //15
    fun clickOnContentCardsInVideoTabBelowTheChips(
        channelId: String, shopId: String,
        promotions: List<String>, channelType: String, filterCategory: String, position: Int
    ) {
        createAnalyticsData(
            EventName.PROMO_CLICK,
            EventAction.CLICK_CONTENT_CARDS,
            EventCategory.CONTENT_FEED_TIMELINE_VIDEO,
            EventLabel.liveVodUpcomingFilterCategory(
                channelId,
                shopId,
                channelType,
                filterCategory
            ),
            getPromotions(channelId, FEED_VIDEO_TAB_CONTENT_CARDS_AGAIN_LIVE, position, promotions)
        )
    }

    //16
    fun impressOnUpcomingContentCarouselWidget(filterCategory: String) {
        createAnalyticsData(
            EventName.VIEW_HOMEPAGE_IRIS,
            EventAction.IMPRESSION_CAROUSEL_WIDGET_UPCOMING,
            EventCategory.CONTENT_FEED_TIMELINE_VIDEO,
            EventLabel.getFilterCategory(filterCategory)
        )
    }

    //17
    fun impressOnUpcomingCarouselContentCards(
        channelId: String, shopId: String,
        promotions: List<String>, channelType: String, filterCategory: String, position: Int
    ) {
        createAnalyticsData(
            EventName.PROMO_VIEW,
            EventAction.IMPRESSION_CONTENT_CARDS_UPCOMING,
            EventCategory.CONTENT_FEED_TIMELINE_VIDEO,
            EventLabel.liveVodUpcomingFilterCategory(
                channelId,
                shopId,
                channelType,
                filterCategory
            ),
            getPromotions(channelId, FEED_VIDEO_TAB_CONTENT_CARDS_UPCOMING, position, promotions)
        )
    }

    //18
    fun clickOnUpcomingCarouselContentCards(
        channelId: String, shopId: String,
        promotions: List<String>, channelType: String, filterCategory: String, position: Int
    ) {
        createAnalyticsData(
            EventName.PROMO_CLICK,
            EventAction.CLICK_CONTENT_CARDS_UPCOMING,
            EventCategory.CONTENT_FEED_TIMELINE_VIDEO,
            EventLabel.liveVodUpcomingFilterCategory(
                channelId,
                shopId,
                channelType,
                filterCategory
            ),
            getPromotions(channelId, FEED_VIDEO_TAB_CONTENT_CARDS_UPCOMING, position, promotions)
        )
    }

    //19
    fun clickOnSeeAllOnUpcomingCarousel(filterCategory: String) {
        createAnalyticsData(
            EventName.CLICK_HOMEPAGE,
            EventAction.CLICK_SEE_ALL_UPCOMING,
            EventCategory.CONTENT_FEED_TIMELINE_VIDEO,
            EventLabel.getFilterCategory(filterCategory)
        )
    }

    //20
    fun visitVideoTabPageOnFeed(position: Int) {
        createAnalyticsData(
            EventName.OPEN_SCREEN,
            "", "", "", isLoggedInStatus = userSession.isLoggedIn,
            screenName = EventLabel.getScreenName(position)
        )
    }

    //23
    fun impressOnContentHighlightCard(
        channelId: String, shopId: String, promotions: List<String>,
        channelType: String, position: Int
    ) {
        createAnalyticsData(
            EventName.PROMO_VIEW,
            EventAction.IMPRESSION_CONTENT_HIGHLIGHT_CARDS,
            EventCategory.CONTENT_FEED_TIMELINE_VIDEO,
            EventLabel.liveVodUpcoming(channelId, shopId, channelType),
            getPromotions(channelId, FEED_VIDEO_TAB_HIGHLIGHT_CARDS, position, promotions)
        )
    }

    //24
    fun clickOnRemindMeButtonOnPlayCardsWithinChip(
        channelId: String, shopId: String,
        channelType: String, filterCategory: String
    ) {
        createAnalyticsData(
            EventName.CLICK_HOMEPAGE,
            EventAction.CLICK_REMIND,
            EventCategory.CONTENT_FEED_TIMELINE_VIDEO,
            EventLabel.liveVodUpcomingFilterCategory(
                channelId,
                shopId,
                channelType,
                filterCategory
            )
        )
    }

    //25
    fun clickOnUnRemindMeButtonOnPlayCardsWithinChip(
        channelId: String, shopId: String,
        channelType: String, filterCategory: String
    ) {
        createAnalyticsData(
            EventName.CLICK_HOMEPAGE,
            EventAction.CLICK_UNREMIND,
            EventCategory.CONTENT_FEED_TIMELINE_VIDEO,
            EventLabel.liveVodUpcomingFilterCategory(
                channelId,
                shopId,
                channelType,
                filterCategory
            )
        )
    }

    //26
    fun clickOnRemindMeButtonOnPlayCardInUpcomingCarousel(
        channelId: String, shopId: String, channelType: String, filterCategory: String
    ) {
        createAnalyticsData(
            EventName.CLICK_HOMEPAGE,
            EventAction.CLICK_REMIND_UPCOMING,
            EventCategory.CONTENT_FEED_TIMELINE_VIDEO,
            EventLabel.liveVodUpcomingFilterCategory(
                channelId,
                shopId,
                channelType,
                filterCategory
            )
        )
    }

    //27
    fun clickOnUnRemindMeButtonOnPlayCardInUpcomingCarousel(
        channelId: String, shopId: String, channelType: String, filterCategory: String
    ) {
        createAnalyticsData(
            EventName.CLICK_HOMEPAGE,
            EventAction.CLICK_UNREMIND_UPCOMING,
            EventCategory.CONTENT_FEED_TIMELINE_VIDEO,
            EventLabel.liveVodUpcomingFilterCategory(
                channelId,
                shopId,
                channelType,
                filterCategory
            )
        )
    }

    //28
    fun clickOnRemindMeButtonOnPlayCardInContentHighlight(
        channelId: String,
        shopId: String,
        promotions: List<String>, channelType: String, filterCategory: String
    ) {
        createAnalyticsData(
            EventName.CLICK_HOMEPAGE,
            EventAction.CLICK_REMIND_CONTENTHIGHLIGHT,
            EventCategory.CONTENT_FEED_TIMELINE_VIDEO,
            EventLabel.liveVodUpcoming(
                channelId,
                shopId,
                channelType,
                filterCategory
            )
        )
    }

    //29
    fun clickOnUnRemindMeButtonOnPlayCardInContentHighlight(
        channelId: String, shopId: String,
        promotions: List<String>, channelType: String, filterCategory: String
    ) {
        createAnalyticsData(
            EventName.CLICK_HOMEPAGE,
            EventAction.CLICK_UNREMIND_CONTENTHIGHLIGHT,
            EventCategory.CONTENT_FEED_TIMELINE_VIDEO,
            EventLabel.liveVodUpcoming(
                channelId,
                shopId,
                channelType,
                filterCategory
            )
        )
    }


    private fun createAnalyticsData(
        eventName: String,
        eventAction: String = "",
        eventCategory: String = "",
        eventLabel: String = "",
        promotions: Map<String, Any>? = null,
        isLoggedInStatus: Boolean? = null,
        screenName: String = ""
    ) {
        val generalData = DataLayer.mapOf(
            TrackAppUtils.EVENT, eventName,
            TrackAppUtils.EVENT_ACTION, eventAction,
            TrackAppUtils.EVENT_CATEGORY, eventCategory,
            TrackAppUtils.EVENT_LABEL, eventLabel,
            PlayTrackerConst.BUSINESS_UNIT, PlayTrackerConst.CONTENT,
            PlayTrackerConst.CURRENT_SITE, PlayTrackerConst.TOKOPEDIA_MARKET_PLACE,
            PlayTrackerConst.SESSION_IRIS, irisSession.getSessionId(),
            PlayTrackerConst.USER_ID, userSession.userId,
            PlayTrackerConst.IS_LOGGED_IN, isLoggedInStatus,
            PlayTrackerConst.SCREEN_NAME, screenName
        )
        if (promotions == null)
            TrackApp.getInstance().gtm.sendGeneralEvent(generalData)
        else {
            trackingQueue.putEETracking(generalData.plus(DataLayer.mapOf(ECOMMERCE, promotions)) as HashMap<String, Any>)
        }
    }

    private fun getPromotions(
        channelId: String, name: String, position: Int, creative: List<String>
    ): Map<String, Any> {
        val listPromo = mutableListOf<HashMap<String, String>>()
        creative.forEach {
            listPromo.add(
                DataLayer.mapOf(
                    "creative", it,
                    "id", channelId,
                    "name", name,
                    "position", (position + 1).toString()
                ) as HashMap<String, String>
            )
        }

        return mapOf(
            "promoClick" to mapOf(
                "promotions" to listPromo
            )
        )

        /*val promoList = mutableListOf<AnalyticsPromo.PromoClick.Promotions>()
        creative.forEach {
            promoList.add(
                AnalyticsPromo.PromoClick.Promotions(
                    it, channelId, name, (position + 1).toString()
                )
            )
        }
        val obj = AnalyticsPromo(AnalyticsPromo.PromoClick(promoList))
        return obj*/
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
    }

    private object EventAction {
        const val CLICK_REMIND = "click - remind"
        const val CLICK_REMIND_UPCOMING = "click - remind - upcoming"
        const val CLICK_REMIND_CONTENTHIGHLIGHT = "click - remind - content highlight"
        const val CLICK_UNREMIND = "click - unremind"
        const val CLICK_UNREMIND_UPCOMING = "click - unremind - upcoming"
        const val CLICK_UNREMIND_CONTENTHIGHLIGHT = "click - unremind - content highlight"
        const val CLICK_FEED_TAB = "click - feed tab"
        const val CLICK_CONTENT_HIGHLIGHT_CARDS = "click - content highlight cards"
        const val CLICK_CONTENT_CARD_AGAIN_LIVE = "click - content cards - again live"
        const val CLICK_SEE_ALL_UPCOMING = "click - see all - upcoming"
        const val CLICK_LIHAT_SEMUA_LAGI_LIVE = "click - lihat semua - lagi live"
        const val CLICK_LIHAT_SEMUA_UPCOMING = "click - lihat semua - upcoming"
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
        const val IMPRESSION_CONTENT_CARDS_AGAIN_LIVE = "impression - content cards - again live"
        const val IMPRESSION_CONTENT_CARDS_UPCOMING = "impressions - content cards - upcoming"
        const val IMPRESSION_CONTENT_CARDS = "impressions - content cards"
        const val IMPRESSION_FILTER_CHIPS = "impression - filter chips"
    }

    private object EventLabel {
        const val UPDATE = "update"
        const val EXPLORE = "explore"
        const val VIDEO = "video"
        const val WIDGET_LIVE = "live"
        const val WIDGET_UPCOMING = "upcoming"
        fun liveVodUpcoming(channelId: String, shopId: String, type: String) =
            "{${channelId}} - {${shopId}} - {${type}}"

        fun liveVodUpcomingFilterCategory(
            channelId: String,
            shopId: String,
            type: String,
            filterCategory: String
        ) = "{${channelId}} - {${shopId}} - {${type}} - {${filterCategory}}"

        fun liveVodUpcoming(
            channelId: String,
            shopId: String,
            type: String,
            filterCategory: String
        ) = "{${channelId}} - {${shopId}} - {${type}}"

        fun liveVodUpcomingFilterCategoryEntryPointCarousel(
            channelId: String, shopId: String, type: String, filterCategory: String
        ) =
            "{${channelId}} - {${shopId}} - {${type}} - {${filterCategory}} - {entry point carousel widget}"

        const val FILTER_CATEGORY_ENTRY_POINT_CAROUSEL_WIDGET =
            "{filter category} - {entry point carousel widget}"

        fun getFilterCategory(filterCategory: String) = "{${filterCategory}}"

        fun getScreenName(position: Int): String {
            return when (position) {
                0 -> "/feed - update tab"
                1 -> "/feed - explore tab"
                2 -> "/feed - video tab"
                else -> ""
            }
        }
    }

    companion object {
        private const val FEED_VIDEO_TAB_CONTENT_CARDS_UPCOMING =
            "/feed video tab - content cards - upcoming"
        private const val FEED_VIDEO_TAB_CONTENT_LIST_PAGE_CONTENT_CARDS =
            "/feed video tab - content list page - content cards"
        private const val FEED_VIDEO_TAB_CONTENT_CARDS_AGAIN_LIVE =
            "/feed video tab - content cards - again live"
        private const val FEED_VIDEO_TAB_HIGHLIGHT_CARDS = "/feed video tab - highlight cards"
    }
}