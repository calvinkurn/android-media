package com.tokopedia.payment.setting.list.analytics

import com.tokopedia.payment.setting.list.analytics.PaymentSettingListAnalyticsConstant.Action
import com.tokopedia.payment.setting.list.analytics.PaymentSettingListAnalyticsConstant.Category
import com.tokopedia.payment.setting.list.analytics.PaymentSettingListAnalyticsConstant.Event
import com.tokopedia.payment.setting.list.analytics.PaymentSettingListAnalyticsConstant.Key
import com.tokopedia.payment.setting.list.analytics.PaymentSettingListAnalyticsConstant.Value
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class PaymentSettingListAnalytics @Inject constructor(val userSessionInterface: UserSessionInterface) {

    private fun sendGeneralEvent(gtmData: MutableMap<String, Any>) {
        gtmData[Key.BUSINESS_UNIT] = Value.PAYMENT
        gtmData[Key.CURRENT_SITE] = Value.TOKOPEDIAMARKETPLACE
        gtmData[Key.USER_ID] = userSessionInterface.userId

        TrackApp.getInstance().gtm.sendGeneralEvent(gtmData)
    }

    fun sendEventViewBannerUserNotYetHaveCobrand() {
        val map = TrackAppUtils.gtmData(
            Event.VIEW_ITEM,
            Category.CREDIT_CARD_SETTING_PAGE_COBRAND,
            Action.VIEW_BANNER_USER_NOT_YET_HAVE_COBRAND,
            ""
        )
        map[Key.TRACKER_ID] = Value.TRACKER_ID_41438
        sendGeneralEvent(map)
    }

    fun sendEventClickBannerUserNotYetHaveCobrand() {
        val map = TrackAppUtils.gtmData(
            Event.SELECT_CONTENT,
            Category.CREDIT_CARD_SETTING_PAGE_COBRAND,
            Action.CLICK_BANNER_USER_NOT_YET_HAVE_COBRAND,
            ""
        )
        map[Key.TRACKER_ID] = Value.TRACKER_ID_41439
        sendGeneralEvent(map)
    }

    fun sendEventViewBannerUserCobrandOnProgress() {
        val map = TrackAppUtils.gtmData(
            Event.VIEW_ITEM,
            Category.CREDIT_CARD_SETTING_PAGE_COBRAND,
            Action.VIEW_BANNER_USER_COBRAND_ON_PROGRESS,
            ""
        )
        map[Key.TRACKER_ID] = Value.TRACKER_ID_41440
        sendGeneralEvent(map)
    }

    fun sendEventClickBannerUserCobrandOnProgress() {
        val map = TrackAppUtils.gtmData(
            Event.SELECT_CONTENT,
            Category.CREDIT_CARD_SETTING_PAGE_COBRAND,
            Action.CLICK_BANNER_USER_COBRAND_ON_PROGRESS,
            ""
        )
        map[Key.TRACKER_ID] = Value.TRACKER_ID_41441
        sendGeneralEvent(map)
    }

    fun sendEventViewBannerUserCobrandActivate() {
        val map = TrackAppUtils.gtmData(
            Event.VIEW_ITEM,
            Category.CREDIT_CARD_SETTING_PAGE_COBRAND,
            Action.VIEW_BANNER_USER_COBRAND_ACTIVATE,
            ""
        )
        map[Key.TRACKER_ID] = Value.TRACKER_ID_41442
        sendGeneralEvent(map)
    }

    fun sendEventClickBannerUserCobrandActivate() {
        val map = TrackAppUtils.gtmData(
            Event.SELECT_CONTENT,
            Category.CREDIT_CARD_SETTING_PAGE_COBRAND,
            Action.CLICK_BANNER_USER_COBRAND_ACTIVATE,
            ""
        )
        map[Key.TRACKER_ID] = Value.TRACKER_ID_41443
        sendGeneralEvent(map)
    }

    fun sendEventViewBannerUserCobrandAdd() {
        val map = TrackAppUtils.gtmData(
            Event.VIEW_ITEM,
            Category.CREDIT_CARD_SETTING_PAGE_COBRAND,
            Action.CLICK_BANNER_USER_COBRAND_ADD,
            ""
        )
        map[Key.TRACKER_ID] = Value.TRACKER_ID_41444
        sendGeneralEvent(map)
    }
}
