package com.tokopedia.play.model

import com.google.gson.Gson
import com.tokopedia.play.data.ReportSummaries

/**
 * Created by jegul on 09/02/21
 */
class PlayResponseBuilder {

    private val gson = Gson()

    private val reportSummariesResponse = """
        {
            "broadcasterReportSummariesBulk": {
                "reportData": [{
                    "channel": {
                        "metrics": {
                            "totalLike": "1",
                            "totalLikeFmt": "1",
                            "visitChannel": "720",
                            "visitChannelFmt": "720"
                        }
                    }
                }]
            }
        }
    """.trimIndent()

    fun buildReportSummariesResponse(): ReportSummaries {
        return gson.fromJson(reportSummariesResponse, ReportSummaries.Response::class.java).reportSummaries
    }
}