package com.tokopedia.payment.setting.authenticate.analytics

import com.tokopedia.payment.setting.authenticate.analytics.PaymentSettingAuthenticateAnalyticsConstant.Action
import com.tokopedia.payment.setting.authenticate.analytics.PaymentSettingAuthenticateAnalyticsConstant.Category
import com.tokopedia.payment.setting.authenticate.analytics.PaymentSettingAuthenticateAnalyticsConstant.Event
import com.tokopedia.payment.setting.authenticate.analytics.PaymentSettingAuthenticateAnalyticsConstant.Key
import com.tokopedia.payment.setting.authenticate.analytics.PaymentSettingAuthenticateAnalyticsConstant.Value
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class PaymentSettingAuthenticateAnalytics @Inject constructor(val userSessionInterface: UserSessionInterface) {
    private fun sendGeneralEvent(gtmData: MutableMap<String, Any>) {
        gtmData[Key.BUSINESS_UNIT] = Value.PAYMENT
        gtmData[Key.CURRENT_SITE] = Value.TOKOPEDIAMARKETPLACE
        gtmData[Key.USER_ID] = userSessionInterface.userId

        TrackApp.getInstance().gtm.sendGeneralEvent(gtmData)
    }

    fun sendEventViewVerificationMethod() {
        val map = TrackAppUtils.gtmData(
            Event.EVENT_VIEW_PAYMENT_IRIS,
            Category.PAYMENT_SETTING_PAGE,
            Action.VIEW_VERIFICATION_METHOD,
            ""
        )
        map[Key.TRACKER_ID] = Value.TRACKER_ID_42702
        sendGeneralEvent(map)
    }

    fun sendEventClickVerificationMethod() {
        val map = TrackAppUtils.gtmData(
            Event.EVENT_CLICK_PAYMENT,
            Category.PAYMENT_SETTING_PAGE,
            Action.CLICK_ON_VERIFICATION_METHOD,
            ""
        )
        map[Key.TRACKER_ID] = Value.TRACKER_ID_42703
        sendGeneralEvent(map)
    }

    fun sendEventClickSafeVerificationMethod() {
        val map = TrackAppUtils.gtmData(
            Event.EVENT_CLICK_PAYMENT,
            Category.PAYMENT_SETTING_PAGE,
            Action.CLICK_SAVE,
            ""
        )
        map[Key.TRACKER_ID] = Value.TRACKER_ID_42704
        sendGeneralEvent(map)
    }
}
