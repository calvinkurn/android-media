package com.tkpd.remoteresourcerequest.task

import android.content.Context
import android.graphics.Bitmap
import android.util.DisplayMetrics
import com.tkpd.remoteresourcerequest.callback.DeferredTaskCallback
import com.tkpd.remoteresourcerequest.database.ResourceDB
import com.tkpd.remoteresourcerequest.runnable.ImageDecodeRunnable
import com.tkpd.remoteresourcerequest.runnable.ResourceDownloadRunnable
import com.tkpd.remoteresourcerequest.type.ImageType
import com.tkpd.remoteresourcerequest.type.RequestedResourceType
import com.tkpd.remoteresourcerequest.utils.Constants
import com.tkpd.remoteresourcerequest.utils.Constants.URL_SEPARATOR
import com.tkpd.remoteresourcerequest.view.DeferredImageView
import okhttp3.OkHttpClient
import java.io.File
import java.lang.ref.WeakReference

class DeferredResourceTask(
        resourceDownloadManager: ResourceDownloadManager,
        private val okHttpClient: OkHttpClient,
        private val resourceDB: ResourceDB
) :
        ImageDecodeRunnable.TaskDecodeProperties,
        ResourceDownloadRunnable.TaskDownloadProperties {
    internal var isRequestedFromWorker: Boolean = false
    private var mDecodeRunnable: Runnable? = null
    internal var mDownloadRunnable: Runnable? = null
    private lateinit var mUrl: String
    private val downloadManager by lazy { resourceDownloadManager }
    internal var deferredImageView: WeakReference<DeferredImageView>? = null
    private var deferredTaskCallback: DeferredTaskCallback? = null
    private var mResVersion: String = ""

    private var byteArray: ByteArray? = null

    private var currentThread: Thread? = null

    private var bitmap: Bitmap? = null

    override val mTargetWidth: Int
        get() = deferredImageView?.get()?.width ?: 0


    override val mTargetHeight: Int
        get() = deferredImageView?.get()?.height ?: 0

    internal var requestedDensity: Int = 0

    fun initTask(
            url: String,
            resourceType: RequestedResourceType,
            deferredTaskCallback: DeferredTaskCallback?
    ) {
        mUrl = url
        deferredImageView = resourceType.imageView?.let { WeakReference(it) }
        mResVersion = resourceType.resourceVersion
        isRequestedFromWorker = resourceType.isRequestedFromWorker
        if (resourceType is ImageType)
            requestedDensity = resourceType.densityType
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
        val name = mUrl.substring(mUrl.lastIndexOf(Constants.URL_SEPARATOR))
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

    /**
     *  Creating separate [resourceVersion] for separate task objects since in future it can be used
     *  to purge specific file if it gets updated in server using remote config from firebase.
     *  Current behavior is similar to that of an apk update. Unless you don't update the app, you
     *  can't get updated resources.
     */
    override fun resourceVersion() = mResVersion

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
        byteArray = null
        mDownloadRunnable = null
        mDecodeRunnable = null
        deferredImageView?.clear()
        deferredTaskCallback = null
        bitmap = null
    }

    fun notifyDownloadComplete() {
        deferredTaskCallback?.onTaskCompleted(
                mUrl.substring(mUrl.lastIndexOf(URL_SEPARATOR) + 1),
                filePath = getFilePath()
        )
    }

    fun notifyDownloadFailed() {
        deferredTaskCallback?.onTaskFailed(
                mUrl.substring(mUrl.lastIndexOf(URL_SEPARATOR) + 1)
        )
    }

    private fun getFilePath(): String? = getFileLocationFromDirectory().absolutePath

}
