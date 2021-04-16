package com.tokopedia.inappupdate

import android.content.Context
import android.content.pm.PackageManager
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
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
        ServerLogger.log(Priority.P1, "IN_APP_UPDATE_CHECK", mapOf("type" to status,
                "avail_ver_code" to availableVersionCode.toString(),
                "update_availability" to updateAvailability?.toString().orEmpty(),
                "update_allowed" to updateAllowed?.toString().orEmpty(),
                "staleness_days" to clientVersionStalenessDays?.toString().orEmpty(),
                "update_prio" to updatePriority?.toString().orEmpty(),
                "dl_size" to getSizeInMB(totalBytesToDownload ?: 0),
                "installer_pkg" to getInstallerPackageName(context)
        ))
    }

    fun logStatusDownload(type:String, value: String) {
        ServerLogger.log(Priority.P1, "IN_APP_UPDATE_STAT", mapOf("type" to type, "status" to LOG_UPDATE_STATUS_OK, "value" to value))
    }

    fun logStatusFailure(type:String, err: String) {
        ServerLogger.log(Priority.P1, "IN_APP_UPDATE_STAT", mapOf("type" to type, "status" to LOG_UPDATE_STATUS_ERROR, "value" to err))
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