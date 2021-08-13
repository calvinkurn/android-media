package com.tokopedia.internal_review.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created By @ilhamsuaib on 02/02/21
 */

//data layer : https://mynakama.tokopedia.com/datatracker/requestdetail/350

class CustomerReviewTracking constructor(private val userSession: UserSessionInterface) : ReviewTracking {

    override fun sendClickDismissBottomSheetEvent(pageName: String) {
        val map = createMap(CustomerTrackingConstant.Event.CLICK_CUSTOMER_REVIEW, CustomerTrackingConstant.Action.CLICK_CLOSE_POP_UP, pageName)
        sendEvent(map)
    }

    override fun sendImpressionNoNetworkEvent(pageName: String) {
        val map = createMap(CustomerTrackingConstant.Event.VIEW_CUSTOMER_REVIEW_IRIS, CustomerTrackingConstant.Action.IMPRESSION_NO_NETWORK, pageName)
        sendEvent(map)
    }

    override fun sendImpressionErrorStateEvent(pageName: String) {
        val map = createMap(CustomerTrackingConstant.Event.VIEW_CUSTOMER_REVIEW_IRIS, CustomerTrackingConstant.Action.IMPRESSION_ERROR_STATE, pageName)
        sendEvent(map)
    }

    override fun sendImpressionLoadingStateEvent(pageName: String) {
        val map = createMap(CustomerTrackingConstant.Event.VIEW_CUSTOMER_REVIEW_IRIS, CustomerTrackingConstant.Action.IMPRESSION_LOADING_STATE, pageName)
        sendEvent(map)
    }

    private fun sendEvent(eventMap: Map<String, Any>) {
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    private fun createMap(event: String, action: String, label: String): MutableMap<String, Any> {
        val eventMap = TrackAppUtils.gtmData(event, CustomerTrackingConstant.Category.CUSTOMER_APP_REVIEW, action, label)
        eventMap[CustomerTrackingConstant.Key.BUSINESS_UNIT] = CustomerTrackingConstant.Value.PHYSICAL_GOODS
        eventMap[CustomerTrackingConstant.Key.CURRENT_SITE] = CustomerTrackingConstant.Value.TOKOPEDIACUSTOMER
        eventMap[CustomerTrackingConstant.Key.USER_ID] = userSession.userId
        return eventMap
    }
}