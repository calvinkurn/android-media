package com.tokopedia.dynamicfeatures

import android.app.usage.StorageStatsManager
import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.os.storage.StorageManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.pm.PackageInfoCompat
import com.google.android.gms.common.GoogleApiAvailability
import kotlinx.coroutines.*
import timber.log.Timber
import java.io.File
import java.lang.Exception

/**
 * Created by hendry on 2019-10-03.
 */
object DFInstallerLogUtil {
    private const val MEGA_BYTE = 1024 * 1024
    private var storageStatsManager: StorageStatsManager? = null

    internal suspend fun getFreeSpaceBytes(context: Context): Long {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                withContext(Dispatchers.IO) {
                    val uuid = StorageManager.UUID_DEFAULT
                    getStorageStatsManager(context)?.getFreeBytes(uuid) ?: 0
                }
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 -> {
                val statFs = StatFs(Environment.getDataDirectory().absolutePath)
                statFs.availableBytes
            }
            else -> {
                File(context.filesDir.absoluteFile.toString()).freeSpace
            }
        }
    }

    private suspend fun getTotalInternalSpaceBytes(context: Context): Long {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                withContext(Dispatchers.IO) {
                    val uuid = StorageManager.UUID_DEFAULT
                    getStorageStatsManager(context)?.getTotalBytes(uuid) ?: 0
                }
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 -> {
                val statFs = StatFs(Environment.getDataDirectory().absolutePath)
                statFs.totalBytes
            }
            else -> {
                File(context.filesDir.absoluteFile.toString()).totalSpace
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getStorageStatsManager(context: Context): StorageStatsManager? {
        if (storageStatsManager == null) {
            storageStatsManager = ContextCompat.getSystemService(
                context,
                StorageStatsManager::class.java
            )
        }
        return storageStatsManager
    }

    internal fun logStatus(context: Context,
                           tag: String = "",
                           modulesName: String,
                           previousFreeSpace: Long = 0,
                           moduleSize: Long = 0,
                           errorCode: List<String>? = null,
                           downloadTimes: Int = 1,
                           isSuccess: Boolean = false) {
        GlobalScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, _ ->  }) {
            val messageStringBuilder = StringBuilder()
            messageStringBuilder.append("P1$tag{$modulesName};")
            messageStringBuilder.append("times_dl:{$downloadTimes};")

            if (errorCode?.isNotEmpty() == true) {
                messageStringBuilder.append("err:{${errorCode.joinToString("|")}};")
            }
            if (isSuccess) {
                messageStringBuilder.append("success;")
            }
            val phoneSize = getTotalInternalSpaceBytes(context)
            if (phoneSize > 0) {
                messageStringBuilder.append("phone:{")
                val phoneSizeInMB = String.format("%.2fMB", phoneSize.toDouble() / MEGA_BYTE)
                messageStringBuilder.append("$phoneSizeInMB};")
            }
            messageStringBuilder.append("free:{")
            if (previousFreeSpace > 0) {
                val previousFreeSpaceSizeInMB = String.format("%.2fMB", previousFreeSpace.toDouble() / MEGA_BYTE)
                messageStringBuilder.append(previousFreeSpaceSizeInMB)
            } else {
                messageStringBuilder.append("0")
            }
            try {
                val freeSpaceBytes = getFreeSpaceBytes(context)
                val totalFreeSpaceSizeInMB = String.format("%.2fMB", freeSpaceBytes.toDouble() / MEGA_BYTE)
                messageStringBuilder.append("|$totalFreeSpaceSizeInMB")
            } catch (ignored: Exception) {
            }
            messageStringBuilder.append("};")

            if (moduleSize > 0) {
                val moduleSizeInMB = String.format("%.2fMB", moduleSize.toDouble() / MEGA_BYTE)
                messageStringBuilder.append("size:{$moduleSizeInMB};")
            }
            try {
                val playServiceVersion = PackageInfoCompat.getLongVersionCode(
                    context.packageManager.getPackageInfo(GoogleApiAvailability.GOOGLE_PLAY_SERVICES_PACKAGE, 0)
                )
                messageStringBuilder.append("play_srv:{$playServiceVersion}")
            } catch (e: Exception) {
            }
            Timber.w(messageStringBuilder.toString())
        }
    }
}