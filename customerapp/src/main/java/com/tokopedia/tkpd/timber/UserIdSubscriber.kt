package com.tokopedia.tkpd.timber

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.tokopedia.user.session.UserSession
import java.util.concurrent.TimeUnit

class UserIdSubscriber(private val appContext: Context, private val callBack: UserIdChangeCallback) : Application.ActivityLifecycleCallbacks {

    var userId = ""
    var lastTimeChanged: Long = System.currentTimeMillis()

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        val userSession = UserSession(appContext)
        if(userId != userSession.userId && lastTimeChanged < (System.currentTimeMillis() - TimeUnit.HOURS.toMillis(1))) {
            lastTimeChanged = System.currentTimeMillis()
            userId = userSession.userId
            callBack.onUserIdChanged()
        }
    }

    override fun onActivityResumed(activity: Activity?) {
        // No-op
    }

    override fun onActivityPaused(activity: Activity?) {
        // No-op
    }

    override fun onActivityStarted(activity: Activity?) {
        // No-op
    }

    override fun onActivityDestroyed(activity: Activity?) {
        // No-op
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
        // No-op
    }

    override fun onActivityStopped(activity: Activity?) {
        // No-op
    }
}