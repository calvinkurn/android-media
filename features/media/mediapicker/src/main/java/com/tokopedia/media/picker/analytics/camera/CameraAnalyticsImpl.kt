@file:Suppress("SameParameterValue")

package com.tokopedia.media.picker.analytics.camera

import com.tokopedia.media.picker.analytics.*
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class CameraAnalyticsImpl @Inject constructor(
    private val userSession: UserSessionInterface
) : CameraAnalytics {

    private val userId: String
        get() = userSession.userId

    private val shopId: String
        get() = userSession.shopId?: ""

    override fun visitCameraPage(
        entryPoint: String,
        pagePath: String,
        pageType: String
    ) {
        sendGeneralEvent(
            event = EVENT_CLICK_COMMUNICATION,
            eventAction = ACTION_VISIT_CAMERA,
            eventCategory = CATEGORY_MEDIA_CAMERA,
            eventLabel = "$entryPoint - $userId - $shopId",
            additionalEvent = mapOf(
                KEY_PAGE_PATH to "",
                KEY_PAGE_TYPE to ""
            )
        )
    }

    override fun clickRecord(entryPoint: String) {
        sendGeneralEvent(
            event = EVENT_CLICK_COMMUNICATION,
            eventAction = ACTION_CLICK_RECORD,
            eventCategory = CATEGORY_MEDIA_CAMERA,
            eventLabel = "$entryPoint - $userId - $shopId"
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