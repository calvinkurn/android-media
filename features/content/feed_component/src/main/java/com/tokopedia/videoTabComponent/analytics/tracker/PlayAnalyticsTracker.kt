package com.tokopedia.videoTabComponent.analytics.tracker

import android.os.Bundle
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.trackingoptimizer.constant.Constant.Companion.ECOMMERCE
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.videoTabComponent.analytics.tracker.PlayAnalyticsTracker.EventName.PROMO_CLICK
import com.tokopedia.videoTabComponent.analytics.tracker.PlayAnalyticsTracker.EventName.PROMO_VIEW
import com.tokopedia.videoTabComponent.analytics.tracker.PlayAnalyticsTracker.PlayTrackerConst.PROMOTIONS
import com.tokopedia.videoTabComponent.analytics.tracker.PlayAnalyticsTracker.PlayTrackerConst.USER_ID
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class PlayAnalyticsTracker @Inject constructor(
    private val irisSession: IrisSession,
    private val trackingQueue: TrackingQueue,
    private val userSession: UserSessionInterface
) {

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
        trackEnhancedEcommerceEventNew(
                PROMO_CLICK,
                EventAction.CLICK_CONTENT_HIGHLIGHT_CARDS,
                EventCategory.CONTENT_FEED_TIMELINE_VIDEO,
                EventLabel.liveVodUpcoming(channelId, shopId, channelType),
                channelId, FEED_VIDEO_TAB_HIGHLIGHT_CARDS, position, promotions
        )

    }

    //4
    fun impressOnLagiLiveContentCarouselWidget() {
        createAnalyticsData(
                PROMO_VIEW,
                EventAction.IMPRESSION_CAROUSEL_WIDGET_LAGI_LIVE,
                EventCategory.CONTENT_FEED_TIMELINE_VIDEO,
                ""
        )
    }

    //5
    fun impressOnLagiLiveCarouselContentCards(
        channelId: String, shopId: String,
        promotions: List<String>, channelType: String, position: Int
    ) {
       trackEnhancedEcommerceEventNew(
            PROMO_VIEW,
            EventAction.IMPRESSION_CONTENT_CARDS_LAGI_LIVE,
            EventCategory.CONTENT_FEED_TIMELINE_VIDEO,
            EventLabel.liveVodUpcoming(channelId, shopId, channelType),
            channelId, FEED_VIDEO_TAB_CONTENT_CARDS_LAGI_LIVE, position, promotions
        )

    }

    //6
    fun clickOnLagiLiveCarouselContentCards(
            channelId: String,
            shopId: String,
            promotions: List<String>, channelType: String, position: Int
    ) {
        trackEnhancedEcommerceEventNew(
                PROMO_CLICK,
                EventAction.CLICK_CONTENT_CARD_LAGI_LIVE,
                EventCategory.CONTENT_FEED_TIMELINE_VIDEO,
                EventLabel.liveVodUpcoming(channelId, shopId, channelType),
                channelId, FEED_VIDEO_TAB_CONTENT_CARDS_LAGI_LIVE, position, promotions
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

    //8//will be used in video detail page
    fun impressOnContentCardsInContentListPageForLagiLive(
        channelId: String, shopId: String,
        promotions: List<String>, channelType: String, filterCategory: String, position: Int, entryPoint: String
    ) {
        trackEnhancedEcommerceEventNew(
                PROMO_VIEW,
                EventAction.IMPRESSION_CONTENT_CARDS,
                EventCategory.CONTENT_FEED_TIMELINE_VIDEO_CONTENT_LIST_PAGE,
                EventLabel.liveVodUpcomingFilterCategoryEntryPointCarousel(
                        channelId,
                        shopId,
                        channelType,
                        filterCategory,
                        entryPoint
                ),
                channelId, FEED_VIDEO_TAB_CONTENT_LIST_PAGE_CONTENT_CARDS, position, promotions

        )


    }

    //9//will be used  in video detail page
    fun clickOnContentCardsInContentListPageForLagiLive(
            channelId: String, shopId: String,
            promotions: List<String>, channelType: String, filterCategory: String, position: Int, entryPoint: String
    ) {
        trackEnhancedEcommerceEventNew(
                PROMO_CLICK,
                EventAction.CLICK_CONTENT_CARDS,
                EventCategory.CONTENT_FEED_TIMELINE_VIDEO_CONTENT_LIST_PAGE,
                EventLabel.liveVodUpcomingFilterCategoryEntryPointCarousel(
                        channelId,
                        shopId,
                        channelType,
                        filterCategory,
                        entryPoint
                ),

                channelId, FEED_VIDEO_TAB_CONTENT_LIST_PAGE_CONTENT_CARDS, position, promotions

        )

    }

    //10//will be used  later
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
        trackEnhancedEcommerceEventNew(
                PROMO_VIEW,
                EventAction.IMPRESSION_CONTENT_CARDS,
                EventCategory.CONTENT_FEED_TIMELINE_VIDEO,
                EventLabel.liveVodUpcomingFilterCategory(
                        channelId,
                        shopId,
                        channelType,
                        filterCategory
                ),
                channelId, FEED_VIDEO_TAB_CONTENT_CARDS, position, promotions)


    }

    //15
    fun clickOnContentCardsInVideoTabBelowTheChips(
            channelId: String, shopId: String,
            promotions: List<String>, channelType: String, filterCategory: String, position: Int
    ) {
        trackEnhancedEcommerceEventNew(
                PROMO_CLICK,
                EventAction.CLICK_CONTENT_CARDS,
                EventCategory.CONTENT_FEED_TIMELINE_VIDEO,
                EventLabel.liveVodUpcomingFilterCategory(
                        channelId,
                        shopId,
                        channelType,
                        filterCategory
                ),
                channelId, FEED_VIDEO_TAB_CONTENT_CARDS, position, promotions)

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
        trackEnhancedEcommerceEventNew(
                PROMO_VIEW,
                EventAction.IMPRESSION_CONTENT_CARDS_UPCOMING,
                EventCategory.CONTENT_FEED_TIMELINE_VIDEO,
                EventLabel.liveVodUpcomingFilterCategory(
                        channelId,
                        shopId,
                        channelType,
                        filterCategory
                ),
                channelId, FEED_VIDEO_TAB_CONTENT_CARDS_UPCOMING, position, promotions)


    }

    //18
    fun clickOnUpcomingCarouselContentCards(
            channelId: String, shopId: String,
            promotions: List<String>, channelType: String, filterCategory: String, position: Int
    ) {
        trackEnhancedEcommerceEventNew(
                PROMO_CLICK,
                EventAction.CLICK_CONTENT_CARDS_UPCOMING,
                EventCategory.CONTENT_FEED_TIMELINE_VIDEO,
                EventLabel.liveVodUpcomingFilterCategory(
                        channelId,
                        shopId,
                        channelType,
                        filterCategory
                ),
                channelId, FEED_VIDEO_TAB_CONTENT_CARDS_UPCOMING, position, promotions)


    }

    //19
    fun clickOnSeeAllOnUpcomingCarousel(filterCategory: String) {
        createAnalyticsData(
            EventName.CLICK_HOMEPAGE,
            EventAction.CLICK_LIHAT_SEMUA_UPCOMING,
            EventCategory.CONTENT_FEED_TIMELINE_VIDEO,
            EventLabel.getFilterCategory(filterCategory)
        )
    }

    //20
    fun visitVideoTabPageOnFeed(position: Int) {
        createAnalyticsForOpenScreen(
            EventName.OPEN_SCREEN,
            isLoggedInStatus = userSession.isLoggedIn,
            screenName = EventLabel.getScreenName(position)
        )
    }

    //23
    fun impressOnContentHighlightCard(
        channelId: String, shopId: String, promotions: List<String>,
        channelType: String, position: Int
    ) {
        trackEnhancedEcommerceEventNew(
            PROMO_VIEW,
            EventAction.IMPRESSION_CONTENT_HIGHLIGHT_CARDS,
            EventCategory.CONTENT_FEED_TIMELINE_VIDEO,
            EventLabel.liveVodUpcoming(channelId, shopId, channelType),
            channelId, FEED_VIDEO_TAB_HIGHLIGHT_CARDS, position, promotions)


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

        )

            TrackApp.getInstance().gtm.sendGeneralEvent(generalData)

    }

    private fun createAnalyticsForOpenScreen(
            eventName: String,
            isLoggedInStatus: Boolean? = null,
            screenName: String = ""
    ) {
        val generalData = mapOf(
                TrackAppUtils.EVENT to eventName,
                PlayTrackerConst.BUSINESS_UNIT to PlayTrackerConst.CONTENT,
                PlayTrackerConst.CURRENT_SITE to PlayTrackerConst.TOKOPEDIA_MARKET_PLACE,
                PlayTrackerConst.SESSION_IRIS to irisSession.getSessionId(),
                PlayTrackerConst.USER_ID to userSession.userId.toString(),
                PlayTrackerConst.IS_LOGGED_IN to isLoggedInStatus.toString(),
                PlayTrackerConst.SCREEN_NAME to screenName
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(generalData)

    }



    private fun sendEnhanceEcommerceEvent(eventName: String, bundle: Bundle) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(eventName, bundle)
    }

    /**
     * Data Generator as Bundle
     */
    private fun trackEnhancedEcommerceEventNewAsBundle(
            eventName: String,
            eventAction: String,
            eventCategory: String,
            eventLabel: String,
            channelId: String, name: String, position: Int, creative: List<String>
    ): Bundle {
        return Bundle().apply {
            putString(TrackAppUtils.EVENT, eventName)
            putString(TrackAppUtils.EVENT_CATEGORY, eventCategory)
            putString(TrackAppUtils.EVENT_ACTION, eventAction)
            putString(TrackAppUtils.EVENT_LABEL, eventLabel)
            putString(PlayTrackerConst.BUSINESS_UNIT, PlayTrackerConst.CONTENT)
            putString(PlayTrackerConst.CURRENT_SITE, PlayTrackerConst.TOKOPEDIA_MARKET_PLACE)
            putString(PlayTrackerConst.SESSION_IRIS, irisSession.getSessionId())
            putString(PlayTrackerConst.USER_ID, userSession.userId)
            val items = getBundleItems(channelId, name, position, creative)

            val listBundle = Bundle().apply {
                putParcelableArrayList(PROMOTIONS, items)
            }
            if (eventName == PROMO_VIEW)
            putBundle(ECOMMERCE, getPromoViewDataBundle(listBundle))
            else
            putBundle(ECOMMERCE, getPromoClickDataBundle(listBundle))

        }
    }
    private fun trackEnhancedEcommerceEventNew(
            eventName: String,
            eventAction: String,
            eventCategory: String,
            eventLabel: String,
            channelId: String, name: String, position: Int, creative: List<String>
    ) {
        val eCommerceData =
                if (eventName == PROMO_VIEW) {
                    getPromotionsViewData(channelId, name, position, creative)
                } else {
                    getPromotionsClickData(channelId, name, position, creative)
                }
        trackingQueue.putEETracking(
                getGeneralDataNew(eventName, eventCategory, eventAction, eventLabel)
                        .plus(getEcommerceData(eCommerceData)) as HashMap<String, Any>
        )
        
    }
    /**
     * Data Generator
     */
    private fun getGeneralDataNew(
            eventName: String,
            eventCategory: String,
            eventAction: String,
            eventLabel: String,
    ): Map<String, Any> = DataLayer.mapOf(
            TrackAppUtils.EVENT, eventName,
            TrackAppUtils.EVENT_CATEGORY, eventCategory,
            TrackAppUtils.EVENT_ACTION, eventAction,
            TrackAppUtils.EVENT_LABEL, eventLabel,
            USER_ID, userSession.userId,
            PlayTrackerConst.BUSINESS_UNIT, PlayTrackerConst.CONTENT,
            PlayTrackerConst.CURRENT_SITE, PlayTrackerConst.TOKOPEDIA_MARKET_PLACE
    )

    private fun getBundleItems(channelId: String, name: String, position: Int, creative: List<String>): ArrayList<Bundle> {
        val items = ArrayList<Bundle>()
        creative.forEach {
            val bundle = getBundleItem(channelId, name, position, it)
            items.add(bundle)
        }
        return items
    }
    private fun getBundleItem(channelId: String, name: String, position: Int, creative: String): Bundle {
        return Bundle().apply {
                            putString(Promotions.CREATIVE, creative.toEnhancedEcommerceDefaultValueIfEmpty())
                            putString(Promotions.ID, channelId.toEnhancedEcommerceDefaultValueIfEmpty())
                            putString( Promotions.NAME, name.toEnhancedEcommerceDefaultValueIfEmpty())
                            putString(Promotions.POSITION, (position + 1).toString().toEnhancedEcommerceDefaultValueIfEmpty())

        }
    }

    private fun String.toEnhancedEcommerceDefaultValueIfEmpty(): String {
        return if (this.isBlank()) "none/other" else this
    }

    private fun getPromoViewDataBundle(data: Bundle): Bundle =
           Bundle().apply { putBundle(PROMO_VIEW, data) }

    private fun getPromoClickDataBundle(data: Bundle): Bundle =
            Bundle().apply { putBundle(PROMO_CLICK, data) }

    private fun getEcommerceData(data: Any): Map<String, Any> = DataLayer.mapOf(ECOMMERCE, data)

    private fun getPromoClickData(data: Any): Map<String, Any> =
            DataLayer.mapOf(PROMO_CLICK, data)

    private fun getPromoViewData(data: Any): Map<String, Any> =
            DataLayer.mapOf(PROMO_VIEW, data)

    private fun getPromotionsData(
            promotionDataList: List<Any>,
    ): Map<String, Any> = DataLayer.mapOf(PROMOTIONS, promotionDataList)

    private fun getPromotionsClickData(
            channelId: String, name: String, position: Int, creative: List<String>
    ): Map<String, Any> {
        val listPromo: MutableList<Map<String, Any>> = mutableListOf()
        creative.forEach {
            listPromo.add(
                    DataLayer.mapOf(
                            Promotions.CREATIVE, it,
                            Promotions.ID, channelId,
                            Promotions.NAME, name,
                            Promotions.POSITION, (position + 1).toString()
                    )
            )
        }

        return getPromoClickData(
                getPromotionsData(
                        listPromo
                )
        )

    }

    private fun getPromotionsViewData(
            channelId: String, name: String, position: Int, creative: List<String>
    ): Map<String, Any> {
        val listPromo: MutableList<Map<String, Any>> = mutableListOf()
        creative.forEach {
            listPromo.add(
                    DataLayer.mapOf(
                            Promotions.CREATIVE, it,
                            Promotions.ID, channelId,
                            Promotions.NAME, name,
                            Promotions.POSITION, (position + 1).toString()
                    )
            )
        }

        return getPromoViewData(
                getPromotionsData(
                        listPromo
                )
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
        const val CLICK_CONTENT_HIGHLIGHT_CARDS = "click - content highlight cards"
        const val CLICK_CONTENT_CARD_LAGI_LIVE = "click - content cards - lagi live"
        const val CLICK_LIHAT_SEMUA_UPCOMING = "click - lihat semua - upcoming"
        const val CLICK_LIHAT_SEMUA_LAGI_LIVE = "click - lihat semua - lagi live"
        const val CLICK_CONTENT_CARDS = "click - content cards"
        const val CLICK_CONTENT_CARDS_UPCOMING = "click - content cards - upcoming"
        const val CLICK_FILTER_CHIPS = "click - filter chips"

        const val IMPRESSION_CONTENT_HIGHLIGHT_WIDGET = "impression - content highlight widget"
        const val IMPRESSION_CONTENT_HIGHLIGHT_CARDS = "impression - content highlight cards"
        const val IMPRESSION_CAROUSEL_WIDGET_LAGI_LIVE = "impression - carousel widget - lagi live"

        const val IMPRESSION_CAROUSEL_WIDGET_UPCOMING = "impression - carousel widget - upcoming"
        const val IMPRESSION_CONTENT_CARDS_LAGI_LIVE = "impression - content cards - lagi live"

        const val IMPRESSION_CONTENT_CARDS_UPCOMING = "impressions - content cards - upcoming"
        const val IMPRESSION_CONTENT_CARDS = "impressions - content cards"
        const val IMPRESSION_FILTER_CHIPS = "impression - filter chips"
    }
    private object Promotions{
        const val CREATIVE ="creative"
        const val ID = "id"
        const val NAME = "name"
        const val POSITION = "position"

    }

    private object EventLabel {

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
            channelId: String, shopId: String, type: String, filterCategory: String, entryPoint: String =""
        ) =
            "{${channelId}} - {${shopId}} - {${type}} - $FILTER_CATEGORY_EMPTY - {${entryPoint}}"

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
        private const val FILTER_CATEGORY_EMPTY = ""
        private const val FEED_VIDEO_TAB_CONTENT_CARDS_UPCOMING =
            "/feed video tab - content cards - upcoming"
        private const val FEED_VIDEO_TAB_CONTENT_LIST_PAGE_CONTENT_CARDS =
            "/feed video tab - content list page - content cards"
        private const val FEED_VIDEO_TAB_CONTENT_CARDS_LAGI_LIVE =
            "/feed video tab - content cards - lagi live"
        private const val FEED_VIDEO_TAB_CONTENT_CARDS =
                "/feed video tab - content cards"
        private const val FEED_VIDEO_TAB_HIGHLIGHT_CARDS = "/feed video tab - highlight cards"
    }
}