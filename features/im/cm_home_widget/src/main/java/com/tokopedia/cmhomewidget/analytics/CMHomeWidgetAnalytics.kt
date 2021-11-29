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

    fun sendCMWidgetReceivedEvent(
        parentID: Long,
        campaignID: Long,
        campaignCode: String
    ) {
        val map = TrackAppUtils.gtmData(
            CMHomeWidgetAnalyticsConstants.Event.WIDGET_RECEIVED,
            CMHomeWidgetAnalyticsConstants.Category.HOME_TO_DO_WIDGET,
            CMHomeWidgetAnalyticsConstants.Action.RECEIVED,
            getLabel(parentID, campaignID)
        )
        map[CMHomeWidgetAnalyticsConstants.Key.CAMPAIGN_CODE] = campaignCode
        sendGeneralEvent(map)
    }

    fun sendCMWidgetClickEvent(
        parentID: Long,
        campaignID: Long,
        campaignCode: String
    ) {
        val map = TrackAppUtils.gtmData(
            CMHomeWidgetAnalyticsConstants.Event.WIDGET_CLICKED,
            CMHomeWidgetAnalyticsConstants.Category.HOME_TO_DO_WIDGET,
            CMHomeWidgetAnalyticsConstants.Action.CLICK_CTA,
            getLabel(parentID, campaignID)
        )
        map[CMHomeWidgetAnalyticsConstants.Key.CAMPAIGN_CODE] = campaignCode
        sendGeneralEvent(map)
    }

    private fun sendGeneralEvent(map: MutableMap<String, Any>) {
        map[CMHomeWidgetAnalyticsConstants.Key.BUSINESS_UNIT] =
            CMHomeWidgetAnalyticsConstants.BusinessUnit.VALUE_BUSINESS_UNIT
        map[CMHomeWidgetAnalyticsConstants.Key.CURRENT_SITE] =
            CMHomeWidgetAnalyticsConstants.CurrentSite.VALUE_CURRENT_SITE
        map[CMHomeWidgetAnalyticsConstants.Key.USER_ID] = userSession.get().userId
//        analyticTracker.sendGeneralEvent(map)
    }

    private fun getLabel(
        parentID: Long,
        campaignID: Long
    ): String {
        return "${parentID - campaignID}"
    }

}