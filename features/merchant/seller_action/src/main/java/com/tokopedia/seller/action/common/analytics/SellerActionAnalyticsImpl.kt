package com.tokopedia.seller.action.common.analytics

import com.tokopedia.seller.action.common.presentation.model.SellerActionStatus
import com.tokopedia.seller.action.common.utils.SellerActionUtils.isOrderDateToday
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class SellerActionAnalyticsImpl @Inject constructor(private val userSession: UserSessionInterface): SellerActionAnalytics {

    override fun sendSellerActionImpression(status: SellerActionStatus, date: String?) {

        val eventAction =
                when(status) {
                    is SellerActionStatus.Success -> {
                        if (status.itemList.isNullOrEmpty()) {
                            SellerActionTracking.EventAction.Impression.EMPTY_STATE
                        } else {
                            if (date == null || date.isOrderDateToday()) {
                                SellerActionTracking.EventAction.Impression.SUCCESS_TODAY_STATE
                            } else {
                                SellerActionTracking.EventAction.Impression.SUCCESS_CERTAIN_STATE
                            }
                        }
                    }
                    is SellerActionStatus.Fail -> SellerActionTracking.EventAction.Impression.ERROR_STATE
                    is SellerActionStatus.NotLogin -> SellerActionTracking.EventAction.Impression.NON_LOGIN_STATE
                    is SellerActionStatus.Loading -> SellerActionTracking.EventAction.Impression.LOADING_STATE
                }
        sendGeneralTracking(SellerActionTracking.EventName.VIEW, eventAction)
    }

    override fun clickOrderAppButton() {
        val eventAction = SellerActionTracking.EventAction.Click.OPEN_APP_BUTTON
        sendGeneralTracking(SellerActionTracking.EventName.CLICK, eventAction)
    }

    override fun clickOrderLine() {
        val eventAction = SellerActionTracking.EventAction.Click.ORDER_LINE
        sendGeneralTracking(SellerActionTracking.EventName.CLICK, eventAction)
    }

    private fun sendGeneralTracking(eventName: String, eventAction: String) {
        val trackingMap = mapOf(
                SellerActionTracking.Key.EVENT to eventName,
                SellerActionTracking.Key.EVENT_CATEGORY to SellerActionTracking.EVENT_CATEGORY,
                SellerActionTracking.Key.EVENT_ACTION to eventAction,
                SellerActionTracking.Key.EVENT_LABEL to "",
                SellerActionTracking.Key.BUSINESS_UNIT to SellerActionTracking.PHYSICAL_GOODS,
                SellerActionTracking.Key.CURRENT_SITE to SellerActionTracking.TOKOPEDIA_SELLER,
                SellerActionTracking.Key.USER_ID to userSession.userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(trackingMap)
    }
}