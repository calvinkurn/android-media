package com.tokopedia.dynamicfeatures.service

import android.app.job.JobParameters
import android.app.job.JobService
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class DFDownloadJobService : JobService() {
    override fun onStopJob(params: JobParameters?): Boolean {
        return true
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        GlobalScope.launch {
            DFDownloader.startJob(applicationContext)
            jobFinished(params, false)
        }
        return true
    }
}