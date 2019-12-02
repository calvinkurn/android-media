package com.tokopedia.fcmcommon.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.tokopedia.fcmcommon.FirebaseMessagingManager
import com.tokopedia.fcmcommon.di.DaggerFcmComponent
import com.tokopedia.fcmcommon.di.FcmModule
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

class SyncFcmTokenService: Service(), FirebaseMessagingManager.SyncListener {

    @Inject
    lateinit var fcmManager: FirebaseMessagingManager

    override fun onCreate() {
        super.onCreate()
        DaggerFcmComponent.builder()
                .fcmModule(FcmModule(this))
                .build()
                .inject(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        try {
            fcmManager.syncFcmToken(this)
        } catch (e: Exception) {
            Timber.e(e)
            stopSelf()
        }
        return START_NOT_STICKY
    }

    override fun onSuccess() {
        stopSelf()
    }

    override fun onError(exception: Exception?) {
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        fcmManager.clear()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, SyncFcmTokenService::class.java)
        }
    }
}