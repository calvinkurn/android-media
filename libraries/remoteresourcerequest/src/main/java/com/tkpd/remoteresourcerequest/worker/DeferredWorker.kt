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

    private val resourceDownloadManager =
            ResourceDownloadManager.getManager()
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

        if (isDeferredWorkCompleted(
                        context,
                        resId
                )
        ) {
            resourceDownloadManager.deferredCallback?.logDeferred(
                    context.getString(R.string.worker_completed_message).format(WORKER_TAG, list.size)
            )
            return Result.success()

        }
        resourceDownloadManager.deferredCallback?.logDeferred(
                context.getString(R.string.worker_retry_message).format(WORKER_TAG)
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
                        object :
                                DeferredTaskCallback {
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

        /**
         * This [APP_VERSION] will be used to decide whether database needs to be cleared or not.
         * By default it is kept as this library version_name i.e.
         * [com.tkpd.remoteresourcerequest.BuildConfig.VERSION_NAME].
         * For older apks, all their table entries will be cleared whenever first attempt to
         * download any resource is done. This will allow this WorkManager to reschedule all
         * downloads.
         */
        private const val APP_VERSION = "app_version"
        private const val WORKER_TAG = "DEFERRED_WORKER_#1"

        fun schedulePeriodicWorker(
                context: Context,
                resourceDownloadManager: ResourceDownloadManager,
                @RawRes resourceId: Int,
                appVersion: String
        ) {
            try {
                launch {
                    /**
                     * Need to check [appVersion] saved in database as the very first internal step.
                     * If it is unmatched then we purge all entries from database and let the
                     * download start from fresh as this will be the case when we update the
                     * library/ update the app. Right now it has been handled in the download
                     * thread. But this place will be more efficient. Also to extend from here we
                     * can set firebase remote config for appversion and if needed, anytime we
                     * can set a different appversion and all user's database will be refreshed.
                     */

                    val helper = DeferredWorkerHelper(context)
                    helper.checkAppVersionAndManageDB(appVersion)

                    if (!isDeferredWorkCompleted(
                                    context,
                                    resourceId
                            )
                    ) {
                        val pushWorker = OneTimeWorkRequest
                                .Builder(DeferredWorker::class.java)
                                .setConstraints(getWorkerConstraints())
                                .setBackoffCriteria(
                                        BackoffPolicy.LINEAR,
                                        30 * 60 * 1000,
                                        TimeUnit.MILLISECONDS
                                )
                                .setInputData(
                                        createInputData(
                                                resourceId,
                                                appVersion
                                        )
                                )
                                .build()
                        WorkManager.getInstance()
                                .enqueueUniqueWork(WORKER_TAG, ExistingWorkPolicy.KEEP, pushWorker)
                        resourceDownloadManager.deferredCallback?.logDeferred(
                                context.getString(R.string.worker_scheduled_message).format(WORKER_TAG)
                        )
                    } else {
                        resourceDownloadManager.deferredCallback?.logDeferred(
                                context.getString(R.string.worker_schedule_not_required_message)
                                        .format(WORKER_TAG)
                        )
                    }
                }
            } catch (ex: Exception) {
                resourceDownloadManager.deferredCallback?.logDeferred(
                        context.getString(R.string.worker_schedule_not_required_message)
                                .format(WORKER_TAG)
                )


            }
        }

        private fun isDeferredWorkCompleted(
                context: Context,
                resourceId: Int
        ): Boolean {
            return DeferredWorkerHelper(context)
                    .getPendingDeferredResourceURLs(resourceId).isEmpty()
        }

        private fun createInputData(@RawRes resourceId: Int, appVersion: String): Data {
            return Data.Builder()
                    .putInt(RESOURCE_FILE_ID, resourceId)
                    .putString(APP_VERSION, appVersion)
                    .build()
        }

        private fun getWorkerConstraints(): Constraints = Constraints.Builder()
                .setRequiresCharging(false)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build()

    }

}


