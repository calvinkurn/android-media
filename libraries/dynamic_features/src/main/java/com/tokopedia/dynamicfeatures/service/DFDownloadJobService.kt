package com.tokopedia.dynamicfeatures.service

import android.app.job.JobParameters
import android.app.job.JobService
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class DFDownloadJobService : JobService(), CoroutineScope {
    override fun onStopJob(params: JobParameters?): Boolean {
        return true
    }

    val handler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { _, ex ->
            DFDownloader.isServiceRunning = false
        }
    }

    override val coroutineContext: CoroutineContext by lazy {
        Dispatchers.IO + handler
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        launch {
            DFDownloader.startJob(applicationContext)
            try {
                jobFinished(params, false)
            } catch (e:Exception) {
                DFDownloader.isServiceRunning = false
            }
        }
        // this is to make sure the service is not finished, until call jobFinished
        return true
    }
}