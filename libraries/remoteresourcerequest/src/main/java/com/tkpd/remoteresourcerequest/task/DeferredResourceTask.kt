package com.tkpd.remoteresourcerequest.task

import android.content.Context
import android.graphics.Bitmap
import android.util.DisplayMetrics
import com.tkpd.remoteresourcerequest.database.ResourceDB
import com.tkpd.remoteresourcerequest.view.DeferredImageView
import okhttp3.OkHttpClient
import java.io.File
import java.lang.ref.WeakReference

class DeferredResourceTask(
        resourceDownloadManager: ResourceDownloadManager,
        private val okHttpClient: OkHttpClient,
        private val resourceDB: ResourceDB
) :
        ImageDecodeRunnable.TaskDecodeProperties, ResourceDownloadRunnable.TaskDownloadProperties {
    var isRequestedFromWorker: Boolean = false
    private var mDecodeRunnable: Runnable? = null

    var mDownloadRunnable: Runnable? = null
    private lateinit var mUrl: String
    private val downloadManager by lazy { resourceDownloadManager }
    var deferredImageView: WeakReference<DeferredImageView>? = null
    var deferredTaskCallback: DeferredTaskCallback? = null

    private var byteArray: ByteArray? = null

    private var currentThread: Thread? = null

    private var bitmap: Bitmap? = null

    override val mTargetWidth: Int
        get() = deferredImageView?.get()?.width ?: 0


    override val mTargetHeight: Int
        get() = deferredImageView?.get()?.height ?: 0

    fun initTask(
            url: String,
            imageView: DeferredImageView?,
            deferredTaskCallback: DeferredTaskCallback?
    ) {
        mUrl = url
        deferredImageView =
                imageView?.let { WeakReference(it) }
        this.deferredTaskCallback = deferredTaskCallback
    }


    override fun getByteBuffer(): ByteArray? = byteArray

    override fun setByteBuffer(byteArray: ByteArray?) {
        this.byteArray = byteArray
    }

    override fun setCurrentThread(thread: Thread?) {
        this.currentThread = thread
    }

    override fun getCurrentThread(): Thread? {
        return currentThread
    }

    override fun setImage(image: Bitmap) {
        this.bitmap = image
    }

    fun getBitmap(): Bitmap? {
        return bitmap
    }

    override fun getFileLocationFromDirectory(): File {
        val directory =
                downloadManager.getContextWrapper().getDir("downloads", Context.MODE_PRIVATE)
        directory.mkdir()
        val name = mUrl.substring(mUrl.lastIndexOf(ResourceDownloadManager.URL_SEPARATOR))
        return File(directory.absoluteFile, name)
    }

    override fun getDownloadUrl() = mUrl

    override fun handleDownloadState(state: Int) {
        when (state) {
            ResourceDownloadRunnable.DOWNLOAD_STATE_STARTED ->
                downloadManager.handleState(this, ResourceDownloadManager.DOWNLOAD_STARTED)

            ResourceDownloadRunnable.DOWNLOAD_STATE_COMPLETED ->
                downloadManager.handleState(this, ResourceDownloadManager.DOWNLOAD_COMPLETED)

            ResourceDownloadRunnable.DOWNLOAD_STATE_SKIPPED ->
                downloadManager.handleState(this, ResourceDownloadManager.DOWNLOAD_SKIPPED)

            ResourceDownloadRunnable.DOWNLOAD_STATE_FAILED ->
                downloadManager.handleState(this, ResourceDownloadManager.DOWNLOAD_FAILED)

        }
    }

    override fun handleDecodeState(state: Int) {
        when (state) {
            ImageDecodeRunnable.DECODE_STATE_STARTED ->
                downloadManager.handleState(this, ResourceDownloadManager.DECODE_STARTED)

            ImageDecodeRunnable.DECODE_STATE_COMPLETED ->
                downloadManager.handleState(this, ResourceDownloadManager.DECODE_COMPLETED)

            ImageDecodeRunnable.DECODE_STATE_FAILED ->
                downloadManager.handleState(this, ResourceDownloadManager.DECODE_FAILED)
        }
    }

    override fun getDisplayMetrics(): DisplayMetrics =
            downloadManager.getContextWrapper().resources.displayMetrics

    fun getDecodeRunnable(): Runnable {
        return mDecodeRunnable ?: ImageDecodeRunnable(this)
    }

    fun interruptDecodeRunnable() {
        mDecodeRunnable?.let {
            (mDecodeRunnable as ImageDecodeRunnable).stopDecoding()
        }
    }

    fun getDownloadRunnable(): Runnable {
        return mDownloadRunnable ?: ResourceDownloadRunnable(this, okHttpClient, resourceDB)
    }

    fun removeDownloadRunnable() {
        mDownloadRunnable = null
    }

    fun recycleTask() {
        notifyDeferredWorker()
        byteArray = null
        mDownloadRunnable = null
        mDecodeRunnable = null
        deferredImageView?.clear()
        deferredTaskCallback = null
        bitmap = null
    }

    private fun notifyDeferredWorker() {
        if (deferredTaskCallback != null) {
            deferredTaskCallback?.onTaskCompleted(mUrl)
        }
    }
}