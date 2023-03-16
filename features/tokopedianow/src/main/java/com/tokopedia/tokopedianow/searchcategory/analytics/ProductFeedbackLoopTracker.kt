package com.tokopedia.tokopedianow.searchcategory.analytics

import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CURRENT_SITE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_BUSINESS_UNIT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.searchcategory.analytics.ProductFeedbackLoopTrackerConst.Category.TOKONOW_SEARCH_RESULT
import com.tokopedia.tokopedianow.searchcategory.analytics.ProductFeedbackLoopTrackerConst.Category.TOKONOW_NO_SEARCH_RESULT
import com.tokopedia.tokopedianow.searchcategory.analytics.ProductFeedbackLoopTrackerConst.Event
import com.tokopedia.tokopedianow.searchcategory.analytics.ProductFeedbackLoopTrackerConst.Action
import com.tokopedia.tokopedianow.searchcategory.analytics.ProductFeedbackLoopTrackerConst.TrackerId
import com.tokopedia.tokopedianow.searchcategory.analytics.ProductFeedbackLoopTrackerConst.Id.TRACKER_ID
import com.tokopedia.tokopedianow.searchcategory.analytics.ProductFeedbackLoopTrackerConst.Id.USER_ID
import com.tokopedia.tokopedianow.searchcategory.analytics.ProductFeedbackLoopTrackerConst.Id.WAREHOUSE_ID
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils.*


object ProductFeedbackLoopTracker {

    private val trackApp  = TrackApp.getInstance().gtm

    fun sendImpressionFeedbackLoop(userId:String,warehouseId:String,isSearchResult:Boolean){
        val dataMap:MutableMap<String,Any> = mutableMapOf(
            EVENT to Event.VIEW_GROCERIES,
            EVENT_ACTION to Action.VIEW_WIDGET_SRP,
            EVENT_CATEGORY to getEventCategory(isSearchResult),
            EVENT_LABEL to warehouseId,
            TRACKER_ID to getTrackerId(TrackerId.VIEW_WIDGET_SRP,isSearchResult),
            USER_ID to userId,
            WAREHOUSE_ID to warehouseId
        )
        fillCommonData(dataMap)
        trackApp.sendGeneralEvent(dataMap)
    }

    fun sendClickBackBtnFeedbackLoop(userId:String,warehouseId:String,isSearchResult:Boolean){
        val dataMap:MutableMap<String,Any> = mutableMapOf(
            EVENT to Event.CLICK_GROCERIES,
            EVENT_ACTION to Action.CLICK_BACK_BUTTON,
            EVENT_CATEGORY to getEventCategory(isSearchResult),
            EVENT_LABEL to warehouseId,
            TRACKER_ID to getTrackerId(TrackerId.CLICK_BACK_BUTTON,isSearchResult),
            USER_ID to userId,
            WAREHOUSE_ID to warehouseId
        )
        fillCommonData(dataMap)
        trackApp.sendGeneralEvent(dataMap)
    }

    fun sendImpressionFeedbackSheet(userId:String,warehouseId:String,isSearchResult:Boolean){
        val dataMap:MutableMap<String,Any> = mutableMapOf(
            EVENT to Event.VIEW_GROCERIES,
            EVENT_ACTION to Action.VIEW_FEEDBACK_SHEET,
            EVENT_CATEGORY to getEventCategory(isSearchResult),
            EVENT_LABEL to warehouseId,
            TRACKER_ID to getTrackerId(TrackerId.VIEW_FEEDBACK_SHEET,isSearchResult),
            USER_ID to userId,
            WAREHOUSE_ID to warehouseId
        )
        fillCommonData(dataMap)
        trackApp.sendGeneralEvent(dataMap)
    }

    fun sendClickSarankanCtaFeedbackLoop(userId:String,warehouseId:String,isSearchResult:Boolean){
        val dataMap:MutableMap<String,Any> = mutableMapOf(
            EVENT to Event.VIEW_GROCERIES,
            EVENT_ACTION to Action.CLICK_SAKARANG_CTA,
            EVENT_CATEGORY to getEventCategory(isSearchResult),
            EVENT_LABEL to warehouseId,
            TRACKER_ID to getTrackerId(TrackerId.CLICK_SAKARANG_CTA,isSearchResult),
            USER_ID to userId,
            WAREHOUSE_ID to warehouseId
        )
        fillCommonData(dataMap)
        trackApp.sendGeneralEvent(dataMap)
    }

    fun sendClickInputFeedbackSheet(userId:String,warehouseId:String,isSearchResult:Boolean){
        val dataMap:MutableMap<String,Any> = mutableMapOf(
            EVENT to Event.CLICK_GROCERIES,
            EVENT_ACTION to Action.CLICK_FEEDBACK_SHEET_TEXT_INPUT,
            EVENT_CATEGORY to getEventCategory(isSearchResult),
            EVENT_LABEL to warehouseId,
            TRACKER_ID to getTrackerId(TrackerId.CLICK_FEEDBACK_SHEET_TEXT_INPUT,isSearchResult),
            USER_ID to userId,
            WAREHOUSE_ID to warehouseId
        )
        fillCommonData(dataMap)
        trackApp.sendGeneralEvent(dataMap)
    }

    fun sendCloseFeedbackSheet(userId:String,warehouseId:String,isSearchResult:Boolean){
        val dataMap:MutableMap<String,Any> = mutableMapOf(
            EVENT to Event.CLICK_GROCERIES,
            EVENT_ACTION to Action.CLOSE_FEEDBACK_SHEET,
            EVENT_CATEGORY to getEventCategory(isSearchResult),
            EVENT_LABEL to warehouseId,
            TRACKER_ID to getTrackerId(TrackerId.CLOSE_FEEDBACK_SHEET,isSearchResult),
            USER_ID to userId,
            WAREHOUSE_ID to warehouseId
        )
        fillCommonData(dataMap)
        trackApp.sendGeneralEvent(dataMap)
    }

    fun sendClickCtaFeedbackSheet(userId:String,warehouseId:String,feedback:String,isSearchResult:Boolean){
        val dataMap:MutableMap<String,Any> = mutableMapOf(
            EVENT to Event.CLICK_GROCERIES,
            EVENT_ACTION to Action.CLICK_FEEDBACK_SHEET_CTA,
            EVENT_CATEGORY to getEventCategory(isSearchResult),
            EVENT_LABEL to "$warehouseId - $feedback",
            TRACKER_ID to getTrackerId(TrackerId.CLICK_FEEDBACK_SHEET_CTA,isSearchResult),
            USER_ID to userId,
            WAREHOUSE_ID to warehouseId
        )
        fillCommonData(dataMap)
        trackApp.sendGeneralEvent(dataMap)
    }

    fun sendImpressionSuccessToastFeedbackSheet(userId:String,warehouseId:String,feedback:String,isSearchResult:Boolean){
        val dataMap:MutableMap<String,Any> = mutableMapOf(
            EVENT to Event.VIEW_GROCERIES,
            EVENT_ACTION to Action.VIEW_SUCCESS_TOAST,
            EVENT_CATEGORY to getEventCategory(isSearchResult),
            EVENT_LABEL to "$warehouseId - $feedback",
            TRACKER_ID to getTrackerId(TrackerId.VIEW_SUCCESS_TOAST,isSearchResult),
            USER_ID to userId,
            WAREHOUSE_ID to warehouseId
        )
        fillCommonData(dataMap)
        trackApp.sendGeneralEvent(dataMap)
    }

    fun sendClickSuccessToastOkFeedbackSheet(userId:String,warehouseId:String,feedback:String,isSearchResult:Boolean){
        val dataMap:MutableMap<String,Any> = mutableMapOf(
            EVENT to Event.CLICK_GROCERIES,
            EVENT_ACTION to Action.CLICK_OK_SUCCESS_TOAST,
            EVENT_CATEGORY to getEventCategory(isSearchResult),
            EVENT_LABEL to "$warehouseId - $feedback",
            TRACKER_ID to getTrackerId(TrackerId.CLICK_OK_SUCCESS_TOAST,isSearchResult),
            USER_ID to userId,
            WAREHOUSE_ID to warehouseId
        )
        fillCommonData(dataMap)
        trackApp.sendGeneralEvent(dataMap)
    }

    fun sendImpressionFailureToastFeedbackSheet(userId:String,warehouseId:String,feedback:String,isSearchResult:Boolean){
        val dataMap:MutableMap<String,Any> = mutableMapOf(
            EVENT to Event.VIEW_GROCERIES,
            EVENT_ACTION to Action.VIEW_ERROR_TOAST,
            EVENT_CATEGORY to getEventCategory(isSearchResult),
            EVENT_LABEL to "$warehouseId - $feedback",
            TRACKER_ID to getTrackerId(TrackerId.VIEW_ERROR_TOAST,isSearchResult),
            USER_ID to userId,
            WAREHOUSE_ID to warehouseId
        )
        fillCommonData(dataMap)
        trackApp.sendGeneralEvent(dataMap)
    }

    fun sendClickFailureToastOkFeedbackSheet(userId:String,warehouseId:String,feedback:String,isSearchResult:Boolean){
        val dataMap:MutableMap<String,Any> = mutableMapOf(
            EVENT to Event.CLICK_GROCERIES,
            EVENT_ACTION to Action.CLICK_OK_ERROR_TOAST,
            EVENT_CATEGORY to getEventCategory(isSearchResult),
            EVENT_LABEL to "$warehouseId - $feedback",
            TRACKER_ID to getTrackerId(TrackerId.CLICK_OK_ERROR_TOAST,isSearchResult),
            USER_ID to userId,
            WAREHOUSE_ID to warehouseId
        )
        fillCommonData(dataMap)
        trackApp.sendGeneralEvent(dataMap)
    }

    private fun fillCommonData(dataMap:MutableMap<String,Any>){
        dataMap[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
        dataMap[KEY_CURRENT_SITE] = CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
    }

    private fun getEventCategory(isSearchResult: Boolean) : String {
       return if(isSearchResult)
                TOKONOW_SEARCH_RESULT
              else TOKONOW_NO_SEARCH_RESULT
    }

    private fun getTrackerId(idMap:Map<String,String>,isSearchResult: Boolean) : String{
       return idMap[if(isSearchResult)
           TOKONOW_SEARCH_RESULT
       else TOKONOW_NO_SEARCH_RESULT].orEmpty()
    }
}
