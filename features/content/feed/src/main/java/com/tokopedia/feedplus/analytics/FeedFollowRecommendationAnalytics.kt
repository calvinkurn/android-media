package com.tokopedia.feedplus.analytics

import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.CurrentSite
import com.tokopedia.content.analytic.Event
import com.tokopedia.content.analytic.EventCategory
import com.tokopedia.content.analytic.Key
import com.tokopedia.content.analytic.Value
import com.tokopedia.feedplus.presentation.model.FeedFollowRecommendationModel
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import com.tokopedia.content.common.analytic.utils.*
import com.tokopedia.feedplus.presentation.model.FeedTrackerDataModel
import com.tokopedia.feedplus.presentation.model.type.AuthorType

/**
 * Created By : Jonathan Darwin on August 01, 2023
 */
class FeedFollowRecommendationAnalytics @Inject constructor(
    private val userSession: UserSessionInterface
) {

    /**
     * Mynakama : https://mynakama.tokopedia.com/datatracker/requestdetail/view/3772
     * Row 50 - 54
     */

    /** Row 50 */
    fun eventImpressProfileRecommendation(
        data: FeedTrackerDataModel
    ) {
        
    }

    /** Row 51 */
    fun eventClickProfileRecommendation(
        data: FeedTrackerDataModel
    ) {

    }

    /** Row 52 */
    fun eventClickFollowProfileRecommendation(
        data: FeedTrackerDataModel
    ) {
        sendGeneralTracker(
            eventAction = "click - follow profile recommendations",
            eventLabel = getEventLabel(data.type, data.entryPoint, data.authorType, data.authorId),
            trackerId = "45541"
        )
    }

    /** Row 53 */
    fun eventClickRemoveProfileRecommendation(
        data: FeedTrackerDataModel
    ) {
        sendGeneralTracker(
            eventAction = "click - x - profile recommendation",
            eventLabel = getEventLabel(data.type, data.entryPoint, data.authorType, data.authorId),
            trackerId = "45542"
        )
    }

    /** Row 54 */
    fun eventSwipeProfileRecommendation(
        type: String,
        entryPoint: String,
    ) {
        sendGeneralTracker(
            eventAction = "scroll - right left follow recommendation cards",
            eventLabel = getEventLabel(type, entryPoint),
            trackerId = "45602"
        )
    }

    private fun getEventLabel(
        prefix: String,
        entryPoint: String,
    ) = "$prefix - $entryPoint"

    private fun getEventLabel(
        prefix: String,
        entryPoint: String,
        authorType: AuthorType,
        authorId: String
    ): String {
        val authorTypeValue = when (authorType) {
            AuthorType.User -> Value.user
            AuthorType.Shop -> Value.shop
            else -> ""
        }

        return "${getEventLabel(prefix, entryPoint)} - $authorTypeValue - $authorId"
    }

    private fun sendGeneralTracker(
        eventAction: String,
        eventLabel: String,
        trackerId: String,
    ) {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction(eventAction)
            .setEventLabel(eventLabel)
            .setEventCategory(EventCategory.unifiedFeed)
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .setUserId(userSession.userId)
            .setCustomProperty(Key.sessionIris, TrackApp.getInstance().gtm.irisSessionId)
            .setCustomProperty(Key.trackerId, trackerId)
            .build()
            .send()
    }
}
