package com.tokopedia.internal_review.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created By @ilhamsuaib on 02/02/21
 */

//data layer : https://mynakama.tokopedia.com/datatracker/requestdetail/350

class SellerReviewTracking constructor(private val userSession: UserSessionInterface) : ReviewTracking {

    override fun sendClickDismissBottomSheetEvent(pageName: String) {
        val map = createMap(SellerTrackingConstant.Event.CLICK_SELLER_REVIEW, SellerTrackingConstant.Action.CLICK_CLOSE_POP_UP, pageName)
        sendEvent(map)
    }

    override fun sendImpressionNoNetworkEvent(pageName: String) {
        val map = createMap(SellerTrackingConstant.Event.VIEW_SELLER_REVIEW_IRIS, SellerTrackingConstant.Action.IMPRESSION_NO_NETWORK, pageName)
        sendEvent(map)
    }

    override fun sendImpressionErrorStateEvent(pageName: String) {
        val map = createMap(SellerTrackingConstant.Event.VIEW_SELLER_REVIEW_IRIS, SellerTrackingConstant.Action.IMPRESSION_ERROR_STATE, pageName)
        sendEvent(map)
    }

    override fun sendImpressionLoadingStateEvent(pageName: String) {
        val map = createMap(SellerTrackingConstant.Event.VIEW_SELLER_REVIEW_IRIS, SellerTrackingConstant.Action.IMPRESSION_LOADING_STATE, pageName)
        sendEvent(map)
    }

    private fun sendEvent(eventMap: Map<String, Any>) {
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    private fun createMap(event: String, action: String, label: String): MutableMap<String, Any> {
        val eventMap = TrackAppUtils.gtmData(event, SellerTrackingConstant.Category.SELLER_APP_REVIEW, action, label)
        eventMap[SellerTrackingConstant.Key.BUSINESS_UNIT] = SellerTrackingConstant.Value.PHYSICAL_GOODS
        eventMap[SellerTrackingConstant.Key.CURRENT_SITE] = SellerTrackingConstant.Value.TOKOPEDIASELLER
        eventMap[SellerTrackingConstant.Key.USER_ID] = userSession.userId
        return eventMap
    }
}