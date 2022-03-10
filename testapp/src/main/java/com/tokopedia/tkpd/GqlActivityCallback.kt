package com.tokopedia.tkpd

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.tokopedia.graphql.data.GraphqlClient

/**
 * Created by devarafikry on 09/03/2022
 */
@SuppressLint("NewApi")
class GqlActivityCallback : Application.ActivityLifecycleCallbacks {
    companion object {
        private const val DEFAULT_MODULE_NAME = "tkpd"
    }
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        updateModuleName(activity)
    }
    override fun onActivityStarted(activity: Activity) {
        //no op
    }
    override fun onActivityResumed(activity: Activity) {
        updateModuleName(activity)
    }

    private fun updateModuleName(activity: Activity) {
        val packageName = activity.javaClass.`package`?.name
        if (packageName != null) {
            val splittedPackageName = packageName.split(".")
            if (splittedPackageName.size >= 3) {
                GraphqlClient.moduleName = splittedPackageName[2]
            } else {
                GraphqlClient.moduleName = DEFAULT_MODULE_NAME
            }
        } else {
            GraphqlClient.moduleName = DEFAULT_MODULE_NAME
        }
    }

    override fun onActivityPaused(activity: Activity) {
        //no op
    }

    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}

}