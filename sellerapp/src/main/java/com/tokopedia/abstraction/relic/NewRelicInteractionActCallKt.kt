package com.tokopedia.abstraction.relic

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.newrelic.agent.android.NewRelic
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * Created by @ilhamsuaib on 17/10/23.
 */

class NewRelicInteractionActCallKt(
    private val userSession: UserSessionInterface
) : Application.ActivityLifecycleCallbacks, CoroutineScope {

    companion object {
        @JvmStatic
        fun createInstance(userSession: UserSessionInterface): NewRelicInteractionActCallKt {
            return NewRelicInteractionActCallKt(userSession)
        }

        private const val ATTRIBUTE_ACTIVITY = "activityName"
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

    override fun onActivityCreated(activity: Activity, p1: Bundle?) {
        startNewRelicTrace(activity)
    }

    override fun onActivityStarted(p0: Activity) {
    }

    override fun onActivityResumed(p0: Activity) {
    }

    override fun onActivityPaused(p0: Activity) {
    }

    override fun onActivityStopped(p0: Activity) {
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {}

    override fun onActivityDestroyed(p0: Activity) {}

    private fun startNewRelicTrace(activity: Activity) {
        launch {
            NewRelic.setUserId(userSession.userId.orEmpty())
            NewRelic.startInteraction(activity.localClassName)
            NewRelic.setInteractionName(activity.localClassName)
            setNewRelicAttribute(activity)
        }
    }

    private fun setNewRelicAttribute(activity: Activity) {
        NewRelic.setAttribute(
            ATTRIBUTE_ACTIVITY,
            activity.javaClass.simpleName
        )
    }
}