package com.tokopedia.sellerreview.analytics

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.sellerhome.di.scope.SellerHomeScope
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 02/02/21
 */

@SellerHomeScope
class SellerReviewTracking @Inject constructor(@ApplicationContext context: Context) {

    private val userSession: UserSessionInterface by lazy {
        UserSession(context)
    }

    fun sendClickDismissBottomSheetEvent(pageName: String) {
        val map = createMap(TrackingConstant.Event.CLICK_SELLER_REVIEW, TrackingConstant.Action.CLICK_CLOSE_POP_UP, pageName)
        sendEvent(map)
    }

    fun sendImpressionNoNetworkEvent(pageName: String) {
        val map = createMap(TrackingConstant.Event.VIEW_SELLER_REVIEW_IRIS, TrackingConstant.Action.IMPRESSION_NO_NETWORK, pageName)
        sendEvent(map)
    }

    fun sendImpressionErrorStateEvent(pageName: String) {
        val map = createMap(TrackingConstant.Event.VIEW_SELLER_REVIEW_IRIS, TrackingConstant.Action.IMPRESSION_ERROR_STATE, pageName)
        sendEvent(map)
    }

    fun sendImpressionLoadingStateEvent(pageName: String) {
        val map = createMap(TrackingConstant.Event.VIEW_SELLER_REVIEW_IRIS, TrackingConstant.Action.IMPRESSION_LOADING_STATE, pageName)
        sendEvent(map)
    }

    private fun sendEvent(eventMap: Map<String, Any>) {
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    private fun createMap(event: String, action: String, label: String): MutableMap<String, Any> {
        val eventMap = TrackAppUtils.gtmData(event, TrackingConstant.Category.SELLER_APP_REVIEW, action, label)
        eventMap[TrackingConstant.Key.BUSINESS_UNIT] = TrackingConstant.Value.PHYSICAL_GOODS
        eventMap[TrackingConstant.Key.CURRENT_SITE] = TrackingConstant.Value.TOKOPEDIASELLER
        eventMap[TrackingConstant.Key.USER_ID] = userSession.userId
        return eventMap
    }
}