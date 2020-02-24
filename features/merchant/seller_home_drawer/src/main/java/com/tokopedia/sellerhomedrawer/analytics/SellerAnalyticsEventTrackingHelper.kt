package com.tokopedia.sellerhomedrawer.analytics

import android.content.Context
import com.tokopedia.analytics.TrackAnalytics
import com.tokopedia.analytics.firebase.FirebaseEvent
import com.tokopedia.analytics.firebase.FirebaseParams
import java.util.*

class SellerAnalyticsEventTrackingHelper {

    companion object {
        @JvmStatic
        fun hamburgerIconClickCategory(context: Context, landingScreen: String, optionName: String) {
            val map = HashMap<String, Any>()
            map[FirebaseParams.Home.LANDING_SCREEN_NAME] = landingScreen
            map[FirebaseParams.Home.OPTION_NAME] = landingScreen

            sendEventToAnalytics(context, FirebaseEvent.Home.HAMBURGER_OPTION_CLICK, map)
        }

        @JvmStatic
        fun hamburgerOptionClicked(context: Context, landingScreen: String, optionName: String) {
            val map = HashMap<String, Any>()
            map[FirebaseParams.Home.LANDING_SCREEN_NAME] = landingScreen
            map[FirebaseParams.Home.OPTION_NAME] = optionName

            sendEventToAnalytics(context, FirebaseEvent.Home.HAMBURGER_OPTION_CLICK, map)
        }

        @JvmStatic
        fun hamburgerOptionClicked(context: Context, landingScreen: String, optionName: String, subCategory: String) {
            val map = HashMap<String, Any>()
            map[FirebaseParams.Home.LANDING_SCREEN_NAME] = landingScreen
            map[FirebaseParams.Home.OPTION_NAME] = optionName
            map[FirebaseParams.Home.SUBCATEGORY_NAME] = subCategory

            sendEventToAnalytics(context, FirebaseEvent.Home.HAMBURGER_OPTION_CLICK, map)
        }

        @JvmStatic
        fun hamburgerIconClickLogin(context: Context, landingScreen: String) {
            val map = HashMap<String, Any>()
            map[FirebaseParams.Home.LANDING_SCREEN_NAME] = landingScreen
            sendEventToAnalytics(context, FirebaseEvent.Home.HAMBURGER_ICON_CLICK_LOGIN, map)
        }

        @JvmStatic
        fun hamburgerIconClickSignup(context: Context, landingScreen: String) {
            val map = HashMap<String, Any>()
            map[FirebaseParams.Home.LANDING_SCREEN_NAME] = landingScreen
            sendEventToAnalytics(context, FirebaseEvent.Home.HAMBURGER_ICON_CLICK_SIGNUP, map)
        }

        @JvmStatic
        fun hamburgerTokocashActivateClick(context: Context) {
            val map = HashMap<String, Any>()
            sendEventToAnalytics(context, FirebaseEvent.Home.HAMBURGER_TOKOCASH_ACTIVATE, map)
        }

        @JvmStatic
        fun homepageTokocashClick(context: Context, landingScreen: String) {
            val map = HashMap<String, Any>()
            map[FirebaseParams.Home.LANDING_SCREEN_NAME] = landingScreen
            sendEventToAnalytics(context, FirebaseEvent.Home.HAMBURGER_TOKOCASH, map)
        }

        @JvmStatic
        fun hamburgerTokopointsClick(context: Context, landingScreen: String) {
            val map = HashMap<String, Any>()
            map[FirebaseParams.Home.LANDING_SCREEN_NAME] = landingScreen
            sendEventToAnalytics(context, FirebaseEvent.Home.HAMBURGER_TOKOPOINTS, map)
        }

        @JvmStatic
        fun homepageSaldoClick(context: Context, landingScreen: String) {
            val map = HashMap<String, Any>()
            map[FirebaseParams.Home.LANDING_SCREEN_NAME] = landingScreen
            sendEventToAnalytics(context, FirebaseEvent.Home.HAMBURGER_SALDO, map)
        }

        @JvmStatic
        fun hambugerProfileClick(context: Context, landingScreen: String) {
            val map = HashMap<String, Any>()
            map[FirebaseParams.Home.LANDING_SCREEN_NAME] = landingScreen
            sendEventToAnalytics(context, FirebaseEvent.Home.HAMBURGER_PROFILE, map)
        }

        @JvmStatic
        fun hamburgerTokoCardClick(context: Context, landingScreen: String) {
            val map = HashMap<String, Any>()
            map[FirebaseParams.Home.LANDING_SCREEN_NAME] = landingScreen
            sendEventToAnalytics(context, FirebaseEvent.Home.HAMBURGER_TOKOCARD, map)
        }

        fun sendEventToAnalytics(context: Context, eventName: String, data: Map<String, Any>) {
            TrackAnalytics.sendEvent(eventName, data, context)
        }
    }


}