package com.tokopedia.creation.common.analytics

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
    userSession: UserSessionInterface
) {

    private val userId = userSession.userId
    private val shopId = userSession.shopId

    fun eventClickNextButton(
        authorType: ContentCreationAuthorEnum,
        contentTypeTitle: String,
        widgetSource: ContentCreationEntryPointSource
    ) {
        sendEventTracker(
            generateGeneralTrackerMapData(
                eventName = Event.CLICK_CONTENT,
                eventAction = Action.CLICK_NEXT_CONTENT_CREATION,
                eventLabel = "${getPartnerId(authorType)} - ${getUserType(authorType)} - ${contentTypeTitle.lowercase()}",
                trackerId = if (widgetSource == ContentCreationEntryPointSource.Shop) "47546" else "47965",
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
                eventName = Event.CLICK_CONTENT,
                eventAction = Action.CLICK_PERFORMANCE_DASHBOARD_CONTENT_CREATION,
                eventLabel = "${getPartnerId(authorType)} - ${getUserType(authorType)}",
                trackerId = "47550",
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
                eventName = Event.VIEW_CONTENT_IRIS,
                eventAction = Action.VIEW_CONTENT_CREATION_BOTTOM_SHEET,
                eventLabel = "${getPartnerId(authorType)} - ${getUserType(authorType)}",
                trackerId = if (widgetSource == ContentCreationEntryPointSource.Shop) "47551" else "47966",
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
                eventName = Event.VIEW_CONTENT_IRIS,
                eventAction = Action.VIEW_CREATE_CONTENT,
                eventLabel = "${getPartnerId(authorType)} - ${getUserType(authorType)}",
                trackerId = "47797",
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
                eventName = Event.CLICK_CONTENT,
                eventAction = Action.CLICK_CREATE_CONTENT,
                eventLabel = "${getPartnerId(authorType)} - ${getUserType(authorType)}",
                trackerId = "47798",
                widgetSource = widgetSource
            )
        )
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
        TrackerConstant.BUSINESS_UNIT to VALUE_BUSINESS_UNIT,
        TrackerConstant.CURRENT_SITE to VALUE_CURRENT_SITE,
        TrackerConstant.TRACKER_ID to trackerId,
        TrackerConstant.USERID to userId
    )

    private fun getUserType(authorEnum: ContentCreationAuthorEnum) =
        when (authorEnum) {
            ContentCreationAuthorEnum.USER -> VALUE_USER
            ContentCreationAuthorEnum.SHOP -> VALUE_SELLER
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
            ContentCreationEntryPointSource.Feed -> VALUE_CATEGORY_FEED
            else -> ""
        }

    private object Event {
        const val CLICK_CONTENT = "clickContent"
        const val VIEW_CONTENT_IRIS = "viewContentIris"
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
        private const val VALUE_BUSINESS_UNIT = "content"
        private const val VALUE_CURRENT_SITE = "tokopediamarketplace"

        private const val VALUE_SELLER = "seller"
        private const val VALUE_USER = "user"

        private const val VALUE_CATEGORY_SHOP = "shop page - seller"
        private const val VALUE_CATEGORY_FEED = "unified feed"
    }
}
