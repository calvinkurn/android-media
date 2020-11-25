package com.tokopedia.inappupdate

import android.content.Context
import android.content.pm.PackageManager
import timber.log.Timber
import java.util.*

/**
 * Created by hendry on 2019-10-03.
 */
object InAppUpdateLogUtil {

    private const val ONE_KB = 1024L
    private const val MEGA_BYTE = ONE_KB * ONE_KB

    private var LOG_UPDATE_STATUS_OK = "ok"
    private var LOG_UPDATE_STATUS_ERROR = "err"

    fun logStatusCheck(context: Context, status: String, availableVersionCode: Int?, updateAvailability: Int?,
                       updateAllowed: Boolean?, clientVersionStalenessDays: Int?, updatePriority:Int?, totalBytesToDownload: Long?) {
        val messageBuilder = StringBuilder()
        messageBuilder.append(status)
        messageBuilder.append(";avail_ver_code=$availableVersionCode")
        messageBuilder.append(";update_availability=$updateAvailability")
        messageBuilder.append(";update_allowed=$updateAllowed")
        messageBuilder.append(";staleness_days=$clientVersionStalenessDays")
        messageBuilder.append(";update_prio=$updatePriority")
        messageBuilder.append(";dl_size=${getSizeInMB(totalBytesToDownload ?: 0)}")
        messageBuilder.append(";installer_pkg=${getInstallerPackageName(context)}")
        Timber.w("P1#IN_APP_UPDATE_CHECK#$messageBuilder")
    }

    fun logStatusDownload(type:String, value: String) {
        Timber.w("P1#IN_APP_UPDATE_STAT#$type;status=$LOG_UPDATE_STATUS_OK;value='$value'")
    }

    fun logStatusFailure(type:String, err: String) {
        Timber.w("P1#IN_APP_UPDATE_STAT#$type;status=$LOG_UPDATE_STATUS_ERROR;value='$err'")
    }

    private fun getInstallerPackageName(context: Context):String {
        return try {
            val pm: PackageManager = context.packageManager
            pm.getInstallerPackageName(context.packageName)
        } catch (e: Exception) {
            "-"
        }
    }

    private fun getFormattedNumber(number: Float, format: String = "%.2f", locale: Locale = Locale.ENGLISH) : String {
        return String.format(locale, format, number)
    }

    private fun getSizeInMB(size: Long): String {
        return getFormattedNumber(size.toFloat() / MEGA_BYTE)
    }
}