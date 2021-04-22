package com.tokopedia.fcmcommon.service

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.fcmcommon.BuildConfig
import com.tokopedia.fcmcommon.FirebaseMessagingManager
import com.tokopedia.fcmcommon.di.DaggerFcmComponent
import com.tokopedia.fcmcommon.di.FcmModule
import timber.log.Timber
import javax.inject.Inject

class SyncFcmTokenService : JobIntentService(), FirebaseMessagingManager.SyncListener {

    @Inject
    lateinit var fcmManager: FirebaseMessagingManager

    override fun onCreate() {
        super.onCreate()
        initInjector()
    }

    private fun initInjector() {
        DaggerFcmComponent.builder()
                .fcmModule(FcmModule(this))
                .build()
                .inject(this)
    }

    override fun onHandleWork(intent: Intent) {
        try {
            fcmManager.syncFcmToken(this)
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override fun onSuccess() {}

    override fun onError(exception: Exception?) {}

    companion object {
        private const val JOB_ID = 91219

        fun startService(context: Context) {
            // https://issuetracker.google.com/issues/112157099
            try {
                val intent = Intent(context, SyncFcmTokenService::class.java)
                enqueueWork(context, SyncFcmTokenService::class.java, JOB_ID, intent)
            } catch (e: Exception) {
                e.printStackTrace()
                logExceptionToCrashlytic(e)
            }
        }

        private fun logExceptionToCrashlytic(exception: Exception) {
            try {
                if (!BuildConfig.DEBUG) {
                    FirebaseCrashlytics.getInstance().recordException(exception)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}