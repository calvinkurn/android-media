package com.tokopedia.appdownloadmanager_common.domain.service

import android.app.DownloadManager
import android.net.Uri
import android.os.Environment
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.appdownloadmanager_common.presentation.model.DownloadingProgressUiModel
import com.tokopedia.appdownloadmanager_common.presentation.util.BaseDownloadManagerHelper.Companion.TKPD_DOWNLOAD_APK_DIR
import com.tokopedia.appdownloadmanager_common.presentation.util.BaseDownloadManagerHelper.Companion.TOKOPEDIA_APK_PATH
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File
import java.lang.Long.numberOfLeadingZeros
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DownloadManagerService @Inject constructor(
    private val downloadManager: DownloadManager?
) : CoroutineScope {

    companion object {
        private const val VERSION_PARAM = "versionname"
        private const val VERSION_CODE_PARAM = "versioncode"
        private const val APK_MIME_TYPE = "application/vnd.android.package-archive"
        private const val DELAY_UPDATE_PROGRESS = 300L
        private const val MAX_PROGRESS = 100
        private const val TWO_LAST_DIGIT = 2
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
                                            )
                                        )
                                        downloadManagerListener.onSuccessDownload(
                                            downloadingProgressUiModel,
                                            fileNamePath
                                        )

                                        deleteOldApks(fileNamePath)
                                    }

                                    DownloadManager.STATUS_FAILED, DownloadManager.ERROR_UNKNOWN,
                                    DownloadManager.ERROR_FILE_ALREADY_EXISTS, DownloadManager.ERROR_HTTP_DATA_ERROR,
                                    DownloadManager.ERROR_UNHANDLED_HTTP_CODE,
                                    DownloadManager.ERROR_TOO_MANY_REDIRECTS,
                                    DownloadManager.ERROR_INSUFFICIENT_SPACE,
                                    DownloadManager.ERROR_FILE_ERROR,
                                    DownloadManager.ERROR_DEVICE_NOT_FOUND,
                                    DownloadManager.ERROR_CANNOT_RESUME -> {
                                        finishDownload = true
                                        val reasonColumnIndex =
                                            cursor.getColumnIndex(DownloadManager.COLUMN_REASON)
                                        val reason = cursor.getString(reasonColumnIndex)
                                        downloadManagerListener.onFailedDownload(reason, statusColumnIndex)
                                    }

                                    DownloadManager.STATUS_RUNNING, DownloadManager.STATUS_PENDING -> {
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
                                                        )
                                                    )

                                                downloadManagerListener.onDownloading(
                                                    downloadingProgressUiModel
                                                )

                                                delay(DELAY_UPDATE_PROGRESS)
                                            }
                                        }
                                    }

                                    else -> {
                                        finishDownload = true
                                        val reasonColumnIndex =
                                            cursor.getColumnIndex(DownloadManager.COLUMN_REASON)
                                        val reason = cursor.getString(reasonColumnIndex)
                                        downloadManagerListener.onFailedDownload(reason, statusColumnIndex)
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
                job.cancel()
                downloadManagerListener.onFailedDownload(e.localizedMessage.orEmpty(), DownloadManager.ERROR_UNKNOWN)
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
        val versionCode = uri.getQueryParameter(VERSION_CODE_PARAM)?.takeLast(TWO_LAST_DIGIT).orEmpty()
        val versionName = uri.getQueryParameter(VERSION_PARAM)

        return "$versionName-$versionCode.apk"
    }

    private fun getDownloadRequest(
        apkUrl: String
    ): DownloadManager.Request {
        val fileName = getFileNameFromUrl(apkUrl)

        this.fileNamePath = "$TKPD_DOWNLOAD_APK_DIR/$fileName"

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

    private fun deleteOldApks(
        currentApkNamePath: String
    ) {
        val downloadDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), TOKOPEDIA_APK_PATH)

        val currentApk = File(currentApkNamePath).name

        if (downloadDir.exists()) {
            val files = downloadDir.listFiles()

            files?.forEach { file ->
                val fileName = file.name
                if (fileName.endsWith(".apk") && fileName != currentApk) {
                    file.delete()
                }
            }
        }
    }

    private fun convertToHumanReadableSize(bytes: Long): String {
        // If the size is less than 1024 bytes, return the size in bytes
        if (bytes < 1024) return "$bytes B"

        // Calculate the appropriate unit (KB, MB, GB, TB, PB, or EB) for better readability
        val z = (63 - numberOfLeadingZeros(bytes)) / 10
        // Format the result using the appropriate unit
        return String.format(Locale.getDefault(), "%.1f %sB", bytes.toDouble() / (1L shl (z * 10)), " KMGTPE"[z])
    }

    interface DownloadManagerListener {
        suspend fun onFailedDownload(reason: String, statusColumn: Int)

        suspend fun onDownloading(
            downloadingProgressUiModel: DownloadingProgressUiModel
        )

        suspend fun onSuccessDownload(
            downloadingProgressUiModel: DownloadingProgressUiModel,
            fileNamePath: String
        )
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default
}
