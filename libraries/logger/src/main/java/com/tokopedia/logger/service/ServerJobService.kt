package com.tokopedia.logger.service

import android.annotation.TargetApi
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import com.tokopedia.logger.LogManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class ServerJobService : JobService() {

    override fun onStartJob(params: JobParameters?): Boolean {
        runBlocking {
            launch(Dispatchers.IO) {
                when {
                    // When there is network connection and there is data in DB then we send logs to server
                    isNetworkAvailable(application) and (LogManager.getCount() > 0) -> {
                        LogManager.deleteExpiredLogs()
                        LogManager.sendLogToServer()
                        jobFinished(params,false)
                    }
                    // When there is data in DB but no network connection, we check this data, if its old we delete it
                    LogManager.loggerRepository.getCount() > 0 -> {
                        LogManager.deleteExpiredLogs()
                        jobFinished(params,false)
                    }
                    else -> {
                        jobFinished(params,false)
                    }
                }
            }
        }
        return false
    }
    override fun onStopJob(params: JobParameters?): Boolean {
        return false
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.allNetworks
        return activeNetwork.isNotEmpty()
    }
}