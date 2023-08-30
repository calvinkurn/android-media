package com.tokopedia.abstraction.newrelic

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.newrelic.agent.android.NewRelic
import com.tokopedia.user.session.UserSessionInterface

class NewRelicInteractionActCall(private val userSession: UserSessionInterface?) : Application.ActivityLifecycleCallbacks {

    companion object {
        private const val ATTRIBUTE_ACTIVITY = "activity"
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        NewRelic.setUserId(userSession?.userId.orEmpty())
        NewRelic.startInteraction(activity.localClassName)
        NewRelic.setInteractionName(activity.localClassName)
        NewRelic.setAttribute(ATTRIBUTE_ACTIVITY, activity.localClassName)
    }

    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityResumed(activity: Activity) {
        NewRelic.setAttribute(ATTRIBUTE_ACTIVITY, activity.localClassName)
    }
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}
}
