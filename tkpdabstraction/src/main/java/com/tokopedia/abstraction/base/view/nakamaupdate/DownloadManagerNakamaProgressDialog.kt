package com.tokopedia.abstraction.base.view.nakamaupdate

import android.app.Activity
import android.app.DownloadManager
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Environment
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.lang.ref.WeakReference
import kotlin.coroutines.CoroutineContext

class DownloadManagerNakamaProgressDialog(
    private val progressDialog: ProgressDialog?,
    private val weakActivity: WeakReference<Activity>
) : CoroutineScope {

    companion object {
        private const val MAX_PROGRESS = 100
        private const val DELAY_UPDATE_PROGRESS = 500L
        private const val MESSAGE_PREPARE_DOWNLOAD = "Preparing to download..."
        private const val MESSAGE_DOWNLOADING = "Downloading new version..."
        private const val VERSION_PARAM = "version"
    }

    init {
        initDialog()
    }

    private val downloadManager by lazy {
        weakActivity.get()?.let { ContextCompat.getSystemService(it, DownloadManager::class.java) }
    }

    private val job = SupervisorJob()

    private var downloadID = -1L
    private var fileNamePath = ""

    fun startDownload(
        apkUrl: String
    ) {
        showProgressDialog()
        val request = getDownloadRequest(apkUrl)

        handleDownloadCompletion(request)
    }

    private fun handleDownloadCompletion(request: DownloadManager.Request) {
        var finishDownload = false

        launch {
            downloadID = downloadManager?.enqueue(request) ?: -1L

            withContext(Dispatchers.Main) {
                setDialogPending()
            }

            val query = DownloadManager.Query()

            if (downloadID != -1L) {
                query.setFilterById(downloadID)

                while (!finishDownload) {
                    val cursor = downloadManager?.query(query)

                    val statusColumnIndex =
                        cursor?.getColumnIndex(DownloadManager.COLUMN_STATUS) ?: -1
                    val totalSizeColumnIndex =
                        cursor?.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES) ?: -1
                    val downloadedColumnIndex =
                        cursor?.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR) ?: -1

                    if (statusColumnIndex != -1) {
                        if (cursor?.moveToFirst() == true) {
                            when (cursor.getInt(statusColumnIndex)) {
                                DownloadManager.STATUS_SUCCESSFUL -> {
                                    finishDownload = true
                                    withContext(Dispatchers.Main) {
                                        progressDialog?.progress = MAX_PROGRESS
                                        changeStyleAndHideProgressDialog()
                                    }
                                    updateInstallApk(fileNamePath)
                                }

                                DownloadManager.STATUS_FAILED -> {
                                    finishDownload = true
                                    withContext(Dispatchers.Main) {
                                        changeStyleAndHideProgressDialog()
                                    }
                                }

                                DownloadManager.STATUS_RUNNING -> {
                                    if (totalSizeColumnIndex != -1 && downloadedColumnIndex != -1) {
                                        val apkTotalSize = cursor.getLong(totalSizeColumnIndex)
                                        val downloadedProgress =
                                            cursor.getLong(downloadedColumnIndex)

                                        if (apkTotalSize > 0) {
                                            val currentProgress =
                                                ((downloadedProgress * MAX_PROGRESS.toLong()) / apkTotalSize).toInt()

                                            withContext(Dispatchers.Main) {
                                                progressDialog?.progress = currentProgress
                                            }

                                            delay(DELAY_UPDATE_PROGRESS)
                                        }
                                    }
                                }
                                else -> {
                                    // no op
                                }
                            }
                        }
                    }
                    cursor?.close()

                    if (isActive && finishDownload) {
                        job.cancel()
                    }
                }
            }
        }
    }

    private fun getDownloadRequest(
        apkUrl: String
    ): DownloadManager.Request {
        val fileName = getFileNameFromUrl(apkUrl)

        this.fileNamePath = "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}/$fileName"

        return DownloadManager.Request(Uri.parse(apkUrl))
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setTitle(fileName)
            .setAllowedOverRoaming(true)
            .setAllowedOverMetered(true)
            .setDescription(fileName)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setMimeType("application/vnd.android.package-archive")
    }

    private fun updateInstallApk(fileName: String) {
        weakActivity.get()?.let {
            val uri = FileProvider.getUriForFile(
                it,
                it.applicationContext.packageName + ".provider",
                File(fileName)
            )
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(
                    uri,
                    "application/vnd.android.package-archive"
                )
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            it.runOnUiThread {
                it.startActivity(intent)
            }
        }
    }

    private fun getFileNameFromUrl(url: String): String {
        val uri = Uri.parse(url)
        return "${uri.getQueryParameter(VERSION_PARAM).orEmpty()}.apk"
    }

    private fun initDialog() {
        progressDialog?.run {
            setCancelable(false)
            setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
            setMessage(MESSAGE_PREPARE_DOWNLOAD)
            isIndeterminate = true
        }
    }

    private fun setDialogPending() {
        progressDialog?.run {
            setMessage(MESSAGE_DOWNLOADING)
            isIndeterminate = false
            max = MAX_PROGRESS
            progress = 0
        }
    }

    private fun showProgressDialog() {
        if (progressDialog?.isShowing == false) {
            progressDialog.show()
        }
    }

    private fun changeStyleAndHideProgressDialog() {
        progressDialog?.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        hideProgressDialog()
    }

    private fun hideProgressDialog() {
        if (progressDialog?.isShowing == true) {
            progressDialog.hide()
        }
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default
}
