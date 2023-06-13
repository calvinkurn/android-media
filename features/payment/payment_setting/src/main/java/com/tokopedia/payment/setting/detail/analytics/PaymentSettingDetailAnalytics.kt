package com.tokopedia.payment.setting.detail.analytics

import com.tokopedia.payment.setting.detail.analytics.PaymentSettingDetailAnalyticsConstant.Action
import com.tokopedia.payment.setting.detail.analytics.PaymentSettingDetailAnalyticsConstant.Category
import com.tokopedia.payment.setting.detail.analytics.PaymentSettingDetailAnalyticsConstant.Event
import com.tokopedia.payment.setting.detail.analytics.PaymentSettingDetailAnalyticsConstant.Key
import com.tokopedia.payment.setting.detail.analytics.PaymentSettingDetailAnalyticsConstant.Value
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class PaymentSettingDetailAnalytics @Inject constructor(val userSessionInterface: UserSessionInterface) {

    private fun sendGeneralEvent(gtmData: MutableMap<String, Any>) {
        gtmData[Key.BUSINESS_UNIT] = Value.PAYMENT
        gtmData[Key.CURRENT_SITE] = Value.TOKOPEDIAMARKETPLACE
        gtmData[Key.USER_ID] = userSessionInterface.userId

        TrackApp.getInstance().gtm.sendGeneralEvent(gtmData)
    }

    fun sendEventViewCardDetail() {
        val map = TrackAppUtils.gtmData(
            Event.EVENT_VIEW_PAYMENT_IRIS,
            Category.PAYMENT_SETTING_PAGE,
            Action.VIEW_CARD_DETAIL,
            ""
        )
        map[Key.TRACKER_ID] = Value.TRACKER_ID_42698
        sendGeneralEvent(map)
    }

    fun sendEventClickDeleteCard() {
        val map = TrackAppUtils.gtmData(
            Event.EVENT_CLICK_PAYMENT,
            Category.PAYMENT_SETTING_PAGE,
            Action.CLICK_DELETE_CARD,
            ""
        )
        map[Key.TRACKER_ID] = Value.TRACKER_ID_42699
        sendGeneralEvent(map)
    }
}
