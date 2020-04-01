package com.tokopedia.shakedetect

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.config.GlobalConfig

class ShakeSubscriber(val appContext: Context, val callback: ShakeDetectManager.Callback) : Application.ActivityLifecycleCallbacks {

    companion object {
        private val SHAKE_SHAKE = "shake-shake"
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        if (!GlobalConfig.isSellerApp()) {
            ShakeDetectManager.getShakeDetectManager().init(appContext, callback)
        }
    }

    override fun onActivityResumed(activity: Activity?) {
        processOnActivityResumed(activity, activity?.intent)
    }

    private fun processOnActivityResumed(activity: Activity?, intent: Intent?) {
        if (activity != null && activity is BaseActivity && intent != null) {
            val baseActivity = activity
            if (!GlobalConfig.isSellerApp()) {
                var screenName = baseActivity.screenName
                if (screenName == null) {
                    screenName = baseActivity.javaClass.simpleName
                }

                var mAllowShake = true
                val key = getKeyValueByCaseInsensitive(intent.extras)
                if (key != null && !key.isEmpty()) {
                    mAllowShake = key.toBoolean()
                }

                if (mAllowShake && baseActivity.isAllowShake) {
                    ShakeDetectManager.getShakeDetectManager().registerShake(screenName, activity)
                }
            }
        }
    }

    private fun getKeyValueByCaseInsensitive(bundle: Bundle?): String? {
        if (bundle == null) {
            return null
        }

        val keySet = bundle.keySet()
        for (key in keySet) {
            if (key.toLowerCase() == SHAKE_SHAKE)
                return bundle.getString(key)
        }
        return null
    }

    override fun onActivityPaused(activity: Activity?) {
        if (!GlobalConfig.isSellerApp()) {
            ShakeDetectManager.getShakeDetectManager().unregisterShake()
        }
    }

    override fun onActivityDestroyed(activity: Activity?) {
        if (activity != null && activity is BaseActivity) {
            val baseActivity = activity
            if (!GlobalConfig.isSellerApp()) {
                var screenName = baseActivity.screenName
                if (screenName == null) {
                    screenName = baseActivity.javaClass.simpleName
                }
                ShakeDetectManager.getShakeDetectManager().onDestroy(screenName, baseActivity)
            }
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {

    }

    override fun onActivityStarted(activity: Activity?) {
    }

    override fun onActivityStopped(activity: Activity?) {
    }
}