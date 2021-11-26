package com.tokopedia.notifications.inApp.external

import android.app.Activity
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp

interface IExternalInAppCallback {


    /**
     * this function is called when InApp pop view attached to screen
     *
     * */
    fun onInAppViewShown(activity: Activity)


    /**
     * This function is called when inApp data source consumed just after InApp is shown to user
     * update cmInApp state to database and fire INApp_RECEIVED event to IRIS
     * */
    fun onInAppDataConsumed(cmInApp: CMInApp)



    /**
     * this function is called if InApp view creation failed
     *
     * */
    fun onCMInAppInflateException(cmInApp: CMInApp)

    /**
     *
     * this function is called when user interacted with InApp pop view
     * update cmInApp state to database and fire INApp_RECEIVED event to IRIS
     *
     * */
    fun onUserInteractedWithInAppView(cmInApp: CMInApp)
    fun onUserInteractedWithInAppView(cmInAppId : Long)



    /**
     *  this function will called when user or system dismiss external dialog (other than
     *  CM InApp view) and this external dialog have higher priority than CM InApp.
     *  this function will make available current activity for CM InApp by removing blocked
     *  activity map.
     *
     * */
    fun onInAppViewDismiss(activity: Activity)

    /**
     *  this function will called when user or system dismiss the external InAPP pop view
     *  this function will make available current activity for CM InApp by removing blocked
     *  activity map.
     * */
    fun onInAppViewDismiss(cmInApp: CMInApp)

    /**
     * This function will return `True` if we have inApp
     * already displaying otherwise return 'False'
     *
     * */
    fun isInAppViewVisible(activity: Activity): Boolean
}