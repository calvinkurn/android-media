package com.tokopedia.content.analytic.base

import android.os.Bundle
import com.tokopedia.config.GlobalConfig
import com.tokopedia.content.analytic.CurrentSite
import com.tokopedia.content.analytic.Event
import com.tokopedia.content.analytic.Key
import com.tokopedia.content.analytic.model.ContentEEPromotion
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created By : Jonathan Darwin on January 23, 2024
 */
abstract class BaseContentAnalytic(
    private val userSession: UserSessionInterface,
    private val businessUnit: String,
    private val eventCategory: String,
) {

    private val currentSite: String
        get() = if (GlobalConfig.isSellerApp()) {
            CurrentSite.tokopediaSeller
        } else {
            CurrentSite.tokopediaMarketplace
        }

    private val sessionIris: String
        get() = TrackApp.getInstance().gtm.irisSessionId

    protected fun sendEvent(
        event: String,
        eventLabel: String,
        eventAction: String,
        mainAppTrackerId: String,
        sellerAppTrackerId: String = "",
    ) {
        send(
            Tracker.Builder()
                .setEvent(event)
                .setEventLabel(eventLabel)
                .setEventAction(eventAction)
                .setCustomProperty(
                    Key.trackerId,
                    getTrackerId(mainAppTrackerId, sellerAppTrackerId)
                )
                .setEventCategory(eventCategory)
                .setBusinessUnit(businessUnit)
        )
    }

    protected fun sendViewContent(
        eventLabel: String,
        eventAction: String,
        mainAppTrackerId: String,
        sellerAppTrackerId: String = "",
    ) {
        send(
            Tracker.Builder()
                .setEvent(Event.viewContentIris)
                .setEventLabel(eventLabel)
                .setEventAction(eventAction)
                .setCustomProperty(
                    Key.trackerId,
                    getTrackerId(mainAppTrackerId, sellerAppTrackerId)
                )
                .setEventCategory(eventCategory)
                .setBusinessUnit(businessUnit)
        )
    }

    protected fun sendClickContent(
        eventLabel: String,
        eventAction: String,
        mainAppTrackerId: String,
        sellerAppTrackerId: String = "",
    ) {
        send(
            Tracker.Builder()
                .setEvent(Event.clickContent)
                .setEventLabel(eventLabel)
                .setEventAction(eventAction)
                .setCustomProperty(
                    Key.trackerId,
                    getTrackerId(mainAppTrackerId, sellerAppTrackerId)
                )
                .setEventCategory(eventCategory)
                .setBusinessUnit(businessUnit)
        )
    }

    protected fun sendOpenScreen(
        screenName: String,
        mainAppTrackerId: String,
        sellerAppTrackerId: String = "",
    ) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(
            screenName,
            mapOf(
                Key.trackerId to getTrackerId(mainAppTrackerId, sellerAppTrackerId),
                Key.businessUnit to businessUnit,
                Key.currentSite to currentSite,
                Key.sessionIris to sessionIris,
            )
        )
    }

    protected fun sendViewEEPromotions(
        eventAction: String,
        eventLabel: String,
        mainAppTrackerId: String,
        sellerAppTrackerId: String = "",
        promotions: List<ContentEEPromotion>,
    ) {
        sendEEPromotions(
            event = Event.viewItem,
            eventAction = eventAction,
            eventLabel = eventLabel,
            mainAppTrackerId = mainAppTrackerId,
            sellerAppTrackerId = sellerAppTrackerId,
            promotions = promotions,
        )
    }

    protected fun sendClickEEPromotions(
        eventAction: String,
        eventLabel: String,
        mainAppTrackerId: String,
        sellerAppTrackerId: String = "",
        promotions: List<ContentEEPromotion>,
    ) {
        sendEEPromotions(
            event = Event.selectContent,
            eventAction = eventAction,
            eventLabel = eventLabel,
            mainAppTrackerId = mainAppTrackerId,
            sellerAppTrackerId = sellerAppTrackerId,
            promotions = promotions,
        )
    }

    protected fun buildEventLabel(
        vararg labels: String
    ): String {
        return buildString {
            for (i in labels.indices) {
                append(labels[i])

                if (i != labels.size-1) {
                    append(" - ")
                }
            }
        }
    }

    private fun send(
        trackerBuilder: Tracker.Builder
    ) {
        trackerBuilder
            .setCurrentSite(currentSite)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    private fun sendEEPromotions(
        event: String,
        eventAction: String,
        eventLabel: String,
        mainAppTrackerId: String,
        sellerAppTrackerId: String = "",
        promotions: List<ContentEEPromotion>,
    ) {
        val trackerBundle = Bundle().apply {
            putString(Key.event, event)
            putString(Key.eventCategory, eventCategory)
            putString(Key.eventAction, eventAction)
            putString(Key.eventLabel, eventLabel)
            putParcelableArrayList(
                Key.promotions,
                ArrayList(
                    promotions.map { item ->
                        Bundle().apply {
                            putString(Key.itemId, item.itemId)
                            putString(Key.itemName, item.itemName)
                            putString(Key.creativeSlot, item.creativeSlot)
                            putString(Key.creativeName, item.creativeName)
                        }
                    }
                )
            )
            putString(Key.trackerId, getTrackerId(mainAppTrackerId, sellerAppTrackerId))
            putString(Key.userId, userSession.userId)
            putString(Key.businessUnit, businessUnit)
            putString(Key.currentSite, currentSite)
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(event, trackerBundle)
    }


    private fun getTrackerId(
        mainAppTrackerId: String,
        sellerAppTrackerId: String,
    ): String {
        return if (GlobalConfig.isSellerApp()) sellerAppTrackerId else mainAppTrackerId
    }
}
