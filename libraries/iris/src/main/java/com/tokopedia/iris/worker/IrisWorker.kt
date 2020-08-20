package com.tokopedia.iris.worker

import android.content.Context
import androidx.work.*
import com.tokopedia.iris.model.Configuration
import com.tokopedia.iris.util.DEFAULT_MAX_ROW
import com.tokopedia.iris.util.MAX_ROW
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.concurrent.TimeUnit

class IrisWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val maxRow = inputData.getInt(MAX_ROW, DEFAULT_MAX_ROW)
                IrisServiceCore.run(applicationContext, maxRow)
            } catch (e: Exception) {
                Timber.e("P1#IRIS#worker %s", e.toString())
            }
            Result.success()
        }
    }

    companion object {
        const val WORKER_NAME = "IRIS_WORKER"
        const val DELAY_BETWEEN_SCHEDULE = 500L
        var delayWorker: OneTimeWorkRequest? = null
        var immediateWorker: OneTimeWorkRequest? = null
        var isWorkerRunning: Boolean = false

        private fun createNewWorker(conf: Configuration, runImmediate: Boolean): OneTimeWorkRequest {
            // we do not use periodic because it can only run every 15 minutes
            return OneTimeWorkRequest
                    .Builder(IrisWorker::class.java)
                    .setConstraints(Constraints.Builder()
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            .build())
                    .setInputData(Data.Builder().apply {
                        putInt(MAX_ROW, conf.maxRow)
                    }.build())
                    .setInitialDelay(if (runImmediate) {
                        0
                    } else {
                        conf.intervalSeconds
                    }, TimeUnit.SECONDS)
                    .build()
        }

        suspend fun scheduleWorker(context: Context, conf: Configuration, runImmediate: Boolean) {
            try {
                //to allow the previous running worker update the state
                delay(DELAY_BETWEEN_SCHEDULE)

                val worker = getOrCreateWorker(conf, runImmediate)
                WorkManager.getInstance(context).enqueueUniqueWork(
                        WORKER_NAME,
                        if (runImmediate) {
                            ExistingWorkPolicy.REPLACE
                        } else {
                            ExistingWorkPolicy.KEEP
                        },
                        worker)
            } catch (ex: Exception) {
                Timber.e("P1#IRIS#scheduleError %s", ex.toString())
            }
        }

        private fun getOrCreateWorker(conf: Configuration, isImmediate: Boolean): OneTimeWorkRequest {
            if (isImmediate) {
                val curWorker = immediateWorker
                return if (curWorker == null) {
                    val worker = createNewWorker(conf, true)
                    immediateWorker = worker
                    worker
                } else {
                    curWorker
                }
            } else {
                val curWorker = delayWorker
                return if (curWorker == null) {
                    val worker = createNewWorker(conf, false)
                    delayWorker = worker
                    worker
                } else {
                    curWorker
                }
            }
        }

        fun cancel(context: Context) {
            //noop. It is not a periodic worker. No need to cancel.
        }
    }
}