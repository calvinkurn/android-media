package com.tokopedia.dynamicfeatures

import android.content.Context
import androidx.core.content.pm.PackageInfoCompat
import com.google.android.gms.common.GoogleApiAvailability
import timber.log.Timber
import java.io.File
import java.lang.Exception

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
        messageStringBuilder.append("$tag{$modulesName};")
        messageStringBuilder.append("times_dl:{$downloadTimes};")

        if (errorCode?.isNotEmpty() == true) {
            messageStringBuilder.append("err:{${errorCode.joinToString("|")}};")
        }
        if (isSuccess) {
            messageStringBuilder.append("success;")
        }

        try {
            val totalSize = File(context.filesDir.absoluteFile.toString()).freeSpace.toDouble()
            val totalFreeSpaceSizeInMB = String.format("%.2fMB", totalSize / MEGA_BYTE)
            messageStringBuilder.append("free:{$totalFreeSpaceSizeInMB};")
        } catch (ignored: Exception) { }

        if (moduleSize > 0) {
            val moduleSizeInMB = String.format("%.2fMB", moduleSize.toDouble() / MEGA_BYTE)
            messageStringBuilder.append("size:{$moduleSizeInMB};")
        }
        try {
            val playServiceVersion = PackageInfoCompat.getLongVersionCode(
                    context.packageManager.getPackageInfo(GoogleApiAvailability.GOOGLE_PLAY_SERVICES_PACKAGE, 0)
            )
            messageStringBuilder.append("play_srv:{$playServiceVersion}")
        } catch (e: Exception) { }
        Timber.w("P1#DFM#$messageStringBuilder")
    }
}