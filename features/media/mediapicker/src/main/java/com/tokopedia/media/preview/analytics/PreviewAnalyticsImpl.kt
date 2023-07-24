package com.tokopedia.media.preview.analytics

import com.tokopedia.kotlin.extensions.view.toZeroStringIfNullOrBlank
import com.tokopedia.picker.common.PageSource
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

    override fun clickNextButton(buttonState: String, listImage: List<Triple<String,String, Int>>) {
        sendGeneralEvent(
            event = EVENT_CLICK_COMMUNICATION,
            eventAction = ACTION_CLICK_NEXT,
            eventCategory = CATEGORY_MEDIA_PREVIEW,
            eventLabel = "$buttonState - $sourcePage - $userId - $shopId"
        )

        // Temporary, used to gather data and will be removed later
        val buttonStateInt = if (buttonState == PREVIEW_PAGE_LANJUT) {
            CONTINUE_EDITOR_INDEX
        } else {
            UPLOAD_PICKER_INDEX
        }
        val pageSourceInt = PageSource.values().indexOf(cacheManager.get().pageSource())
        var imageListString = ""
        listImage.forEachIndexed { index, (imageSize, imageResolution, _) ->
            if (index >= DATA_SAMPLING_LIMIT) return@forEachIndexed
            imageListString+= " - $imageSize - $imageResolution"
        }

        sendGeneralEvent(
            event = EVENT_CLICK_COMMUNICATION,
            eventAction = ACTION_CLICK_UPLOAD,
            eventCategory = CATEGORY_IMAGE_UPLOAD,
            trackerId = TRACKER_ID_IMAGE_DETAIL,
            eventLabel = "$buttonStateInt - $pageSourceInt$imageListString - ${listImage.size}"
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
        trackerId: String = "",
        additionalEvent: Map<String, String> = mapOf()
    ) {
        val generalEvent = mutableMapOf(
            KEY_EVENT to event,
            KEY_EVENT_CATEGORY to eventCategory,
            KEY_EVENT_ACTION to eventAction,
            KEY_EVENT_LABEL to eventLabel,
            KEY_BUSINESS_UNIT to BUSINESS_UNIT,
            KEY_CURRENT_SITE to CURRENT_SITE,
            KEY_USER_ID to userId
        )

        if (trackerId.isNotEmpty()) {
            generalEvent[KEY_TRACKER_ID] = trackerId
        }

        if (additionalEvent.isNotEmpty()) {
            generalEvent.putAll(additionalEvent)
        }

        TrackApp.getInstance().gtm.sendGeneralEvent(
            generalEvent.toMap()
        )
    }

    companion object {
        const val DATA_SAMPLING_LIMIT = 4
        const val UPLOAD_PICKER_INDEX = 0
        const val CONTINUE_EDITOR_INDEX = 1
    }
}
