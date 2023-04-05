package com.tokopedia.tokochat.stub.common

import android.app.Activity
import androidx.test.espresso.IdlingResource
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage


class ChatHistoryIdlingResource: IdlingResource {

    private var resourceCallback: IdlingResource.ResourceCallback? = null
    private var isIdle = false

    override fun getName(): String {
        return ChatHistoryIdlingResource::class.java.name
    }

    override fun isIdleNow(): Boolean {
        if (isIdle) return true;
        if (getCurrentActivity() == null) return false
//
//        getCurrentActivity()?.fragmentManager?.findFragmentByTag()

        if (isIdle) {
            resourceCallback!!.onTransitionToIdle()
        }
        return isIdle
    }

    private fun getCurrentActivity(): Activity? {
        var currentActivity: Activity? = null
        InstrumentationRegistry.getInstrumentation()
            .runOnMainSync {
                run {
                    currentActivity = ActivityLifecycleMonitorRegistry.getInstance()
                        .getActivitiesInStage(Stage.RESUMED).elementAtOrNull(0)
                }
            }
        return currentActivity
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.resourceCallback = callback
    }
}
