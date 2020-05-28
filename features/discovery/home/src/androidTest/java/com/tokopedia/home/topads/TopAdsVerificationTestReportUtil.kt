package com.tokopedia.home.topads

import android.app.Activity
import com.tokopedia.analyticsdebugger.database.TopAdsLogDB
import java.io.File

object TopAdsVerificationTestReportUtil {
    fun writeTopAdsVerificatorReportData(activity: Activity, topAdsLogDB: TopAdsLogDB) {
        val path = activity.getExternalFilesDir(null)
        val topadsVerificatorData = File(path, "topads_verificator_data")
        if (!topadsVerificatorData.exists()) {
            makeInitialPerfDir(topadsVerificatorData)
        }
        val perfReportTopads = File(topadsVerificatorData, "report-topads-verificator-home.csv")
        perfReportTopads.appendText(
                "${topAdsLogDB.timestamp}," +
                        "${topAdsLogDB.sourceName}," +
                        "${topAdsLogDB.eventType}," +
                        "${topAdsLogDB.eventStatus}," +
                        "${topAdsLogDB.url}\n")
    }

    fun deleteTopAdsVerificatorReportData(activity: Activity) {
        val path = activity.getExternalFilesDir(null)
        val topadsVerificatorData = File(path, "topads_verificator_data")
        topadsVerificatorData.delete()
    }

    private fun makeInitialPerfDir(topadsDataDir: File) {
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