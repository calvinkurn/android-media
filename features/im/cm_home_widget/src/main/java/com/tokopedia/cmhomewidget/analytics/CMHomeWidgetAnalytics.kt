package com.tokopedia.cmhomewidget.analytics

import com.tokopedia.cmhomewidget.di.qualifier.CMHomeWidgetUserSession
import com.tokopedia.cmhomewidget.di.scope.CMHomeWidgetScope
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.ContextAnalytics
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

@CMHomeWidgetScope
class CMHomeWidgetAnalytics @Inject constructor(
    @CMHomeWidgetUserSession val userSession: dagger.Lazy<UserSessionInterface>
) {

    private val analyticTracker: ContextAnalytics
        get() = TrackApp.getInstance().gtm

    fun sendCMHomeWidgetReceivedEvent(
        parentId: String,
        campaignId: String,
        notificationId: String,
        messageId: String,
        sessionId : String?
    ) {
        kotlin.runCatching {
            val map = TrackAppUtils.gtmData(
                CMHomeWidgetAnalyticsConstants.Event.WIDGET_RECEIVED,
                CMHomeWidgetAnalyticsConstants.Category.HOME_TO_DO_WIDGET,
                CMHomeWidgetAnalyticsConstants.Action.RECEIVED,
                getLabel(parentId, campaignId)
            )
            addStaticCommonData(map)
            addDynamicCommonData(map, parentId, campaignId, notificationId, messageId, sessionId)
            sendGeneralEvent(map)
        }
    }

    fun sendCMHomeWidgetClickEvent(
        parentId: String,
        campaignId: String,
        notificationId: String,
        messageId: String,
        sessionId : String?
    ) {
        val map = TrackAppUtils.gtmData(
            CMHomeWidgetAnalyticsConstants.Event.WIDGET_CLICKED,
            CMHomeWidgetAnalyticsConstants.Category.HOME_TO_DO_WIDGET,
            CMHomeWidgetAnalyticsConstants.Action.CLICK_CTA,
            getLabel(parentId, campaignId)
        )
        addStaticCommonData(map)
        addDynamicCommonData(map, parentId, campaignId, notificationId, messageId, sessionId)
        sendGeneralEvent(map)
    }

    private fun getLabel(
        parentID: String,
        campaignID: String
    ): String {
        return "$parentID - $campaignID"
    }

    private fun addStaticCommonData(map: MutableMap<String, Any>) {
        map[CMHomeWidgetAnalyticsConstants.Key.BUSINESS_UNIT] =
            CMHomeWidgetAnalyticsConstants.BusinessUnit.VALUE_BUSINESS_UNIT
        map[CMHomeWidgetAnalyticsConstants.Key.CURRENT_SITE] =
            CMHomeWidgetAnalyticsConstants.CurrentSite.VALUE_CURRENT_SITE
        map[CMHomeWidgetAnalyticsConstants.Key.USER_ID] = userSession.get().userId
        // always false for now
        map[CMHomeWidgetAnalyticsConstants.Key.IS_SILENT] =
            CMHomeWidgetAnalyticsConstants.IsSilent.FALSE
    }

    private fun addDynamicCommonData(
        map: MutableMap<String, Any>, parentId: String,
        campaignId: String,
        notificationId: String,
        messageId: String,
        sessionId: String?
    ) {
        map[CMHomeWidgetAnalyticsConstants.Key.PARENT_ID] = parentId
        map[CMHomeWidgetAnalyticsConstants.Key.CAMPAIGN_ID] = campaignId
        map[CMHomeWidgetAnalyticsConstants.Key.NOTIFICATION_ID] = notificationId
        map[CMHomeWidgetAnalyticsConstants.Key.CAMPAIGN_CODE] = messageId
        map[CMHomeWidgetAnalyticsConstants.Key.MESSAGE_ID] = messageId
        sessionId?.let {
            map[CMHomeWidgetAnalyticsConstants.Key.SESSION_ID] =  sessionId
        }
    }

    private fun sendGeneralEvent(map: MutableMap<String, Any>) {
        analyticTracker.sendGeneralEvent(map)
    }
}