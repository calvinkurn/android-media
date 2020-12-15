package com.tokopedia.recharge_slice.util

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.recharge_slice.data.Recommendation
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics
import com.tokopedia.user.session.UserSessionInterface

class SliceTracking (private val userSession: UserSessionInterface){

    private object Event {
        val KEY = "event"
        val CATEGORY = "eventCategory"
        val ACTION = "eventAction"
        val LABEL = "eventLabel"
        val BUSINESS_UNIT = "businessUnit"
        val CURRENT_SITE = "currentSite"
        val USER_ID = "userId"
        val SCREEN_NAME = "screenName"
    }

    fun onImpressionSliceRecharge(recommendation : Recommendation?, date : String){
        getTracker().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                Event.KEY, "viewGAMainIris",
                Event.CATEGORY, "ga main app",
                Event.ACTION, "impression on success state - ${date}",
                Event.LABEL, "${recommendation?.categoryName} - ${recommendation?.operatorName} - ${recommendation?.productName}",
                Event.BUSINESS_UNIT, "recharge",
                Event.CURRENT_SITE, "tokopediadigital",
                Event.USER_ID, userSession.userId
        ))
    }

    fun onEmptyState(){
        getTracker().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                Event.KEY, "viewGAMainIris",
                Event.CATEGORY, "ga main app",
                Event.ACTION, "impression empty state",
                Event.LABEL, "",
                Event.BUSINESS_UNIT, "recharge",
                Event.CURRENT_SITE, "tokopediadigital",
                Event.USER_ID, userSession.userId
        ))
    }

    fun onNonLoginState(){
        getTracker().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                Event.KEY, "viewGAMainIris",
                Event.CATEGORY, "ga main app",
                Event.ACTION, "impression non login state",
                Event.LABEL, "",
                Event.BUSINESS_UNIT, "recharge",
                Event.CURRENT_SITE, "tokopediadigital",
                Event.USER_ID, userSession.userId
        ))
    }

    fun onErrorState(){
        getTracker().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                Event.KEY, "viewGAMainIris",
                Event.CATEGORY, "ga main app",
                Event.ACTION, "impression error state",
                Event.LABEL, "",
                Event.BUSINESS_UNIT, "recharge",
                Event.CURRENT_SITE, "tokopediadigital",
                Event.USER_ID, userSession.userId
        ))
    }

    fun onLoadingState(){
        getTracker().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                Event.KEY, "viewGAMainIris",
                Event.CATEGORY, "ga main app",
                Event.ACTION, "impression loading state",
                Event.LABEL, "",
                Event.BUSINESS_UNIT, "recharge",
                Event.CURRENT_SITE, "tokopediadigital",
                Event.USER_ID, userSession.userId
        ))
    }


    fun clickLoginPage(){
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(DataLayer.mapOf(
                Event.KEY, "clickGAMain",
                Event.CATEGORY, "ga main app",
                Event.ACTION, "click open app button",
                Event.LABEL, "",
                Event.BUSINESS_UNIT, "recharge",
                Event.CURRENT_SITE, "tokopediadigital",
                Event.USER_ID, ""
        ))
    }

    fun openLoginPage() {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(DataLayer.mapOf(
                Event.KEY, "openScreen",
                Event.SCREEN_NAME, "login homepage - from voice search - mainapp"
        ))
    }

    private fun getTracker() : Analytics {
        return TrackApp.getInstance().gtm
    }

}