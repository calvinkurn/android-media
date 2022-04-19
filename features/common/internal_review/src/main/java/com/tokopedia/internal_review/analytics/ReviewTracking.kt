package com.tokopedia.internal_review.analytics

/**
 * Created By @ilhamsuaib on 02/02/21
 */

//data layer : https://mynakama.tokopedia.com/datatracker/requestdetail/350

interface ReviewTracking {

    fun sendClickDismissBottomSheetEvent(pageName: String)

    fun sendImpressionNoNetworkEvent(pageName: String)

    fun sendImpressionErrorStateEvent(pageName: String)

    fun sendImpressionLoadingStateEvent(pageName: String)
}