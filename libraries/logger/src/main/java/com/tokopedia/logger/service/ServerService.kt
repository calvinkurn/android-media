package com.tokopedia.logger.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Binder
import android.os.IBinder
import com.tokopedia.logger.LogManager
import kotlinx.coroutines.*

class ServerService : Service() {
    private val mBinder = ServerServiceBinder()
    private val TAG = "ServerService"

    class ServerServiceBinder : Binder() {
        fun getService(): ServerService {
            return ServerService()
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return mBinder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        android.util.Log.e(TAG, "in Service")
        runBlocking {
            launch(Dispatchers.IO){
                when {
                    // When there is network connection and there is data in DB then we send logs to server
                    isNetworkAvailable(application) and (LogManager.getCount() > 0) -> {
                        android.util.Log.e(TAG, "Sending Logs to LoggerCloudDatasource")
                        LogManager.inspectLogs()
                        LogManager.sendLogToServer()
                        stopSelf()
                    }
                    // When there is data in DB but no network connection, we check this data, if its old we delete it
                    LogManager.loggerRepository.getCount() > 0 -> {
                        LogManager.inspectLogs()
                        android.util.Log.e(TAG, "Delete old Data if exists")
                        stopSelf()
                    }
                    else -> {
                        android.util.Log.e(TAG, "Do Nothing")
                        stopSelf()
                    }
                }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        android.util.Log.e(TAG, "DONE")
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.allNetworkInfo
        return activeNetwork.isNotEmpty()
    }
}