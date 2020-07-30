package com.tkpd.remoteresourcerequest.task

import android.content.Context
import android.content.ContextWrapper
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.ImageView
import androidx.annotation.RawRes
import com.tkpd.remoteresourcerequest.database.ResourceDB
import com.tkpd.remoteresourcerequest.utils.CallbackDispatcher
import com.tkpd.remoteresourcerequest.utils.DeferredCallback
import com.tkpd.remoteresourcerequest.utils.DensityFinder
import com.tkpd.remoteresourcerequest.view.DeferredImageView
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

    lateinit var context: WeakReference<Context>
    lateinit var handler: Handler
    private lateinit var mBaseUrl: String
    private lateinit var mRelativeUrl: String
    private val map = mutableMapOf<String, ArrayList<DeferredResourceTask>?>()

    private lateinit var mClient: OkHttpClient

    internal var deferredCallback: DeferredCallback? = null

    private var density: String = ""

    fun initialize(context: Context, @RawRes resourceId: Int): ResourceDownloadManager {
        initializeFields()
        this.context = WeakReference(context.applicationContext)
        roomDB = ResourceDB.getDatabase(context)
        density = getDisplayDensity(context)
        scheduleWorker(context, resourceId)
        return this
    }

    fun scheduleWorker(context: Context, resourceId: Int) {
        DeferredWorker.schedulePeriodicWorker(context, this, resourceId)
    }

    fun addDeferredCallback(deferredCallback: DeferredCallback): ResourceDownloadManager {
        this.deferredCallback = deferredCallback
        return this
    }

    fun getDisplayDensity(context: Context): String {
        return DensityFinder.findDensity(context, deferredCallback)
    }

    /**
     * Call this method when you need to change the base url and relative url of the remote server
     * where file to be downloaded is placed.
     */
    fun setBaseAndRelativeUrl(baseUrl: String, relativeUrl: String): ResourceDownloadManager {
        if (baseUrl != "" && !::mBaseUrl.isInitialized)
            mBaseUrl = baseUrl
        if (relativeUrl != "" && !::mRelativeUrl.isInitialized)
            mRelativeUrl = relativeUrl
        return this
    }

    private fun initializeFields() {
        handler = getMainHandler()
        resourceDownloadTaskQueue = LinkedBlockingQueue<DeferredResourceTask>()
        mDownloadWorkQueue = LinkedBlockingQueue<Runnable>()
        mDecodeWorkQueue = LinkedBlockingQueue<Runnable>()
        mDownloadTaskThreadPool = ThreadPoolExecutor(
                DEFAULT_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, mDownloadWorkQueue
        )
        mDecodeTaskThreadPool = ThreadPoolExecutor(
                DEFAULT_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, mDecodeWorkQueue
        )
    }

    fun startDownload(
            remoteFileName: String,
            imageView: DeferredImageView?,
            deferredTaskCallback: DeferredTaskCallback?
    ): DeferredResourceTask {
        check(!(!::mBaseUrl.isInitialized || !::mRelativeUrl.isInitialized)) {
            "ResourceDownloadManager not initialized!! " +
                    "Call ResourceDownloadManager.setBaseAndRelativeUrl(baseUrl, relativeUrl) at least once!!!!"
        }
        check(::context.isInitialized) {
            "ResourceDownloadManager not initialized!! " +
                    "Call ResourceDownloadManager.initialize(context, resId) first!!!!"
        }

        val customUrl = getResourceUrl(remoteFileName)
        var task = pollForIdleTask()
        if (task == null) {
            task = getNewDownloadTaskInstance()
        }
        task.isRequestedFromWorker = imageView?.let { false } ?: true
        task.initTask(customUrl, imageView, deferredTaskCallback)

        if (map[customUrl] == null || map[customUrl]?.size == 0) {
            map[customUrl] = ArrayList()
            mDownloadTaskThreadPool.execute(task.getDownloadRunnable())
        } else {
            task.removeDownloadRunnable()
            CallbackDispatcher.dispatchLog(deferredCallback,
                    "DOWNLOAD_ALREADY_IN_PROGRESS ${task.getDownloadUrl()}")
        }
        map[customUrl]!!.add(task) // Added !! since null case handled in if case above

        return task
    }

    fun getNewDownloadTaskInstance(): DeferredResourceTask {
        return DeferredResourceTask(this, getClient(), roomDB)
    }

    internal fun getResourceUrl(remoteFileName: String): String {
        val url = mBaseUrl + mRelativeUrl + remoteFileName
        return convertToCustomUrl(url)
    }

    private fun convertToCustomUrl(url: String): String {
        val indexToBreak = url.lastIndexOf(URL_SEPARATOR) + 1
        val name = url.substring(0, indexToBreak)
        return (name + density + URL_SEPARATOR + url.substring(indexToBreak))
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
        when (message.what) {
            DOWNLOAD_STARTED -> {
                CallbackDispatcher.dispatchLog(deferredCallback,
                        "DOWNLOAD_STARTED ${task.getDownloadUrl()}")
                return true
            }
            DOWNLOAD_SKIPPED -> {
                CallbackDispatcher.dispatchLog(deferredCallback,
                        "DOWNLOAD_SKIPPED ${task.getDownloadUrl()}")
                CallbackDispatcher.onCacheHit(deferredCallback, task.getDownloadUrl())
                onDownloadCompleted(task)
                return true
            }
            DOWNLOAD_COMPLETED -> {
                CallbackDispatcher.dispatchLog(deferredCallback,
                        "DOWNLOAD_COMPLETED ${task.getDownloadUrl()},  " +
                                "startedFromWorker = ${task.isRequestedFromWorker} ")
                CallbackDispatcher.onDownloadState(deferredCallback,
                        task.getDownloadUrl(), true)
                onDownloadCompleted(task)
                return true
            }

            DOWNLOAD_FAILED -> {
                CallbackDispatcher.dispatchLog(deferredCallback,
                        "DOWNLOAD_FAILED ${task.getDownloadUrl()},  " +
                                "startedFromWorker = ${task.isRequestedFromWorker} ")
                CallbackDispatcher.onDownloadState(deferredCallback,
                        task.getDownloadUrl(), false)
                offerTask(task)
                return true
            }

            DECODE_STARTED -> {
                CallbackDispatcher.dispatchLog(deferredCallback,
                        "DECODE_STARTED ${task.getDownloadUrl()}")
                return true
            }

            DECODE_COMPLETED -> {
                CallbackDispatcher.dispatchLog(deferredCallback,
                        "DECODE_COMPLETED ${task.getDownloadUrl()},  " +
                                "startedFromWorker = ${task.isRequestedFromWorker} ")

                onDecodeCompleted(task)
                return true
            }

            DECODE_FAILED -> {
                CallbackDispatcher.dispatchLog(deferredCallback,
                        "DECODE_FAILED ${task.getDownloadUrl()}, " +
                                "startedFromWorker = ${task.isRequestedFromWorker} ")
                offerTask(task)
                return true
            }

            TASK_COMPLETED -> {
                CallbackDispatcher.dispatchLog(deferredCallback,
                        "TASK_COMPLETED ${task.getDownloadUrl()}")
                offerTask(task)
                return true
            }
        }
        return false
    }

    private fun offerTask(task: DeferredResourceTask) {
        map[task.getDownloadUrl()]?.clear()
        map[task.getDownloadUrl()] = null
        task.recycleTask()
        resourceDownloadTaskQueue.offer(task)
    }

    private fun onDownloadCompleted(completedTask: DeferredResourceTask) {
        val taskList = map[completedTask.getDownloadUrl()]
        taskList?.forEach { task ->
            task.setByteBuffer(completedTask.getByteBuffer())
            task.deferredImageView?.let {
                if (it.get() != null) {
                    it.get()?.mRemoteFileName = ""
                    startDecoding(task)
                } else {
                    handler.obtainMessage(TASK_COMPLETED, task).sendToTarget()
                }
            } ?: handler.obtainMessage(TASK_COMPLETED, task).sendToTarget()
        }
    }

    private fun onDecodeCompleted(task: DeferredResourceTask) {
        task.deferredImageView?.let { weakRefIV ->
            if (weakRefIV.get() != null) {
                task.getBitmap()?.let {
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

    fun stopDeferredImageViewRendering(
            fileName: String,
            deferredImageView: DeferredImageView
    ) {
        val url = mBaseUrl + mRelativeUrl + fileName
        val taskList = map[url]
        taskList?.forEach { task ->
            if (task.deferredImageView?.get() == deferredImageView) {
                task.interruptDecodeRunnable()
                task.deferredImageView?.clear()
            }
        }
    }

    fun getMainHandler() = Handler(Looper.getMainLooper()) {
        handleMessage(it)
    }

    fun pollForIdleTask(): DeferredResourceTask? = resourceDownloadTaskQueue.poll()

    companion object {

        const val DOWNLOAD_STARTED = 1
        const val DOWNLOAD_COMPLETED = 2
        const val DOWNLOAD_FAILED = 3
        const val DECODE_STARTED = 4
        const val DECODE_FAILED = 5
        const val DECODE_COMPLETED = 6
        const val TASK_COMPLETED = 7
        const val DOWNLOAD_SKIPPED = 8


        private const val DEFAULT_POOL_SIZE = 2
        private const val MAX_POOL_SIZE = 6
        private const val KEEP_ALIVE_TIME: Long = 5
        private const val DEFAULT_TIME_OUT = 60L
        const val URL_SEPARATOR = "/"

        const val MANAGER_TAG: String = "ResourceDownloadManager"

        @Volatile
        private var INSTANCE: ResourceDownloadManager? = null

        fun getManager(): ResourceDownloadManager {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = ResourceDownloadManager()
                INSTANCE = instance
                return instance
            }
        }
    }

}