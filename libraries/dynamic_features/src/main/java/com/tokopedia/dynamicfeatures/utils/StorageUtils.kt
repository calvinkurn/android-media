package com.tokopedia.dynamicfeatures.utils

import android.app.usage.StorageStatsManager
import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.os.storage.StorageManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

object StorageUtils {

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

    suspend fun getTotalInternalSpaceBytes(context: Context): Long {
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

    fun getInternalCacheSize(context: Context):Long {
        return try {
            var size: Long = 0
            context.cacheDir?.let {
                size = getFolderSize(it)
            }
            size
        } catch (e: Exception) {
            -1
        }
    }

    private fun getFolderSize(directory: File): Long {
        var length: Long = 0
        directory.listFiles()?.let {
            for (file in it) {
                length += if (file.isFile) {
                    file.length()
                } else {
                    getFolderSize(file)
                }
            }
        }
        return length
    }
}