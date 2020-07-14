package com.tkpd.remoteresourcerequest.task

import android.content.Context
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
                            startDownload(it)
                        }
                deferredList.add(deferred)
            }
            deferredList.awaitAll()
        }
        deferredList.clear()

        if (isDeferredWorkCompleted(context, resourceDownloadManager, resId)) {
            resourceDownloadManager.deferredCallback?.logDeferred("$WORKER_TAG successfully completed with #taskcount ${list.size}")
            return Result.success()

        }
        resourceDownloadManager.deferredCallback?.logDeferred("$WORKER_TAG RETRY NEXT TIME")
        return Result.retry()
    }

    private suspend fun startDownload(remoteFileName: String): Boolean =
            suspendCancellableCoroutine { cont ->
                cont.invokeOnCancellation {
                    cont.cancel()
                }
                resourceDownloadManager.startDownload(
                        remoteFileName,
                        null,
                        object : DeferredTaskCallback {
                            override fun onTaskCompleted(resourceUrl: String?) {
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
                                        30 * 60 * 1000,
                                        TimeUnit.MILLISECONDS
                                )
                                .setInputData(createInputData(resourceId))
                                .build()
                        WorkManager.getInstance()
                                .enqueueUniqueWork(WORKER_TAG, ExistingWorkPolicy.KEEP, pushWorker)
                        resourceDownloadManager.deferredCallback?.logDeferred("$WORKER_TAG Worker Scheduled")
                    } else {
                        resourceDownloadManager.deferredCallback?.logDeferred("$WORKER_TAG Worker Scheduling not required")
                    }
                }
            } catch (ex: Exception) {
                resourceDownloadManager.deferredCallback?.logDeferred("$WORKER_TAG Worker Scheduling not required")


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


