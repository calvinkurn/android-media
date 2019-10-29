package com.tokopedia.logger.service

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.net.ConnectivityManager
import com.tokopedia.logger.LogManager
import com.tokopedia.logger.utils.launchCatchError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class ServerJobService : JobService() {

    private val TAG = "JobService"

    override fun onStartJob(params: JobParameters?): Boolean {
        android.util.Log.e(TAG, "in JobService")
        runBlocking {
            launch(Dispatchers.IO) {
                when {
                    // When there is network connection and there is data in DB then we send logs to server
                    isNetworkAvailable(application) and (LogManager.getCount() > 0) -> {
                        android.util.Log.e(TAG, "Sending Logs")
                        LogManager.inspectLogs()
                        LogManager.sendLogToServer()
                        jobFinished(params,false)
                    }
                    // When there is data in DB but no network connection, we check this data, if its old we delete it
                    LogManager.loggerRepository.getCount() > 0 -> {
                        LogManager.inspectLogs()
                        android.util.Log.e(TAG, "Delete old Data if exists")
                        jobFinished(params,false)
                    }
                    else -> {
                        android.util.Log.e(TAG, "Do Nothing")
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
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.allNetworks
        return activeNetwork.isNotEmpty()
    }
}