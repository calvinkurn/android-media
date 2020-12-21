package com.tokopedia.logger.utils

import android.content.Context
import androidx.work.WorkManager
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl

// Use pruneWork for workManager
// https://developer.android.com/reference/kotlin/androidx/work/WorkManager#pruneWork()
// will limit the usage for 1 hour.
// Calling the function pruneWorkManagerIfNeeded() will do nothing if it is called before 1 hour.
class WorkManagerPruner(val context: Context) {
    companion object {
        const val REMOTE_KEY_WORK_MANAGER_ALLOW_PRUNE = "android_workmanager_allow_prune"
        const val REMOTE_KEY_WORK_MANAGER_ALLOW_PRUNE_TIME = "android_workmanager_allow_prune_time"
        const val WORK_MANAGER_ALLOW_PRUNE_TIME_DEFAULT = 3_600_000L //1 hour to prune
        const val SHARED_PREF_WM_NAME = "wm_cleaner"
        const val SHARED_PREF_WM_TS_KEY = "ts"
        var lastPrune = 0L
        var pruneDuration = 0L

        var workManagerPruner: WorkManagerPruner? = null

        @JvmStatic
        fun getInstance(context: Context): WorkManagerPruner {
            var pruner = workManagerPruner
            if (pruner == null) {
                pruner = WorkManagerPruner(context.applicationContext)
                workManagerPruner = pruner
                return pruner
            } else {
                return pruner
            }
        }
    }

    private var remoteConfig: FirebaseRemoteConfigImpl? = null

    fun pruneWorkManagerIfNeeded() {
        if (getRemoteConfigObj().getBoolean(REMOTE_KEY_WORK_MANAGER_ALLOW_PRUNE, true)) {
            if (lastPrune == 0L) {
                val sp = context.getSharedPreferences(SHARED_PREF_WM_NAME, Context.MODE_PRIVATE)
                lastPrune = sp.getLong(SHARED_PREF_WM_TS_KEY, 0L)
            }
            val now = System.currentTimeMillis()
            if (now - lastPrune > getPruneDuration()) {
                WorkManager.getInstance(context).pruneWork()
                setLastPrune(now)
            }
        }
    }

    private fun setLastPrune(ts: Long) {
        val sp = context.getSharedPreferences(SHARED_PREF_WM_NAME, Context.MODE_PRIVATE)
        sp.edit().putLong(SHARED_PREF_WM_TS_KEY, ts).apply()
        lastPrune = ts
    }

    private fun getPruneDuration(): Long {
        if (pruneDuration == 0L) {
            pruneDuration = getRemoteConfigObj().getLong(REMOTE_KEY_WORK_MANAGER_ALLOW_PRUNE_TIME,
                    WORK_MANAGER_ALLOW_PRUNE_TIME_DEFAULT)
        }
        return pruneDuration
    }

    private fun getRemoteConfigObj(): FirebaseRemoteConfigImpl {
        var remoteConfigImpl = remoteConfig
        if (remoteConfigImpl == null) {
            remoteConfigImpl = FirebaseRemoteConfigImpl(context)
            remoteConfig = remoteConfigImpl
            return remoteConfigImpl
        }
        return remoteConfigImpl
    }
}