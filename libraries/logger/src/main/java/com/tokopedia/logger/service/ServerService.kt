package com.tokopedia.logger.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Binder
import android.os.IBinder
import com.tokopedia.logger.LogManager
import com.tokopedia.logger.utils.globalScopeLaunch

class ServerService : Service() {
    private val binder = ServerServiceBinder()

    companion object {
        var isRunning = false
    }

    class ServerServiceBinder : Binder() {
        fun getService(): ServerService {
            return ServerService()
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(isRunning) stopSelf()
        globalScopeLaunch({
            LogManager.deleteExpiredLogs()
            if(isNetworkAvailable(application) and (LogManager.getCount() > 0)) {
                LogManager.sendLogToServer()
            }
        }, {
            it.printStackTrace()
        },{
            isRunning = false
            stopSelf()
        })
        return super.onStartCommand(intent, flags, startId)
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.allNetworkInfo
        return activeNetwork.isNotEmpty()
    }
}