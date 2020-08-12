package com.tokopedia.home_wishlist.topads

import android.app.Activity
import com.tokopedia.analyticsdebugger.database.TopAdsLogDB
import java.io.File

object TopAdsVerificationTestReportUtil {
    private const val FILE_TOPADS_REPORT_ROOT = "topads_verificator_data"
    private const val FILE_TOPADS_LOG = "report-topads-verificator-home-log.log"
    private const val FILE_TOPADS_VERIFICATOR_DATA = "report-topads-verificator-home.csv"

    fun writeTopAdsVerificatorReportData(activity: Activity, topAdsLogDB: TopAdsLogDB) {
        val path = activity.getExternalFilesDir(null)
        val topadsVerificatorData = File(path, FILE_TOPADS_REPORT_ROOT)
        if (!topadsVerificatorData.exists()) {
            makeInitialReportDir(topadsVerificatorData)
        }
        val perfReportTopads = File(topadsVerificatorData, FILE_TOPADS_VERIFICATOR_DATA)
        perfReportTopads.appendText(
                "${topAdsLogDB.timestamp}," +
                        "${topAdsLogDB.sourceName}," +
                        "${topAdsLogDB.eventType}," +
                        "${topAdsLogDB.eventStatus}," +
                        "${topAdsLogDB.url}\n")
    }

    fun writeTopAdsVerificatorLog(activity: Activity, message: String) {
        val path = activity.getExternalFilesDir(null)
        val topadsVerificatorData = File(path, FILE_TOPADS_REPORT_ROOT)
        makeInitialLogDir(topadsVerificatorData)
        val logTopAdsFile = File(topadsVerificatorData, FILE_TOPADS_LOG)
        logTopAdsFile.appendText( "${message}\n")
    }

    fun deleteTopAdsVerificatorReportData(activity: Activity) {
        val path = activity.getExternalFilesDir(null)
        val topadsRootDir = File(path, FILE_TOPADS_REPORT_ROOT)
        val topadsLogData = File(topadsRootDir, FILE_TOPADS_LOG)
        val topadsVerificatorData = File(topadsRootDir, FILE_TOPADS_VERIFICATOR_DATA)

        topadsLogData.delete()
        topadsVerificatorData.delete()
        topadsRootDir.delete()

        topadsVerificatorData.delete()
    }

    private fun makeInitialLogDir(topadsDataDir: File) {
        if (!topadsDataDir.exists()) {
            topadsDataDir.mkdirs()
        }

        val perfReportPlt = File(topadsDataDir, FILE_TOPADS_LOG)
        perfReportPlt.appendText("")
    }


    private fun makeInitialReportDir(topadsDataDir: File) {
        val timestamp = "Timestamp"
        val sourceName = "Source Name"
        val eventType = "Type"
        val eventStatus = "Status"
        val url = "Url"

        topadsDataDir.mkdirs()
        val perfReportPlt = File(topadsDataDir, FILE_TOPADS_VERIFICATOR_DATA)
        perfReportPlt.appendText("" +
                "$timestamp," +
                "$sourceName," +
                "$eventType," +
                "$eventStatus," +
                "$url\n")
    }
}