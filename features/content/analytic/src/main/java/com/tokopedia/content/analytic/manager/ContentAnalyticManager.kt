package com.tokopedia.content.analytic.manager

import android.os.Bundle
import com.tokopedia.config.GlobalConfig
import com.tokopedia.content.analytic.CurrentSite
import com.tokopedia.content.analytic.Event
import com.tokopedia.content.analytic.Key
import com.tokopedia.content.analytic.helper.ContentAnalyticHelper
import com.tokopedia.content.analytic.impression.OneTimeImpressionManager
import com.tokopedia.content.analytic.model.ContentAnalyticAuthor
import com.tokopedia.content.analytic.model.ContentEnhanceEcommerce
import com.tokopedia.content.analytic.model.isEEProduct
import com.tokopedia.content.analytic.model.isEEPromotion
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * Created By : Jonathan Darwin on January 25, 2024
 */
class ContentAnalyticManager @AssistedInject constructor(
    private val userSession: UserSessionInterface,
    @Assisted("businessUnit") private val businessUnit: String,
    @Assisted("eventCategory") private val eventCategory: String,
) {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("businessUnit") businessUnit: String,
            @Assisted("eventCategory") eventCategory: String,
        ): ContentAnalyticManager
    }

    private val currentSite: String
        get() = if (GlobalConfig.isSellerApp()) {
            CurrentSite.tokopediaSeller
        } else {
            CurrentSite.tokopediaMarketplace
        }

    private val sessionIris: String
        get() = TrackApp.getInstance().gtm.irisSessionId

    private val impressionManager = OneTimeImpressionManager<Any>()

    fun sendViewContent(
        eventAction: String,
        eventLabel: String,
        mainAppTrackerId: String,
        sellerAppTrackerId: String = "",
        customFields: Map<String, String> = emptyMap(),
    ) {
        sendEvent(
            event = Event.viewContentIris,
            eventAction = eventAction,
            eventLabel = eventLabel,
            mainAppTrackerId = mainAppTrackerId,
            sellerAppTrackerId = sellerAppTrackerId,
            customFields = customFields,
        )
    }

    fun sendClickContent(
        eventAction: String,
        eventLabel: String,
        mainAppTrackerId: String,
        sellerAppTrackerId: String = "",
        customFields: Map<String, String> = emptyMap(),
    ) {
        sendEvent(
            event = Event.clickContent,
            eventAction = eventAction,
            eventLabel = eventLabel,
            mainAppTrackerId = mainAppTrackerId,
            sellerAppTrackerId = sellerAppTrackerId,
            customFields = customFields,
        )
    }

    fun sendOpenScreen(
        screenName: String,
        mainAppTrackerId: String,
        sellerAppTrackerId: String = "",
        customFields: Map<String, String> = emptyMap(),
    ) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(
            screenName,
            mapOf(
                Key.trackerId to getTrackerId(mainAppTrackerId, sellerAppTrackerId),
                Key.businessUnit to businessUnit,
                Key.currentSite to currentSite,
                Key.sessionIris to sessionIris,
            ) + customFields
        )
    }

    fun sendEEPromotions(
        event: String,
        eventAction: String,
        eventLabel: String,
        promotions: List<ContentEnhanceEcommerce.Promotion>,
        mainAppTrackerId: String,
        sellerAppTrackerId: String = "",
        customFields: Map<String, String> = emptyMap(),
    ) {
        sendEE(
            event = event,
            eventAction = eventAction,
            eventLabel = eventLabel,
            contentEEList = promotions,
            mainAppTrackerId = mainAppTrackerId,
            sellerAppTrackerId = sellerAppTrackerId,
            customFields = customFields,
        )
    }

    fun sendEEProduct(
        event: String,
        eventAction: String,
        eventLabel: String,
        itemList: String,
        products: List<ContentEnhanceEcommerce.Product>,
        mainAppTrackerId: String,
        sellerAppTrackerId: String = "",
        customFields: Map<String, String> = emptyMap(),
    ) {
        sendEE(
            event = event,
            eventAction = eventAction,
            eventLabel = eventLabel,
            contentEEList = products,
            mainAppTrackerId = mainAppTrackerId,
            sellerAppTrackerId = sellerAppTrackerId,
            customFields = customFields.toMutableMap().apply {
                put(Key.itemList, itemList)
            },
        )
    }

    fun sendEvent(
        event: String,
        eventAction: String,
        eventLabel: String,
        mainAppTrackerId: String,
        sellerAppTrackerId: String = "",
        customFields: Map<String, String> = emptyMap(),
    ) {
        sendGeneral(
            Tracker.Builder()
                .setEvent(event)
                .setEventAction(eventAction)
                .setEventLabel(eventLabel)
                .setCustomProperty(
                    Key.trackerId,
                    getTrackerId(mainAppTrackerId, sellerAppTrackerId)
                )
                .setEventCategory(eventCategory)
                .setBusinessUnit(businessUnit)
                .apply {
                    customFields.forEach {
                        setCustomProperty(it.key, it.value)
                    }
                }
        )
    }

    private fun sendGeneral(
        trackerBuilder: Tracker.Builder,
    ) {
        trackerBuilder
            .setCurrentSite(currentSite)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    private fun sendEE(
        event: String,
        eventAction: String,
        eventLabel: String,
        contentEEList: List<ContentEnhanceEcommerce>,
        mainAppTrackerId: String,
        sellerAppTrackerId: String = "",
        customFields: Map<String, String> = emptyMap(),
    ) {
        val trackerBundle = Bundle().apply {
            putString(Key.event, event)
            putString(Key.eventCategory, eventCategory)
            putString(Key.eventAction, eventAction)
            putString(Key.eventLabel, eventLabel)

            when {
                contentEEList.isEEProduct() -> {
                    val products = contentEEList.filterIsInstance<ContentEnhanceEcommerce.Product>()

                    putParcelableArrayList(
                        Key.items,
                        ArrayList(
                            products.map { item ->
                                Bundle().apply {
                                    putString(Key.itemId, item.itemId)
                                    putString(Key.itemName, item.itemName)
                                    putString(Key.itemBrand, item.itemBrand)
                                    putString(Key.itemCategory, item.itemCategory)
                                    putString(Key.itemVariant, item.itemVariant)
                                    putString(Key.price, item.price)
                                    putString(Key.itemIndex, item.index)
                                    item.customFields.entries.forEach {
                                        putString(it.key, it.value)
                                    }
                                }
                            }
                        )
                    )
                }
                contentEEList.isEEPromotion() -> {
                    val promotions = contentEEList.filterIsInstance<ContentEnhanceEcommerce.Promotion>()

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
                }
            }

            putString(Key.trackerId, getTrackerId(mainAppTrackerId, sellerAppTrackerId))
            putString(Key.userId, userSession.userId)
            putString(Key.businessUnit, businessUnit)
            putString(Key.currentSite, currentSite)
            customFields.entries.forEach {
                putString(it.key, it.value)
            }
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(event, trackerBundle)
    }

    fun impressOnlyOnce(key: Any, onImpress: () -> Unit) {
        impressionManager.impress(key, onImpress)
    }

    fun clearAllImpression() {
        impressionManager.reset()
    }

    fun clearImpression(key: Any) {
        impressionManager.clear(key)
    }

    fun concatLabels(
        vararg labels: String
    ): String {
        return ContentAnalyticHelper.concatLabels(*labels)
    }

    fun concatLabelsWithAuthor(
        author: ContentAnalyticAuthor,
        vararg labels: String
    ): String {
        return ContentAnalyticHelper.concatLabelsWithAuthor(author, *labels)
    }

    private fun getTrackerId(
        mainAppTrackerId: String,
        sellerAppTrackerId: String,
    ): String {
        return if (GlobalConfig.isSellerApp()) sellerAppTrackerId else mainAppTrackerId
    }
}
