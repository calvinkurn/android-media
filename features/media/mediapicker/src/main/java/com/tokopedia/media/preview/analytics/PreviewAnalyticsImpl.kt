package com.tokopedia.media.preview.analytics

import com.tokopedia.kotlin.extensions.view.toZeroStringIfNullOrBlank
import com.tokopedia.picker.common.cache.PickerCacheManager
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class PreviewAnalyticsImpl @Inject constructor(
    private val userSession: UserSessionInterface,
    private val cacheManager: PickerCacheManager
) : PreviewAnalytics {

    private val userId: String
        get() = userSession.userId.toZeroStringIfNullOrBlank()

    private val shopId: String
        get() = userSession.shopId.toZeroStringIfNullOrBlank()

    private val sourcePage by lazy {
        cacheManager.get().pageSourceName()
    }

    override fun clickNextButton(buttonState: String) {
        sendGeneralEvent(
            event = EVENT_CLICK_COMMUNICATION,
            eventAction = ACTION_CLICK_NEXT,
            eventCategory = CATEGORY_MEDIA_PREVIEW,
            eventLabel = "$buttonState - $sourcePage - $userId - $shopId"
        )
    }

    override fun clickBackButton() {
        sendGeneralEvent(
            event = EVENT_CLICK_COMMUNICATION,
            eventAction = ACTION_CLICK_BACK,
            eventCategory = CATEGORY_MEDIA_PREVIEW,
            eventLabel = "$sourcePage - $userId - $shopId"
        )
    }

    override fun clickRetakeButton(retakeState: String) {
        sendGeneralEvent(
            event = EVENT_CLICK_COMMUNICATION,
            eventAction = ACTION_CLICK_ULANG,
            eventCategory = CATEGORY_MEDIA_PREVIEW,
            eventLabel = "$retakeState - $sourcePage - $userId - $shopId"
        )
    }

    override fun clickDrawerThumbnail() {
        sendGeneralEvent(
            event = EVENT_CLICK_COMMUNICATION,
            eventAction = ACTION_CLICK_THUMBNAIL,
            eventCategory = CATEGORY_MEDIA_PREVIEW,
            eventLabel = "$sourcePage - $userId - $shopId"
        )
    }

    private fun sendGeneralEvent(
        event: String,
        eventCategory: String,
        eventAction: String,
        eventLabel: String,
        additionalEvent: Map<String, String> = mapOf()
    ) {
        val generalEvent = mutableMapOf(
            KEY_EVENT to event,
            KEY_EVENT_CATEGORY to eventCategory,
            KEY_EVENT_ACTION to eventAction,
            KEY_EVENT_LABEL to eventLabel,
            KEY_BUSINESS_UNIT to BUSINESS_UNIT,
            KEY_CURRENT_SITE to CURRENT_SITE,
            KEY_USER_ID to userId,
        )

        if (additionalEvent.isNotEmpty()) {
            generalEvent.putAll(additionalEvent)
        }

        TrackApp.getInstance().gtm.sendGeneralEvent(
            generalEvent.toMap()
        )
    }
}
