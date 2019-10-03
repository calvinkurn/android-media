package com.tokopedia.dynamicfeatures

import android.content.Context
import timber.log.Timber
import java.io.File

/**
 * Created by hendry on 2019-10-03.
 */
object DFInstallerLogUtil {
    private const val MEGA_BYTE = 1024 * 1024

    internal fun logStatus(context: Context,
                           tag: String = "",
                           modulesName: String,
                           moduleSize: Long = 0,
                           errorCode: List<String>? = null,
                           downloadTimes: Int = 1,
                           isSuccess: Boolean = false) {
        val messageStringBuilder = StringBuilder()
        messageStringBuilder.append("P1$tag {$modulesName}; times_dl: {$downloadTimes};")

        val totalSize = File(context.filesDir.absoluteFile.toString()).freeSpace.toDouble()
        val totalFreeSpaceSizeInMB = String.format("%.2fMB", totalSize / MEGA_BYTE)
        messageStringBuilder.append("free: {$totalFreeSpaceSizeInMB}; ")

        if (moduleSize > 0) {
            val moduleSizeInMB = String.format("%.2fMB", moduleSize.toDouble() / MEGA_BYTE)
            messageStringBuilder.append("size: {$moduleSizeInMB}; ")
        }
        if (errorCode?.isNotEmpty() == true) {
            messageStringBuilder.append("err: {${errorCode.joinToString("-")}}; ")
        }
        if (isSuccess) {
            messageStringBuilder.append("Success; ")
        }
        Timber.w(messageStringBuilder.toString())
    }
}