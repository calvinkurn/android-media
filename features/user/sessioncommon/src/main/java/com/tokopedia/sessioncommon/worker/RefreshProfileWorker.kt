package com.tokopedia.sessioncommon.worker

import android.content.Context
import android.content.SharedPreferences
import androidx.work.*
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.sessioncommon.di.DaggerRefreshProfileComponent
import com.tokopedia.sessioncommon.domain.usecase.GetUserInfoAndSaveSessionUseCase
import com.tokopedia.user.session.UserSessionInterface
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * This One Time Worker is used to Refresh User Session Periodically, based on the interval.
 * The interval is set via Remote Config
 * The interval value is in MINUTE.
 */

class RefreshProfileWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    @Inject
    lateinit var getUserInfoUseCase: GetUserInfoAndSaveSessionUseCase

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var firebaseRemoteConfig: RemoteConfig

    init {
        DaggerRefreshProfileComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    override suspend fun doWork(): Result {
        if (userSession.isLoggedIn) {
            if (isNeedRefresh()) {
                try {
                    getUserInfoUseCase(Unit)
                    saveRefreshTime(System.currentTimeMillis())
                } catch (ignored: Exception) {}
            }
        }
        return Result.success()
    }

    private val sharedPref: SharedPreferences by lazy {
        appContext.getSharedPreferences(
            REFRESH_PROFILE_PREF, Context.MODE_PRIVATE
        )
    }

    private fun saveRefreshTime(refreshTime: Long) {
        sharedPref.edit().putLong(REFRESH_PROFILE_PREF_KEY, refreshTime).apply()
    }

    private fun getLatestRefreshTime(): Long {
        return sharedPref.getLong(REFRESH_PROFILE_PREF_KEY, 0L)
    }

    private fun isNeedRefresh(): Boolean {
        try {
            val interval = firebaseRemoteConfig.getLong(REFRESH_PROFILE_REMOTE_CONFIG_KEY, 0L)
            if (interval > 0L) {
                val currentTime = System.currentTimeMillis()
                val previousTime = getLatestRefreshTime()
                // if the users never refresh their profile, always return true
                if (previousTime == 0L) { return true }
                // Need to convert the difference to MINUTE, because we use minute as the standard
                val diff = TimeUnit.MILLISECONDS.toMinutes(currentTime - previousTime)
                return (diff > interval)
            }
        }catch (ignored: Exception) { }
        return false
    }

    companion object {

        private const val REFRESH_PROFILE_REMOTE_CONFIG_KEY = "android_user_refresh_profile"
        private const val REFRESH_PROFILE_PREF = "REFRESH_PROFILE_INTERVAL"

        private const val REFRESH_PROFILE_PREF_KEY = "refresh_interval"

        private const val WORKER_NAME = "REFRESH_PROFILE_WORKER"

        private const val INITIAL_DELAY = 3L

        @JvmStatic
        fun scheduleWorker(appContext: Context) {
            try {
                val worker = OneTimeWorkRequest
                    .Builder(RefreshProfileWorker::class.java)
                    .setInitialDelay(INITIAL_DELAY, TimeUnit.SECONDS)
                    .build()

                WorkManager.getInstance(appContext).enqueueUniqueWork(
                    WORKER_NAME,
                    ExistingWorkPolicy.REPLACE,
                    worker
                )
            } catch (ignored: Exception) { }
        }
    }
}
