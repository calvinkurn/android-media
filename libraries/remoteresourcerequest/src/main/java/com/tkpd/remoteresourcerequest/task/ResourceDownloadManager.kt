package com.tkpd.remoteresourcerequest.task

import android.content.Context
import android.content.ContextWrapper
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.ImageView
import androidx.annotation.RawRes
import com.tkpd.remoteresourcerequest.R
import com.tkpd.remoteresourcerequest.callback.CallbackDispatcher
import com.tkpd.remoteresourcerequest.callback.DeferredCallback
import com.tkpd.remoteresourcerequest.callback.DeferredTaskCallback
import com.tkpd.remoteresourcerequest.database.ResourceDB
import com.tkpd.remoteresourcerequest.type.RequestedResourceType
import com.tkpd.remoteresourcerequest.utils.Constants.CONTEXT_NOT_INITIALIZED_MESSAGE
import com.tkpd.remoteresourcerequest.utils.Constants.URL_NOT_INITIALIZED_MESSAGE
import com.tkpd.remoteresourcerequest.utils.DensityFinder
import com.tkpd.remoteresourcerequest.worker.DeferredWorker
import okhttp3.OkHttpClient
import java.lang.ref.WeakReference
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class ResourceDownloadManager private constructor() {

    private lateinit var roomDB: ResourceDB

    lateinit var resourceDownloadTaskQueue: BlockingQueue<DeferredResourceTask>
        private set

    private lateinit var mDownloadWorkQueue: BlockingQueue<Runnable>
    private lateinit var mDecodeWorkQueue: BlockingQueue<Runnable>

    private lateinit var mDownloadTaskThreadPool: ThreadPoolExecutor
    private lateinit var mDecodeTaskThreadPool: ThreadPoolExecutor

    private lateinit var context: WeakReference<Context>
    private lateinit var handler: Handler

    private var mBaseUrl: String = ""
    private var mRelativeUrl: String = ""
    private lateinit var mClient: OkHttpClient


    internal var deferredCallback: DeferredCallback? = null

    companion object {

        const val DOWNLOAD_STARTED = 1
        const val DOWNLOAD_COMPLETED = 2
        const val DOWNLOAD_FAILED = 3
        const val DECODE_STARTED = 4
        const val DECODE_FAILED = 5
        const val DECODE_COMPLETED = 6
        const val TASK_COMPLETED = 7
        const val DOWNLOAD_SKIPPED = 8


        private const val DEFAULT_POOL_SIZE = 4
        private const val MAX_POOL_SIZE = 6
        private const val KEEP_ALIVE_TIME: Long = 5
        private const val DEFAULT_TIME_OUT = 60L

        const val MANAGER_TAG: String = "RDM"

        @Volatile
        private var INSTANCE: ResourceDownloadManager? = null

        fun getManager(): ResourceDownloadManager {
            val tempInstance =
                    INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance =
                        ResourceDownloadManager()
                INSTANCE = instance
                return instance
            }
        }
    }

    /**
     * this method should be called before proceeding further with anything. Specially
     * before creating MultiDPIImageType object so that density of device can be matched for it
     * and download can happen for proper density.
     **/
    fun initialize(
            context: Context,
            @RawRes resourceId: Int
    ): ResourceDownloadManager {
        initializeFields()
        this.context = WeakReference(context.applicationContext)
        roomDB = ResourceDB.getDatabase(context)
        initDensityPathToAppendInUrl(context)
        scheduleWorker(context, resourceId)
        return this
    }

    /**
     * Call this method when you need to change the base url and relative url of the remote server
     * where file to be downloaded is placed.
     */
    fun setBaseAndRelativeUrl(baseUrl: String, relativeUrl: String): ResourceDownloadManager {
        if (baseUrl.isNotEmpty() && mBaseUrl.isEmpty())
            mBaseUrl = baseUrl
        if (relativeUrl.isNotEmpty() && mRelativeUrl.isEmpty())
            mRelativeUrl = relativeUrl
        return this
    }

    fun addDeferredCallback(deferredCallback: DeferredCallback): ResourceDownloadManager {
        this.deferredCallback = deferredCallback
        return this
    }

    internal fun scheduleWorker(context: Context, resourceId: Int) {
        DeferredWorker
                .schedulePeriodicWorker(context, this, resourceId)
    }

    private fun initDensityPathToAppendInUrl(context: Context): String {
        DensityFinder.initializeDensityPath(context)
        return DensityFinder.densityUrlPath
    }

    private fun initializeFields() {
        handler = getMainHandler()
        resourceDownloadTaskQueue = LinkedBlockingQueue(10)
        mDownloadWorkQueue = LinkedBlockingQueue()
        mDecodeWorkQueue = LinkedBlockingQueue()
        mDownloadTaskThreadPool = ThreadPoolExecutor(
                DEFAULT_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE_TIME, TimeUnit.SECONDS, mDownloadWorkQueue
        )
        mDecodeTaskThreadPool = ThreadPoolExecutor(
                DEFAULT_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE_TIME, TimeUnit.SECONDS, mDecodeWorkQueue
        )
    }

    fun startDownload(
            resourceType: RequestedResourceType,
            deferredTaskCallback: DeferredTaskCallback?
    ): DeferredResourceTask {
        check(::context.isInitialized) {
            CONTEXT_NOT_INITIALIZED_MESSAGE
        }

        check(mBaseUrl.isNotEmpty() && mRelativeUrl.isNotEmpty()) {
            URL_NOT_INITIALIZED_MESSAGE
        }

        val customUrl =
                if (resourceType.remoteFileCompleteUrl.isEmpty())
                    getResourceUrl(resourceType)
                else
                    resourceType.remoteFileCompleteUrl
        var task = pollForIdleTask()
        task = task ?: getNewDownloadTaskInstance()

        task.initTask(customUrl, resourceType, deferredTaskCallback)

        mDownloadTaskThreadPool.execute(task.getDownloadRunnable())

        return task
    }

    fun getNewDownloadTaskInstance(): DeferredResourceTask {
        return DeferredResourceTask(
                this,
                getClient(),
                roomDB
        )
    }

    internal fun getResourceUrl(remoteFileType: RequestedResourceType): String {
        return mBaseUrl + mRelativeUrl + remoteFileType.relativeFilePath
    }

    fun getContextWrapper(): ContextWrapper {
        return ContextWrapper(context.get())
    }

    private fun getClient(): OkHttpClient {
        if (!::mClient.isInitialized) {
            mClient = OkHttpClient().newBuilder()
                    .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                    .readTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                    .writeTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                    .build()
        }
        return mClient
    }

    fun handleState(task: DeferredResourceTask, state: Int) {
        val message = handler.obtainMessage(state, task)
        message.sendToTarget()
    }

    fun handleMessage(message: Message): Boolean {

        val task = message.obj as DeferredResourceTask
        var urlAndWorkerInfo = "%s,%s"
        urlAndWorkerInfo =
                urlAndWorkerInfo.format(task.isRequestedFromWorker, task.getDownloadUrl())

        when (message.what) {
            /******************** DOWNLOAD state handling starts ********************/
            DOWNLOAD_STARTED -> {
                logCurrentState("DOWNLOAD_STARTED,$urlAndWorkerInfo")
                return true
            }
            DOWNLOAD_SKIPPED -> {
                logCurrentState("DOWNLOAD_SKIPPED,$urlAndWorkerInfo")
                CallbackDispatcher.onCacheHit(deferredCallback, task.getDownloadUrl())
                onDownloadCompleted(task)
                return true
            }
            DOWNLOAD_COMPLETED -> {
                logCurrentState("DOWNLOAD_COMPLETED,$urlAndWorkerInfo")
                CallbackDispatcher.onDownloadState(
                        deferredCallback,
                        task.getDownloadUrl(), true
                )

                onDownloadCompleted(task)
                return true
            }
            DOWNLOAD_FAILED -> {
                logCurrentState("DOWNLOAD_FAILED,$urlAndWorkerInfo")
                CallbackDispatcher.onDownloadState(
                        deferredCallback,
                        task.getDownloadUrl(), false
                )
                onDownloadFailed(task)
                offerTask(task)
                return true
            }
            /******************* DOWNLOAD state handling ends ***********************/

            /******************* DECODE state handling starts ***********************/
            DECODE_STARTED -> {
                logCurrentState("DECODE_STARTED,$urlAndWorkerInfo")
                return true
            }
            DECODE_COMPLETED -> {
                logCurrentState("DECODE_COMPLETED,$urlAndWorkerInfo")

                onDecodeCompleted(task)
                return true
            }
            DECODE_FAILED -> {
                logCurrentState("DECODE_FAILED,$urlAndWorkerInfo")

                offerTask(task)
                return true
            }
            /******************* DECODE state handling ends *************************/

            TASK_COMPLETED -> {
                logCurrentState("TASK_COMPLETED,$urlAndWorkerInfo")
                offerTask(task)
                return true
            }
        }
        return false
    }

    private fun onDownloadFailed(task: DeferredResourceTask) {
        task.deferredImageView?.let { imageReference ->
            imageReference.get()?.setImageResource(R.drawable.ic_loading_error)
        }
        task.notifyDownloadFailed()
    }

    internal fun logCurrentState(logMessage: String) {
        CallbackDispatcher.dispatchLog(deferredCallback, logMessage)
    }

    private fun offerTask(task: DeferredResourceTask) {
        task.recycleTask()
        resourceDownloadTaskQueue.offer(task)
    }

    private fun onDownloadCompleted(completedTask: DeferredResourceTask) {
        completedTask.deferredImageView?.let {
            if (it.get() != null) {
                it.get()?.mRemoteFileName = ""
                it.get()?.mCompleteUrl = ""
                startDecoding(completedTask)
            } else {
                handler.obtainMessage(TASK_COMPLETED, completedTask).sendToTarget()
            }
        } ?: kotlin.run {
            handler.obtainMessage(TASK_COMPLETED, completedTask).sendToTarget()
        }
        completedTask.notifyDownloadComplete()
    }

    private fun onDecodeCompleted(task: DeferredResourceTask) {
        task.deferredImageView?.let { weakRefIV ->
            if (weakRefIV.get() != null) {
                task.getBitmap()?.let {

                    if (task.requestedDensity != 0)
                        it.density = task.requestedDensity

                    (weakRefIV.get() as ImageView).setImageBitmap(it)
                }
                handler.obtainMessage(TASK_COMPLETED, task).sendToTarget()
            } else {
                handler.obtainMessage(TASK_COMPLETED, task).sendToTarget()
            }
        } ?: handler.obtainMessage(TASK_COMPLETED, task).sendToTarget()
    }

    private fun startDecoding(task: DeferredResourceTask) {
        mDecodeTaskThreadPool.execute(task.getDecodeRunnable())
    }

    fun stopDeferredImageViewRendering(task: DeferredResourceTask?) {
        if (task?.deferredImageView?.get() != null) {
            task.interruptDecodeRunnable()
            task.deferredImageView?.clear()
        }
    }

    fun getMainHandler() = Handler(Looper.getMainLooper()) {
        handleMessage(it)
    }

    fun pollForIdleTask(): DeferredResourceTask? = resourceDownloadTaskQueue.poll()

}
