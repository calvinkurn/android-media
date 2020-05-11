package com.tokopedia.logger.service

import android.annotation.TargetApi
import android.app.job.JobParameters
import android.app.job.JobService
import android.os.Build

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class ServerJobService : JobService() {

    override fun onStartJob(params: JobParameters?): Boolean {
        ServiceLogger.run (this) {
            jobFinished(params, false)
        }
        return false
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return false
    }
}