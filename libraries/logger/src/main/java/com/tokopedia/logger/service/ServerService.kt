package com.tokopedia.logger.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Binder
import android.os.IBinder
import com.tokopedia.logger.LogManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class ServerService : Service() {
    private val binder = ServerServiceBinder()

    class ServerServiceBinder : Binder() {
        fun getService(): ServerService {
            return ServerService()
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        runBlocking {
            launch(Dispatchers.IO){
                when {
                    // When there is network connection and there is data in DB then we send logs to server
                    isNetworkAvailable(application) and (LogManager.getCount() > 0) -> {
                        LogManager.deleteExpiredLogs()
                        LogManager.sendLogToServer()
                        stopSelf()
                    }
                    // When there is data in DB but no network connection, we check this data, if its old we delete it
                    LogManager.loggerRepository.getCount() > 0 -> {
                        LogManager.deleteExpiredLogs()
                        stopSelf()
                    }
                    else -> {
                        stopSelf()
                    }
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.allNetworkInfo
        return activeNetwork.isNotEmpty()
    }
}