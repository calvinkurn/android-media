package com.tkpd.remoteresourcerequest.worker

import android.content.Context
import androidx.annotation.RawRes
import androidx.work.*
import com.tkpd.remoteresourcerequest.R
import com.tkpd.remoteresourcerequest.callback.DeferredTaskCallback
import com.tkpd.remoteresourcerequest.task.ResourceDownloadManager
import com.tkpd.remoteresourcerequest.type.RequestedResourceType
import com.tkpd.remoteresourcerequest.utils.DeferredWorkerHelper
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume


class DeferredWorker(val context: Context, params: WorkerParameters) :
        CoroutineWorker(context, params), CoroutineScope {

    private val resourceDownloadManager = ResourceDownloadManager.getManager()
    private val deferredWorkerHelper = DeferredWorkerHelper(context)

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

        if (isDeferredWorkCompleted(context, resId)) {
            resourceDownloadManager.logCurrentState(
                    context.getString(R.string.rem_res_req_worker_completed_message).format(WORKER_TAG, list.size)
            )
            return Result.success()

        }
        resourceDownloadManager.logCurrentState(
                context.getString(R.string.rem_res_req_worker_retry_message).format(WORKER_TAG)
        )
        return Result.retry()
    }

    private suspend fun startDownload(resourceType: RequestedResourceType): Boolean =
            suspendCancellableCoroutine { cont ->
                cont.invokeOnCancellation {
                    cont.cancel()
                }
                resourceType.isRequestedFromWorker = true
                resourceDownloadManager.startDownload(
                        resourceType,
                        object : DeferredTaskCallback {
                            override fun onTaskCompleted(resourceUrl: String?, filePath: String?) {
                                cont.resume(true)
                            }

                            override fun onTaskFailed(resourceUrl: String?) {
                                cont.resume(false)
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
                    try {
                        if (!isDeferredWorkCompleted(context, resourceId)) {
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
                            WorkManager.getInstance(context)
                                    .enqueueUniqueWork(WORKER_TAG, ExistingWorkPolicy.KEEP, pushWorker)
                            resourceDownloadManager.logCurrentState(
                                    context.getString(R.string.rem_res_req_worker_scheduled_message).format(WORKER_TAG)
                            )
                        } else {
                            resourceDownloadManager.logCurrentState(
                                    context.getString(R.string.rem_res_req_worker_schedule_not_required_message)
                                            .format(WORKER_TAG)
                            )
                        }
                    } catch (ex: Exception) {
                        resourceDownloadManager.logCurrentState(
                                context.getString(R.string.rem_res_req_worker_schedule_not_required_message)
                                        .format(WORKER_TAG)
                        )
                    }
                }
            } catch (ex: Exception) {
                resourceDownloadManager.logCurrentState(
                        context.getString(R.string.rem_res_req_worker_schedule_not_required_message)
                                .format(WORKER_TAG)
                )


            }
        }

        private fun isDeferredWorkCompleted(context: Context, resourceId: Int): Boolean {
            return DeferredWorkerHelper(context)
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
