package com.tokopedia.notifications.inApp.viewEngine;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.tokopedia.notifications.inApp.CMInAppManager;

/**
 * @author lalit.singh
 */
public class CMActivityLifeCycle implements Application.ActivityLifecycleCallbacks {

    static String TAG = CMActivityLifeCycle.class.getSimpleName();

    CMInAppManager cmInAppManager;


    public CMActivityLifeCycle(CMInAppManager cmInAppManager){
        this.cmInAppManager = cmInAppManager;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        try {
            cmInAppManager.onActivityStartedInternal(activity);
        }catch (Exception e){}
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
        try {
            cmInAppManager.onActivityStopInternal(activity);
        }catch (Exception e){}
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }



    @Override
    public void onActivityDestroyed(Activity activity) {

    }


}
