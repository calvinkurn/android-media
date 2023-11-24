package com.tokopedia.creation.common.analytics

import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.CurrentSite
import com.tokopedia.content.analytic.Event
import com.tokopedia.content.analytic.EventCategory
import com.tokopedia.content.analytic.Value
import com.tokopedia.config.GlobalConfig
import com.tokopedia.creation.common.presentation.model.ContentCreationAuthorEnum
import com.tokopedia.creation.common.presentation.model.ContentCreationEntryPointSource
import com.tokopedia.track.TrackApp
import com.tokopedia.track.constant.TrackerConstant
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By : Muhammad Furqan on 27/10/23
 *
 * Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4256
 */
class ContentCreationAnalytics @Inject constructor(
    val userSession: UserSessionInterface
) {

    private val userId: String
        get() = userSession.userId
    private val shopId: String
        get() = userSession.shopId

    fun eventClickNextButton(
        authorType: ContentCreationAuthorEnum,
        contentTypeTitle: String,
        widgetSource: ContentCreationEntryPointSource
    ) {
        sendEventTracker(
            generateGeneralTrackerMapData(
                eventName = Event.clickContent,
                eventAction = Action.CLICK_NEXT_CONTENT_CREATION,
                eventLabel = "${getPartnerId(authorType)} - ${getUserType(authorType)} - ${contentTypeTitle.lowercase()}",
                trackerId = getTrackerIdBasedOnWidgetSource(
                    widgetSource,
                    trackerIdShopPageMainApp = "47546",
                    trackerIdShopPageSellerApp = "47889",
                    trackerIdFeed = "47965",
                ),
                widgetSource = widgetSource
            )
        )
    }

    fun eventClickPerformanceDashboard(
        authorType: ContentCreationAuthorEnum,
        widgetSource: ContentCreationEntryPointSource
    ) {
        sendEventTracker(
            generateGeneralTrackerMapData(
                eventName = Event.clickContent,
                eventAction = Action.CLICK_PERFORMANCE_DASHBOARD_CONTENT_CREATION,
                eventLabel = "${getPartnerId(authorType)} - ${getUserType(authorType)}",
                trackerId = getTrackerIdBasedOnWidgetSource(
                    widgetSource,
                    trackerIdShopPageMainApp = "47550",
                    trackerIdShopPageSellerApp = "47890",
                    trackerIdFeed = "",
                ),
                widgetSource = widgetSource
            )
        )
    }

    fun eventImpressionContentCreationBottomSheet(
        authorType: ContentCreationAuthorEnum,
        widgetSource: ContentCreationEntryPointSource
    ) {
        sendEventTracker(
            generateGeneralTrackerMapData(
                eventName = Event.viewContentIris,
                eventAction = Action.VIEW_CONTENT_CREATION_BOTTOM_SHEET,
                eventLabel = "${getPartnerId(authorType)} - ${getUserType(authorType)}",
                trackerId = getTrackerIdBasedOnWidgetSource(
                    widgetSource,
                    trackerIdShopPageMainApp = "47551",
                    trackerIdShopPageSellerApp = "47891",
                    trackerIdFeed = "47966",
                ),
                widgetSource = widgetSource
            )
        )
    }

    fun eventImpressionContentCreationEndpointWidget(
        authorType: ContentCreationAuthorEnum,
        widgetSource: ContentCreationEntryPointSource
    ) {
        sendEventTracker(
            generateGeneralTrackerMapData(
                eventName = Event.viewContentIris,
                eventAction = Action.VIEW_CREATE_CONTENT,
                eventLabel = "${getPartnerId(authorType)} - ${getUserType(authorType)}",
                trackerId = getTrackerIdBasedOnWidgetSource(
                    widgetSource,
                    trackerIdShopPageMainApp = "47797",
                    trackerIdShopPageSellerApp = "47892",
                    trackerIdFeed = "",
                ),
                widgetSource = widgetSource
            )
        )
    }

    fun clickContentCreationEndpointWidget(
        authorType: ContentCreationAuthorEnum,
        widgetSource: ContentCreationEntryPointSource
    ) {
        sendEventTracker(
            generateGeneralTrackerMapData(
                eventName = Event.clickContent,
                eventAction = Action.CLICK_CREATE_CONTENT,
                eventLabel = "${getPartnerId(authorType)} - ${getUserType(authorType)}",
                trackerId = getTrackerIdBasedOnWidgetSource(
                    widgetSource,
                    trackerIdShopPageMainApp = "47798",
                    trackerIdShopPageSellerApp = "47893",
                    trackerIdFeed = "",
                ),
                widgetSource = widgetSource
            )
        )
    }

    private fun getTrackerIdBasedOnWidgetSource(
        widgetSource: ContentCreationEntryPointSource,
        trackerIdShopPageMainApp: String,
        trackerIdShopPageSellerApp: String,
        trackerIdFeed: String
    ): String {
        return if (widgetSource == ContentCreationEntryPointSource.Shop) {
            if (GlobalConfig.isSellerApp()) {
                trackerIdShopPageSellerApp
            } else {
                trackerIdShopPageMainApp
            }
        }
        else {
            trackerIdFeed
        }
    }

    private fun sendEventTracker(params: Map<String, Any>) {
        TrackApp.getInstance().gtm.sendGeneralEvent(params)
    }

    private fun generateGeneralTrackerMapData(
        eventName: String,
        eventAction: String,
        eventLabel: String,
        trackerId: String,
        widgetSource: ContentCreationEntryPointSource
    ): Map<String, Any> = mapOf(
        TrackerConstant.EVENT to eventName,
        TrackerConstant.EVENT_CATEGORY to getEventCategory(widgetSource),
        TrackerConstant.EVENT_ACTION to eventAction,
        TrackerConstant.EVENT_LABEL to eventLabel,
        TrackerConstant.BUSINESS_UNIT to BusinessUnit.content,
        TrackerConstant.CURRENT_SITE to if (GlobalConfig.isSellerApp()) CurrentSite.tokopediaSeller else CurrentSite.tokopediaMarketplace,
        TrackerConstant.TRACKER_ID to trackerId,
        TrackerConstant.USERID to userId
    )

    private fun getUserType(authorEnum: ContentCreationAuthorEnum) =
        when (authorEnum) {
            ContentCreationAuthorEnum.USER -> Value.user
            ContentCreationAuthorEnum.SHOP -> Value.seller
            ContentCreationAuthorEnum.NONE -> ""
        }

    private fun getPartnerId(authorEnum: ContentCreationAuthorEnum) =
        when (authorEnum) {
            ContentCreationAuthorEnum.USER -> userId
            ContentCreationAuthorEnum.SHOP -> shopId
            ContentCreationAuthorEnum.NONE -> ""
        }

    private fun getEventCategory(widgetSource: ContentCreationEntryPointSource) =
        when (widgetSource) {
            ContentCreationEntryPointSource.Shop -> VALUE_CATEGORY_SHOP
            ContentCreationEntryPointSource.Feed -> EventCategory.unifiedFeed
            else -> ""
        }

    private object Action {
        const val CLICK_NEXT_CONTENT_CREATION = "click - lanjut content creation"
        const val CLICK_PERFORMANCE_DASHBOARD_CONTENT_CREATION =
            "click - analytics dashboard entry point"
        const val VIEW_CONTENT_CREATION_BOTTOM_SHEET = "view - content creation selection page"
        const val VIEW_CREATE_CONTENT = "view - create content"
        const val CLICK_CREATE_CONTENT = "click - create content"
    }

    companion object {
        private const val VALUE_CATEGORY_SHOP = "shop page - seller"
    }
}
