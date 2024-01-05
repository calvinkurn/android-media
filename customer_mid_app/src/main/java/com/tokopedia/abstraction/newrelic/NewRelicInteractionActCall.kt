package com.tokopedia.abstraction.newrelic

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.newrelic.agent.android.NewRelic
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class NewRelicInteractionActCall(
    private val userSession: UserSessionInterface?
) : Application.ActivityLifecycleCallbacks, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        runNewRelicOnBackground(activity)
    }

    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}

    private fun runNewRelicOnBackground(activity: Activity) {
        // context : Dispatchers.Default
        launch {
            startNewRelicTrace(activity)
        }
    }

    private fun startNewRelicTrace(activity: Activity) {
        NewRelic.setUserId(userSession?.userId.orEmpty())
        NewRelic.startInteraction(activity.localClassName)
        NewRelic.setInteractionName(activity.localClassName)
    }
}
