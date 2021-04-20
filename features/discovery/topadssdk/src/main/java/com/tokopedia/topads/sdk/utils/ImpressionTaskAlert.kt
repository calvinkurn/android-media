package com.tokopedia.topads.sdk.utils

import android.net.Uri
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority

/**
 * Author errysuprayogi on 30,March,2020
 */
class ImpressionTaskAlert {
    private var lastImpression = 0L
    private val impressionTreshold = 200L
    private val VIEWS = "views"
    private val CLICKS = "clicks"
    private val TOPADS_TRACKING = "TOPADS_TRACKING"
    private val TOPADS_HOST = "ta.tokopedia.com"

    private fun checkImpression(uri: Uri, fileName: String, methodName: String, lineNumber: Int) {
        val currentTime = System.currentTimeMillis()
        val timeSpan = currentTime - lastImpression
        if (timeSpan < impressionTreshold && uri.toString().contains(CLICKS)) {
            ServerLogger.log(Priority.P2, TOPADS_TRACKING, mapOf(
                    "type" to "impression",
                    "class" to fileName, "method" to methodName,
                    "line" to lineNumber.toString(), "diff_time" to timeSpan.toString(),
                    "url" to uri.toString()
            ))
        }
        lastImpression = currentTime
    }

    fun track(url: String, fileName: String, methodName: String, lineNumber: Int) {
        var uri = Uri.parse(url)
        if (uri.host.equals(TOPADS_HOST)) {
            checkImpression(uri, fileName, methodName, lineNumber)
        }
    }

    companion object {
        private val TAG = ImpressionTaskAlert::class.java.simpleName
        private var instance: ImpressionTaskAlert? = null

        @JvmStatic
        fun getInstance(className: String = ""): ImpressionTaskAlert? {
            if (instance == null) {
                instance = ImpressionTaskAlert()
            }
            return instance
        }
    }

}
