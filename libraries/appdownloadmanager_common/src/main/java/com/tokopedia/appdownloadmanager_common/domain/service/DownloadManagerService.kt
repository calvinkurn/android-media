package com.tokopedia.appdownloadmanager_common.domain.service

import android.app.DownloadManager
import android.net.Uri
import android.os.Environment
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.appdownloadmanager_common.presentation.model.DownloadingProgressUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File
import java.lang.Long.numberOfLeadingZeros
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DownloadManagerService @Inject constructor(
    private val downloadManager: DownloadManager?
) : CoroutineScope {

    companion object {
        private const val VERSION_PARAM = "version"
        private const val APK_MIME_TYPE = "application/vnd.android.package-archive"
        private const val DELAY_UPDATE_PROGRESS = 300L
        private const val MAX_PROGRESS = 100
    }

    private val job = SupervisorJob()

    private var fileNamePath = ""
    private var downloadID = -1L

    fun startDownload(
        apkUrl: String,
        downloadManagerListener: DownloadManagerListener
    ) {

        val request = getDownloadRequest(apkUrl)

        downloadID = downloadManager?.enqueue(request) ?: -1L

        handleDownloadCompletion(downloadManagerListener)
    }

    private fun handleDownloadCompletion(downloadManagerListener: DownloadManagerListener) {
        var finishDownload = false

        launch {
            try {

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
                            cursor?.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
                                ?: -1

                        if (statusColumnIndex != -1) {
                            if (cursor?.moveToFirst() == true) {
                                when (cursor.getInt(statusColumnIndex)) {
                                    DownloadManager.STATUS_SUCCESSFUL -> {
                                        finishDownload = true
                                        val apkTotalSize = cursor.getLong(totalSizeColumnIndex)
                                        val downloadedProgress =
                                            cursor.getLong(downloadedColumnIndex)

                                        val downloadingProgressUiModel = DownloadingProgressUiModel(
                                            currentProgressInPercent = MAX_PROGRESS,
                                            currentDownloadedSize = convertToHumanReadableSize(
                                                downloadedProgress
                                            ),
                                            totalResourceSize = convertToHumanReadableSize(
                                                apkTotalSize
                                            ),
                                            isFinishedDownloading = true
                                        )
                                        downloadManagerListener.onSuccessDownload(
                                            downloadingProgressUiModel,
                                            fileNamePath
                                        )
                                    }

                                    DownloadManager.STATUS_FAILED -> {
                                        finishDownload = true
                                        downloadManagerListener.onFailedDownload()
                                    }

                                    DownloadManager.STATUS_RUNNING -> {
                                        if (totalSizeColumnIndex != -1 && downloadedColumnIndex != -1) {
                                            val apkTotalSize = cursor.getLong(totalSizeColumnIndex)
                                            val downloadedProgress =
                                                cursor.getLong(downloadedColumnIndex)

                                            if (apkTotalSize > 0) {
                                                val currentProgress =
                                                    ((downloadedProgress * MAX_PROGRESS.toLong()) / apkTotalSize).toInt()

                                                val downloadingProgressUiModel =
                                                    DownloadingProgressUiModel(
                                                        currentProgressInPercent = currentProgress,
                                                        currentDownloadedSize = convertToHumanReadableSize(
                                                            downloadedProgress
                                                        ),
                                                        totalResourceSize = convertToHumanReadableSize(
                                                            apkTotalSize
                                                        ),
                                                        isFinishedDownloading = false
                                                    )

                                                downloadManagerListener.onDownloading(
                                                    downloadingProgressUiModel,
                                                )

                                                delay(DELAY_UPDATE_PROGRESS)
                                            }
                                        }
                                    }

                                    else -> {
                                        // no op
                                        finishDownload = true
                                        downloadManagerListener.onFailedDownload()
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
            } catch (e: Exception) {
                FirebaseCrashlytics.getInstance().recordException(e)
                downloadManagerListener.onFailedDownload()
            }
        }
    }

    fun cancelDownload() {
        try {
            downloadManager?.remove(downloadID)
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

    private fun getFileNameFromUrl(url: String): String {
        val uri = Uri.parse(url)
        return "${uri.getQueryParameter(VERSION_PARAM).orEmpty()}.apk"
    }

    private fun getDownloadRequest(
        apkUrl: String
    ): DownloadManager.Request {
        val fileName = getFileNameFromUrl(apkUrl)

        this.fileNamePath =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .toString() + "/$fileName"

        val file = File(fileNamePath)

        if (file.exists()) {
            file.delete()
        }

        val uri = Uri.fromFile(file)

        return DownloadManager.Request(Uri.parse(apkUrl)).setDestinationUri(uri)
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setTitle(fileName).setAllowedOverRoaming(true).setAllowedOverMetered(true)
            .setDescription(fileName)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setMimeType(APK_MIME_TYPE)
    }

    private fun convertToHumanReadableSize(bytes: Long): String {
        if (bytes < 1024) return "$bytes B"
        val z = (63 - numberOfLeadingZeros(bytes)) / 10
        return String.format("%.1f %sB", bytes.toDouble() / (1L shl (z * 10)), " KMGTPE"[z])
    }

    interface DownloadManagerListener {

        suspend fun onSuccessDownload(
            downloadingProgressUiModel: DownloadingProgressUiModel,
            fileNamePath: String
        )
        suspend fun onFailedDownload()

        suspend fun onDownloading(
            downloadingProgressUiModel: DownloadingProgressUiModel,
        )
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default

}
