package com.tokopedia.dynamicfeatures

import android.app.usage.StorageStatsManager
import android.content.Context
import android.content.pm.PackageManager
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

/**
 * Created by hendry on 2019-10-03.
 */
object DFInstallerLogUtil {
    private const val DFM_TAG = "DFM"
    private const val PLAY_STORE_PACKAGE_NAME = "com.android.vending"
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
                           message: String = "",
                           modulesName: String,
                           previousFreeSpace: Long = 0,
                           moduleSize: Long = 0,
                           errorList: List<String> = emptyList(),
                           downloadTimes: Int = 1,
                           isSuccess: Boolean = false,
                           tag: String = DFM_TAG) {

        GlobalScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, _ ->  }) {
            val messageBuilder = StringBuilder()
            messageBuilder.append(message)

            messageBuilder.append(";mod_name=$modulesName")

            messageBuilder.append(";success=$isSuccess")

            messageBuilder.append(";dl_times=$downloadTimes")

            messageBuilder.append(";err='${getError(errorList)}'")

            messageBuilder.append(";mod_size=")
            if (moduleSize > 0) {
                messageBuilder.append(getSizeInMB(moduleSize))
            } else {
                messageBuilder.append(-1)
            }

            messageBuilder.append(";phone_size=")
            val phoneSize = getTotalInternalSpaceBytes(context)
            if (phoneSize > 0) {
                messageBuilder.append(getSizeInMB(phoneSize))
            } else {
                messageBuilder.append(-1)
            }

            messageBuilder.append(";free_bef=")
            if (previousFreeSpace > 0) {
                messageBuilder.append(getSizeInMB(previousFreeSpace))
            } else {
                messageBuilder.append(-1)
            }

            messageBuilder.append(";free_aft=")
            try {
                messageBuilder.append(getSizeInMB(getFreeSpaceBytes(context)))
            } catch (ignored: Exception) {
                messageBuilder.append(-1)
            }

            messageBuilder.append(";play_str='${getPlayStoreVersionName(context)}'")
            messageBuilder.append(";play_str_l=${getPlayStoreLongVersionCode(context)}")
            messageBuilder.append(";play_srv=${getPlayServiceLongVersionCode(context)}")
            messageBuilder.append(";installer_pkg=${getInstallerPackageName(context)}")

            Timber.w("P1#$tag#$messageBuilder")
        }
    }

    private fun getSizeInMB(size: Long) : String {
        return String.format("%.2f", size.toDouble() / MEGA_BYTE)
    }

    private fun getError(errorList: List<String>):String {
        if (errorList.isEmpty()) {
            return "0"
        }
        var errorText = errorList.first()
        for (error in errorList) if (error != errorText) {
            errorText = errorList.joinToString("|")
            break
        }
        return errorText
    }

    private fun getPlayStoreVersionName(context: Context):String {
        return try {
            val pm: PackageManager = context.packageManager
            val playStoreInfo = pm.getInstalledPackages(PackageManager.GET_META_DATA).first {
                it.packageName == PLAY_STORE_PACKAGE_NAME
            }
            playStoreInfo.versionName
        } catch (e: Exception) {
            "-1"
        }
    }

    private fun getPlayStoreLongVersionCode(context: Context):Long {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) return -1
        return try {
            val pm: PackageManager = context.packageManager
            val playStoreInfo = pm.getInstalledPackages(PackageManager.GET_META_DATA).first {
                it.packageName == PLAY_STORE_PACKAGE_NAME
            }
            playStoreInfo.longVersionCode
        } catch (e: Exception) {
            -1
        }
    }

    private fun getPlayServiceLongVersionCode(context: Context):Long {
        return try {
            val pm: PackageManager = context.packageManager
            PackageInfoCompat.getLongVersionCode(pm.getPackageInfo(GoogleApiAvailability.GOOGLE_PLAY_SERVICES_PACKAGE, 0))
        } catch (e: Exception) {
            -1
        }
    }

    private fun getInstallerPackageName(context: Context):String {
        return try {
            val pm: PackageManager = context.packageManager
            pm.getInstallerPackageName(context.packageName)
        } catch (e: Exception) {
            "-"
        }
    }
}