package com.tokopedia.mvcwidget

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.tokopedia.mvcwidget.trackers.MvcTrackerImpl
import com.tokopedia.mvcwidget.views.activities.TransParentActivity

class MVCActivityCallbacks:Application.ActivityLifecycleCallbacks {
    var hashCodeForMVC:Int = -1
    lateinit var mvcTrackerImpl: MvcTrackerImpl

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

    }

    override fun onActivityStarted(activity: Activity) {
        if(activity is TransParentActivity && activity.mvcDataHashcode == hashCodeForMVC){
            if(::mvcTrackerImpl.isInitialized){
                activity.mvcTracker.trackerImpl = mvcTrackerImpl
            }
        }
    }

    override fun onActivityResumed(activity: Activity) {
        
    }

    override fun onActivityPaused(activity: Activity) {
        
    }

    override fun onActivityStopped(activity: Activity) {
        
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        
    }

    override fun onActivityDestroyed(activity: Activity) {
        
    }
}