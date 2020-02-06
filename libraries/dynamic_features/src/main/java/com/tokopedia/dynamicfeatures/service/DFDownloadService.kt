package com.tokopedia.dynamicfeatures.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DFDownloadService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        GlobalScope.launch {
            DFDownloader.cancelAlarm(applicationContext)
            DFDownloader.startJob(applicationContext)
            stopSelf()
        }
        return START_NOT_STICKY
    }
}