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
        parentId: Long,
        campaignId: Long,
        notificationId: Long,
        messageId: String
    ) {
        val map = TrackAppUtils.gtmData(
            CMHomeWidgetAnalyticsConstants.Event.WIDGET_RECEIVED,
            CMHomeWidgetAnalyticsConstants.Category.HOME_TO_DO_WIDGET,
            CMHomeWidgetAnalyticsConstants.Action.RECEIVED,
            getLabel(parentId, campaignId)
        )
        map[CMHomeWidgetAnalyticsConstants.Key.PARENT_ID] = parentId
        map[CMHomeWidgetAnalyticsConstants.Key.CAMPAIGN_ID] = campaignId
        map[CMHomeWidgetAnalyticsConstants.Key.NOTIFICATION_ID] = notificationId
        map[CMHomeWidgetAnalyticsConstants.Key.CAMPAIGN_CODE] = messageId
        map[CMHomeWidgetAnalyticsConstants.Key.MESSAGE_ID] = messageId
        sendGeneralEvent(map)
    }

    fun sendProductCardClickEvent(
        parentId: Long,
        campaignId: Long,
        notificationId: Long,
        messageId: String,
        productId: Long?,
        productName: String?,
        productPrice: String?,
        shopId: Long?,
        shopName: String?,
    ) {
        val map = TrackAppUtils.gtmData(
            CMHomeWidgetAnalyticsConstants.Event.WIDGET_CLICKED,
            CMHomeWidgetAnalyticsConstants.Category.HOME_TO_DO_WIDGET,
            CMHomeWidgetAnalyticsConstants.Action.CLICK_CTA,
            getLabel(parentId, campaignId)
        )
        map[CMHomeWidgetAnalyticsConstants.Key.PARENT_ID] = parentId
        map[CMHomeWidgetAnalyticsConstants.Key.CAMPAIGN_ID] = campaignId
        map[CMHomeWidgetAnalyticsConstants.Key.NOTIFICATION_ID] = notificationId
        map[CMHomeWidgetAnalyticsConstants.Key.CAMPAIGN_CODE] = messageId
        map[CMHomeWidgetAnalyticsConstants.Key.MESSAGE_ID] = messageId
        val itemsList = ArrayList<Map<String, Any>>()
        val itemMap = HashMap<String, Any>()
        itemMap[CMHomeWidgetAnalyticsConstants.Key.ITEM_ID] = productId.toString()
        itemMap[CMHomeWidgetAnalyticsConstants.Key.ITEM_NAME] = productName.toString()
        itemMap[CMHomeWidgetAnalyticsConstants.Key.PRICE] = productPrice.toString()
        itemMap[CMHomeWidgetAnalyticsConstants.Key.SHOP_ID] = shopId.toString()
        itemMap[CMHomeWidgetAnalyticsConstants.Key.SHOP_NAME] = shopName.toString()
        itemsList.add(itemMap)
        map[CMHomeWidgetAnalyticsConstants.Key.ECOMMERCE] = mapOf(CMHomeWidgetAnalyticsConstants.Key.ITEMS to itemsList)
        sendEnhancedEcommerceEvent(map)
    }

    fun sendViewAllCardClickEvent(
        parentId: Long,
        campaignId: Long,
        notificationId: Long,
        messageId: String
    ) {
        val map = TrackAppUtils.gtmData(
            CMHomeWidgetAnalyticsConstants.Event.WIDGET_CLICKED,
            CMHomeWidgetAnalyticsConstants.Category.HOME_TO_DO_WIDGET,
            CMHomeWidgetAnalyticsConstants.Action.CLICK_CTA,
            getLabel(parentId, campaignId)
        )
        map[CMHomeWidgetAnalyticsConstants.Key.PARENT_ID] = parentId
        map[CMHomeWidgetAnalyticsConstants.Key.CAMPAIGN_ID] = campaignId
        map[CMHomeWidgetAnalyticsConstants.Key.NOTIFICATION_ID] = notificationId
        map[CMHomeWidgetAnalyticsConstants.Key.CAMPAIGN_CODE] = messageId
        map[CMHomeWidgetAnalyticsConstants.Key.MESSAGE_ID] = messageId
        sendGeneralEvent(map)
    }

    private fun addCommonKeys(map: MutableMap<String, Any>) {
        map[CMHomeWidgetAnalyticsConstants.Key.BUSINESS_UNIT] =
            CMHomeWidgetAnalyticsConstants.BusinessUnit.VALUE_BUSINESS_UNIT
        map[CMHomeWidgetAnalyticsConstants.Key.CURRENT_SITE] =
            CMHomeWidgetAnalyticsConstants.CurrentSite.VALUE_CURRENT_SITE
        map[CMHomeWidgetAnalyticsConstants.Key.USER_ID] = userSession.get().userId
        // always false for now
        map[CMHomeWidgetAnalyticsConstants.Key.IS_SILENT] =
            CMHomeWidgetAnalyticsConstants.IsSilent.FALSE
    }

    private fun getLabel(
        parentID: Long,
        campaignID: Long
    ): String {
        return "$parentID - $campaignID"
    }

    private fun sendGeneralEvent(map: MutableMap<String, Any>) {
        addCommonKeys(map)
//        analyticTracker.sendGeneralEvent(map)
    }

    private fun sendEnhancedEcommerceEvent(map: MutableMap<String, Any>) {
        addCommonKeys(map)
//        analyticTracker.sendEnhanceEcommerceEvent(map)
    }

}