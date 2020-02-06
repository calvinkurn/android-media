package com.tkpd.remoteresourcerequest.task

import android.content.Context
import android.util.Log
import androidx.annotation.RawRes
import androidx.work.*
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume


class DeferredWorker(val context: Context, params: WorkerParameters) :
        CoroutineWorker(context, params), CoroutineScope {

    private val resourceDownloadManager = ResourceDownloadManager.getManager()
    private val deferredWorkerHelper = DeferredWorkerHelper(context, resourceDownloadManager)

    override suspend fun doWork(): Result {
        val resId = inputData.getInt(RESOURCE_FILE_ID, -1)
        if (resId == -1)
            return Result.success()

        val list = deferredWorkerHelper.getPendingDeferredResourceURLs(resId)

        val deferredList: ArrayList<Deferred<Boolean>> = arrayListOf()
        coroutineScope {
            list.forEach {
                val deferred =
                        async(Dispatchers.IO) {
                            Log.d(WORKER_TAG, "start Download")
                            startDownload(it)
                        }
                deferredList.add(deferred)
            }
            deferredList.awaitAll()
        }
        deferredList.clear()
        Log.d(WORKER_TAG, "resume")
        if (isDeferredWorkCompleted(context, resourceDownloadManager, resId))
            return Result.success()
        return Result.retry()
    }

    private suspend fun startDownload(remoteFileName: String): Boolean =
            suspendCancellableCoroutine { cont ->
                cont.invokeOnCancellation {
                    Log.d(WORKER_TAG, "Cont cancelled")
                    cont.cancel()
                }
                resourceDownloadManager.startDownload(
                        remoteFileName,
                        null,
                        object : DeferredTaskCallback {
                            override fun onTaskCompleted(resourceUrl: String?) {
                                Log.d(WORKER_TAG, "Cont call $resourceUrl")
                                cont.resume(true)
                            }
                        })

            }

    companion object : CoroutineScope {

        override val coroutineContext: CoroutineContext
            get() = Dispatchers.Default

        private const val RESOURCE_FILE_ID = "resource_file_id"
        private const val WORKER_TAG = "DEFERRED_WORKER_#1"

        fun schedulePeriodicWorker(
                context: Context,
                resourceDownloadManager: ResourceDownloadManager,
                @RawRes resourceId: Int
        ) {
            try {
                launch {
                    if (!isDeferredWorkCompleted(context, resourceDownloadManager, resourceId)) {
                        val pushWorker = OneTimeWorkRequest
                                .Builder(DeferredWorker::class.java)
                                .setConstraints(getWorkerConstraints())
                                .setBackoffCriteria(
                                        BackoffPolicy.LINEAR,
                                        OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                                        TimeUnit.MILLISECONDS
                                )
                                .setInputData(createInputData(resourceId))
                                .build()
                        WorkManager.getInstance()
                                .enqueueUniqueWork(WORKER_TAG, ExistingWorkPolicy.KEEP, pushWorker)
                        Log.d(WORKER_TAG, "Worker Scheduled")
                    } else {
                        Log.d(WORKER_TAG, "Worker Scheduling not required")
                    }
                }
            } catch (ex: Exception) {
                Log.d(WORKER_TAG, "Worker ${ex.message}")

            }
        }

        private fun isDeferredWorkCompleted(
                context: Context, resourceDownloadManager: ResourceDownloadManager,
                resourceId: Int
        ): Boolean {
            return DeferredWorkerHelper(context, resourceDownloadManager)
                    .getPendingDeferredResourceURLs(resourceId).isEmpty()
        }

        private fun createInputData(@RawRes resourceId: Int): Data {
            return Data.Builder()
                    .putInt(RESOURCE_FILE_ID, resourceId)
                    .build()
        }

        private fun getWorkerConstraints(): Constraints = Constraints.Builder()
                .setRequiresCharging(false)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build()

    }

}


