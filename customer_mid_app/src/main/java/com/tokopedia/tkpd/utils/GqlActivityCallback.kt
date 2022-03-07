package com.tokopedia.tkpd.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.tokopedia.graphql.data.GraphqlClient

/**
 * Created by Vishal Gupta on 17/10/2018
 */
@SuppressLint("NewApi")
class GqlActivityCallback constructor() : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityResumed(activity: Activity) {
        val packageName = activity.javaClass.`package`.name.split(".")
        if (packageName.size >= 3) {
            GraphqlClient.moduleName = packageName[2]
        }
    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}

}