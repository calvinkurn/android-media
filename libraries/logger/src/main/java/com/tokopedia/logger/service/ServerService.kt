package com.tokopedia.logger.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

class ServerService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        ServiceLogger.run (this) {
            stopSelf()
        }
        return super.onStartCommand(intent, flags, startId)
    }

}