package com.tokopedia.logger.service

import android.annotation.TargetApi
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import com.tokopedia.logger.LogManager
import com.tokopedia.logger.utils.globalScopeLaunch

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class ServerJobService : JobService() {

    override fun onStartJob(params: JobParameters?): Boolean {
        globalScopeLaunch({
            LogManager.deleteExpiredLogs()
            if(isNetworkAvailable(application) and (LogManager.getCount() > 0)) {
                LogManager.sendLogToServer()
            }
            jobFinished(params, false)
        }, {
            it.printStackTrace()
        })
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