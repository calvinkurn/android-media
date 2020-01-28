package com.tkpd.remoteresourcerequest.task

import android.content.Context
import android.content.ContextWrapper
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.ImageView
import androidx.annotation.RawRes
import com.tkpd.remoteresourcerequest.database.ResourceDB
import com.tkpd.remoteresourcerequest.utils.DensityFinder
import com.tkpd.remoteresourcerequest.view.DeferredImageView
import okhttp3.OkHttpClient
import org.json.JSONObject
import timber.log.Timber
import java.io.IOException
import java.io.InputStream
import java.lang.ref.WeakReference
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class ResourceDownloadManager private constructor() {

    private lateinit var roomDB: ResourceDB
    private lateinit var resourceDownloadTaskQueue: BlockingQueue<DeferredResourceTask>
    private lateinit var mCheckEntryInDB: BlockingQueue<Runnable>
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
    private var density: String = ""

    fun initialize(context: Context, @RawRes resourceId: Int) {
        initializeFields()
        this.context = WeakReference(context.applicationContext)
        roomDB = ResourceDB.getDatabase(context)
        density = getDisplayDensity(context)
        if (!::mBaseUrl.isInitialized)
            mBaseUrl = BASE_URL
        if (!::mRelativeUrl.isInitialized)
            mRelativeUrl = RELATIVE_URL
        readFromFile(resourceId)
    }

    fun getDisplayDensity(context: Context): String {
        return DensityFinder.findDensity(context)
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
        mCheckEntryInDB = LinkedBlockingQueue<Runnable>()
        mDownloadWorkQueue = LinkedBlockingQueue<Runnable>()
        mDecodeWorkQueue = LinkedBlockingQueue<Runnable>()
        mDownloadTaskThreadPool = ThreadPoolExecutor(
                DEFAULT_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, mDownloadWorkQueue
        )
        mDecodeTaskThreadPool = ThreadPoolExecutor(
                DEFAULT_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, mDecodeWorkQueue
        )
        mDecodeTaskThreadPool = ThreadPoolExecutor(
                DEFAULT_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, mDecodeWorkQueue
        )
    }

    fun readFromFile(@RawRes resourceId: Int) {
        val localContext = context.get()
        if (localContext != null) {
            val json: String?
            try {
                val inputStream: InputStream = localContext.resources.openRawResource(resourceId)
                val size: Int = inputStream.available()
                val buffer = ByteArray(size)
                inputStream.read(buffer)
                inputStream.close()
                json = String(buffer)
                val jsonObject = JSONObject(json)
                val multiDpi = jsonObject.getJSONArray("multiDpi")
                for (element in 0 until multiDpi.length()) {
                    val url = multiDpi[element] as String
                    startDownload(url, null)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun startDownload(remoteFileName: String, imageView: DeferredImageView?) {
        val url = mBaseUrl + mRelativeUrl + remoteFileName
        val customUrl = convertToCustomUrl(url)
        if (!::context.isInitialized)
            throw IllegalStateException("ResourceDownloadManager not initialized!! " +
                    "Call ResourceDownloadManager.initialize(context, resId) first!!!!")
        var task = pollForIdleTask()
        if (task == null) {
            task = getNewDownloadTaskInstance()
        }
        task.initTask(customUrl, imageView)

        if (map[customUrl] == null || map[customUrl]?.size == 0) {
            map[customUrl] = ArrayList()
            mDownloadTaskThreadPool.execute(task.getDownloadRunnable())
        } else {
            task.removeDownloadRunnable()
            Timber.d("ResourceDownloadManager: DOWNLOAD_ALREADY_IN_PROGRESS url = ${task.getDownloadUrl()}")
        }
        map[customUrl]!!.add(task) // Added !! since null case handled in if case above

    }

    fun pollForIdleTask()= resourceDownloadTaskQueue.poll()

    fun getNewDownloadTaskInstance(): DeferredResourceTask {
        return DeferredResourceTask(this, getClient(), roomDB)
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
                Timber.d("ResourceDownloadManager: DOWNLOAD_STARTED url = ${task.getDownloadUrl()}")
                return true
            }
            DOWNLOAD_SKIPPED -> {
                Timber.d("ResourceDownloadManager: DOWNLOAD_SKIPPED url = ${task.getDownloadUrl()}")
                onDownloadCompleted(task)
                return true
            }
            DOWNLOAD_COMPLETED -> {
                Timber.d(
                        "ResourceDownloadManager: DOWNLOAD_COMPLETED url = ${task.getDownloadUrl()}"
                )
                onDownloadCompleted(task)
                return true
            }

            DOWNLOAD_FAILED -> {
                Timber.d("ResourceDownloadManager: DOWNLOAD_FAILED url = ${task.getDownloadUrl()}")
                map[task.getDownloadUrl()]?.clear()
                map[task.getDownloadUrl()] = null
                resourceDownloadTaskQueue.offer(task)
                return true
            }

            DECODE_STARTED -> {
                Timber.d("ResourceDownloadManager: DECODE_STARTED url = ${task.getDownloadUrl()}")
                return true
            }

            DECODE_COMPLETED -> {
                Timber.d("ResourceDownloadManager: DECODE_COMPLETED url = ${task.getDownloadUrl()}")
                onDecodeCompleted(task)
                return true
            }

            DECODE_FAILED -> {
                Timber.d("ResourceDownloadManager: DECODE_FAILED url = ${task.getDownloadUrl()}")
                return true
            }

            TASK_COMPLETED -> {
                Timber.d("ResourceDownloadManager: TASK_COMPLETED url = ${task.getDownloadUrl()}")

                map[task.getDownloadUrl()]?.clear()
                map[task.getDownloadUrl()] = null
                resourceDownloadTaskQueue.offer(task)
                return true
            }
        }
        return false
    }

    private fun onDownloadCompleted(completedTask: DeferredResourceTask) {

        val taskList = map[completedTask.getDownloadUrl()]
        taskList?.forEach { task ->
            task.setByteBuffer(completedTask.getByteBuffer())
            task.deferredImageView?.let {
                if (it.get() != null) {
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

    fun stopPendingDownload(
            fileName: String,
            deferredImageView: DeferredImageView
    ) {
        val url = mBaseUrl + mRelativeUrl + fileName
        val taskList = map[url]
        taskList?.forEach { task ->
            if (task.deferredImageView?.get() == deferredImageView)
                mDownloadTaskThreadPool.remove(task.getDownloadRunnable())
        }
    }

    fun getMainHandler() = Handler(Looper.getMainLooper()) {
        handleMessage(it)
    }


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
        private const val BASE_URL = "https://ecs7.tokopedia.net/"
        private const val RELATIVE_URL = "android/res/"

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