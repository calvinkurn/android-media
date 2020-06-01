package com.tokopedia.home.topads

import android.app.Activity
import com.tokopedia.analyticsdebugger.database.TopAdsLogDB
import java.io.File

object TopAdsVerificationTestReportUtil {
    fun writeTopAdsVerificatorReportData(activity: Activity, topAdsLogDB: TopAdsLogDB) {
        val path = activity.getExternalFilesDir(null)
        val topadsVerificatorData = File(path, "topads_verificator_data")
        if (!topadsVerificatorData.exists()) {
            makeInitialReportDir(topadsVerificatorData)
        }
        val perfReportTopads = File(topadsVerificatorData, "report-topads-verificator-home.csv")
        perfReportTopads.appendText(
                "${topAdsLogDB.timestamp}," +
                        "${topAdsLogDB.sourceName}," +
                        "${topAdsLogDB.eventType}," +
                        "${topAdsLogDB.eventStatus}," +
                        "${topAdsLogDB.url}\n")
    }

    fun writeTopAdsVerificatorLog(activity: Activity, message: String) {
        val path = activity.getExternalFilesDir(null)
        val topadsVerificatorData = File(path, "topads_verificator_data")
        makeInitialLogDir(topadsVerificatorData)
        val logTopAdsFile = File(topadsVerificatorData, "report-topads-verificator-home-log.log")
        logTopAdsFile.appendText( "${message}\n")
    }

    fun deleteTopAdsVerificatorReportData(activity: Activity) {
        val path = activity.getExternalFilesDir(null)
        val topadsVerificatorData = File(path, "topads_verificator_data")
        topadsVerificatorData.delete()
    }

    private fun makeInitialLogDir(topadsDataDir: File) {
        if (!topadsDataDir.exists()) {
            topadsDataDir.mkdirs()
        }

        val perfReportPlt = File(topadsDataDir, "report-topads-verificator-home-log.log")
        perfReportPlt.appendText("")
    }

    private fun makeInitialReportDir(topadsDataDir: File) {
        val timestamp = "Timestamp"
        val sourceName = "Source Name"
        val eventType = "Type"
        val eventStatus = "Status"
        val url = "Url"

        topadsDataDir.mkdirs()
        val perfReportPlt = File(topadsDataDir, "report-topads-verificator-home.csv")
        perfReportPlt.appendText("" +
                "$timestamp," +
                "$sourceName," +
                "$eventType," +
                "$eventStatus," +
                "$url\n")
    }
}