package com.tokopedia.stories.analytics

import com.tokopedia.content.analytic.CurrentSite
import com.tokopedia.content.analytic.EventCategory
import com.tokopedia.content.analytic.Key
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * @author by astidhiyaa on 05/09/23
 */
class StoriesSharingAnalyticsImpl @AssistedInject constructor(
    @Assisted val shopId: String,
    private val userSession: UserSessionInterface
) : StoriesSharingAnalytics {

    @AssistedFactory
    interface Factory : StoriesSharingAnalytics.Factory {
        override fun create(
            shopId: String,
        ): StoriesSharingAnalyticsImpl
    }


    private val role: String
        get() {
            val isOwner = userSession.shopId == shopId
            return if (isOwner) "creator" else "viewer"
        }

    private val userId: String
        get() = userSession.userId

    private val irisSession: String
        get() = TrackApp.getInstance().gtm.irisSessionId

    override fun onClickShareIcon(storyId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                Key.event to KEY_TRACK_CLICK_COMMUNICATION,
                Key.eventAction to "click - share button",
                Key.eventCategory to EventCategory.storiesPage,
                Key.eventLabel to "$role - $storyId - $shopId",
                Key.businessUnit to KEY_TRACK_BUSINESS_UNIT_SHARE_EXPERIENCE,
                Key.currentSite to CurrentSite.tokopediaMarketplace,
                Key.sessionIris to irisSession,
                Key.userId to userId,
                Key.shopId to shopId,
                Key.trackerId to "45641",
            )
        )
    }

    override fun onClickShareOptions(storyId: String, channel: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                Key.event to KEY_TRACK_CLICK_COMMUNICATION,
                Key.eventAction to "click - sharing channel",
                Key.eventCategory to EventCategory.storiesPage,
                Key.eventLabel to "$channel - $role - $storyId - $shopId",
                Key.businessUnit to KEY_TRACK_BUSINESS_UNIT_SHARE_EXPERIENCE,
                Key.currentSite to CurrentSite.tokopediaMarketplace,
                Key.sessionIris to irisSession,
                Key.userId to userId,
                Key.shopId to shopId,
                Key.trackerId to "45643",
            )
        )
    }

    override fun onImpressShareSheet(storyId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                Key.event to KEY_TRACK_VIEW_COMMUNICATION_IRIS,
                Key.eventAction to "view on sharing channel",
                Key.eventCategory to EventCategory.storiesPage,
                Key.eventLabel to "$role - $storyId - $shopId",
                Key.businessUnit to KEY_TRACK_BUSINESS_UNIT_SHARE_EXPERIENCE,
                Key.currentSite to CurrentSite.tokopediaMarketplace,
                Key.sessionIris to irisSession,
                Key.userId to userId,
                Key.shopId to shopId,
                Key.trackerId to "45644",
            )
        )
    }

    override fun onCloseShareSheet(storyId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                Key.event to KEY_TRACK_CLICK_COMMUNICATION,
                Key.eventAction to "click - close share bottom sheet",
                Key.eventCategory to EventCategory.storiesPage,
                Key.eventLabel to "$role - $storyId - $shopId",
                Key.businessUnit to KEY_TRACK_BUSINESS_UNIT_SHARE_EXPERIENCE,
                Key.currentSite to CurrentSite.tokopediaMarketplace,
                Key.sessionIris to irisSession,
                Key.userId to userId,
                Key.shopId to shopId,
                Key.trackerId to "45642",
            )
        )
    }

    companion object {
        internal const val KEY_TRACK_CLICK_COMMUNICATION = "clickCommunication"
        internal const val KEY_TRACK_VIEW_COMMUNICATION_IRIS = "viewCommunicationIris"
        internal const val KEY_TRACK_BUSINESS_UNIT_SHARE_EXPERIENCE = "sharingexperience"
    }
}
