package com.tokopedia.abstraction.base.view.nakamaupdate

import android.app.Activity
import android.app.DownloadManager
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.ONE
import java.lang.ref.WeakReference
import java.net.URI

class DownloadManagerNakamaProgressDialog(
    private val progressDialog: ProgressDialog?,
    private val weakActivity: WeakReference<Activity>
) {

    companion object {
        private const val MAX_PROGRESS = 100
        private const val MESSAGE_PREPARE_DOWNLOAD = "Preparing to download..."
        private const val MESSAGE_DOWNLOADING = "Downloading new version..."
    }

    init {
        initDialog()
    }

    private val downloadManager by lazy {
        weakActivity.get()?.let { ContextCompat.getSystemService(it, DownloadManager::class.java) }
    }

    private val downloadCompleteReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (downloadID == id) {
                setDialogPending()
                handleDownloadCompletion()
            }
        }
    }

    private var downloadID = -1L

    fun startDownload(
        apkUrl: String
    ) {
        showProgressDialog()
        val request = getDownloadRequest(apkUrl)

        weakActivity.get()?.registerReceiver(
            downloadCompleteReceiver,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )

        downloadID = downloadManager?.enqueue(request) ?: -1L
    }

    private fun handleDownloadCompletion() {
        val query = DownloadManager.Query()
        query.setFilterById(downloadID)

        var finishDownload = false
        while (!finishDownload) {
            val cursor = downloadManager?.query(query)

            val statusColumnIndex = cursor?.getColumnIndex(DownloadManager.COLUMN_STATUS) ?: -1
            val totalSizeColumnIndex =
                cursor?.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES) ?: -1
            val downloadedColumnIndex =
                cursor?.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR) ?: -1

            if (statusColumnIndex != -1) {
                if (cursor?.moveToFirst() == true) {
                    when (cursor.getInt(statusColumnIndex)) {
                        DownloadManager.STATUS_SUCCESSFUL -> {
                            progressDialog?.progress = MAX_PROGRESS
                            finishDownload = true
                            changeStyleAndHideProgressDialog()
                        }

                        DownloadManager.STATUS_FAILED -> {
                            finishDownload = true
                            changeStyleAndHideProgressDialog()
                        }

                        DownloadManager.STATUS_RUNNING -> {
                            if (totalSizeColumnIndex != -1 && downloadedColumnIndex != -1) {
                                val apkTotalSize = cursor.getLong(totalSizeColumnIndex)
                                val downloadedProgress = cursor.getLong(downloadedColumnIndex)

                                if (apkTotalSize >= 0) {
                                    val currentProgress =
                                        ((downloadedProgress * MAX_PROGRESS.toLong()) / apkTotalSize).toInt()

                                    progressDialog?.progress = currentProgress
                                }
                            }
                        }

                        else -> {
                            changeStyleAndHideProgressDialog()
                        }
                    }
                }
            }
            cursor?.close()
        }
    }

    private fun getDownloadRequest(
        apkUrl: String,
        authorization: String = ""
    ): DownloadManager.Request {
        val fileName = getFileNameFromUrl(apkUrl)

        return DownloadManager.Request(Uri.parse(apkUrl))
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setTitle(fileName)
//            .addRequestHeader(HEADER_AUTHORIZATION, authorization)
            .setAllowedOverRoaming(true)
            .setAllowedOverMetered(true)
            .setDescription(fileName)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
    }

    private fun getFileNameFromUrl(url: String): String {
        val path = URI(url).path
        return path.substring(path.lastIndexOf('/') + Int.ONE)
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
        progressDialog?.let {
            it.setMessage(MESSAGE_DOWNLOADING)
            it.isIndeterminate = false
            it.max = MAX_PROGRESS
            it.progress = 0
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
}
