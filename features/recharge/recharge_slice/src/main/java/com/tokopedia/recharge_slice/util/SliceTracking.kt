package com.tokopedia.recharge_slice.util

import com.tokopedia.user.session.UserSession
import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.recharge_slice.data.Recommendation
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics

class SliceTracking (private val userSession: UserSession){

    private object Event {
        val KEY = "event"
        val CATEGORY = "eventCategory"
        val ACTION = "eventAction"
        val LABEL = "eventLabel"
        val BUSINESS_UNIT = "businessUnit"
        val CURRENT_SITE = "currentSite"
        val USER_ID = "userId"
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

    private fun getTracker() : Analytics {
        return TrackApp.getInstance().gtm
    }

}