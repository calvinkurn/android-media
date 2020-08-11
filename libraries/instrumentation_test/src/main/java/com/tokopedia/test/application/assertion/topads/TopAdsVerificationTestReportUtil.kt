package com.tokopedia.test.application.assertion.topads

import android.content.Context
import com.tokopedia.analyticsdebugger.database.TopAdsLogDB
import java.io.File

object TopAdsVerificationTestReportUtil {
    private val FILE_TOPADS_REPORT_ROOT = "topads_verificator_data"
    private val FILE_TOPADS_LOG = "report-topads-verificator-log.log"
    private val FILE_TOPADS_VERIFICATOR_DATA = "report-topads-verificator.csv"

    fun writeTopAdsVerificatorReportData(context: Context, topAdsLogDB: TopAdsLogDB) {
        val path = context.getExternalFilesDir(null)
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

    fun writeTopAdsVerificatorLog(context: Context, message: String) {
        val path = context.getExternalFilesDir(null)
        val topadsVerificatorData = File(path, FILE_TOPADS_REPORT_ROOT)
        makeInitialLogDir(topadsVerificatorData)
        val logTopAdsFile = File(topadsVerificatorData, FILE_TOPADS_LOG)
        logTopAdsFile.appendText( "${message}\n")
    }

    fun deleteTopAdsVerificatorReportData(context: Context) {
        val path = context.getExternalFilesDir(null)
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