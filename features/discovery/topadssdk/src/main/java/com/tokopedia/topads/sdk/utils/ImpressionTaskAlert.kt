package com.tokopedia.entertainment.home.alert

import android.net.Uri
import timber.log.Timber

/**
 * Author errysuprayogi on 30,March,2020
 */
class ImpressionTaskAlert(private val className: String) {
    private var lastImpression = 0L
    private val impressionTreshold = 1000L
    private val SID = "sid"
    private val VIEWS = "views"
    private val CLICKS = "clicks"
    private val TOPADS_TRACKING = "TOPADS_TRACKING"
    private val TOPADS_HOST = "ta.tokopedia.com"

    private fun checkImpression(uri: Uri) {
        val currentTime = System.currentTimeMillis()
        val timeSpan = currentTime - lastImpression
        if (timeSpan < impressionTreshold && !uri.toString().contains(VIEWS)) {
            Timber.w("P2#$TOPADS_TRACKING#impression;host=${uri.host};class='$className';diff_time=$timeSpan")
        } else if (timeSpan < impressionTreshold && uri.toString().contains(CLICKS)) {
            Timber.w("P2#$TOPADS_TRACKING#click;host=${uri.host};class='$className';diff_time=$timeSpan")
        }
        lastImpression = currentTime
    }

    private fun checkParam(uri: Uri) {
        if (uri.getQueryParameter(SID).isNullOrBlank()){
            Timber.w("P2#$TOPADS_TRACKING#no_sid;host=${uri.host};class='$className'")
        }
    }

    fun track(url: String) {
        var uri = Uri.parse(url)
        if(uri.host.equals(TOPADS_HOST)) {
            checkImpression(uri)
            checkParam(uri)
        }
    }

    companion object {
        private val TAG = ImpressionTaskAlert::class.java.simpleName
        private var instance: ImpressionTaskAlert? = null
        @JvmStatic
        fun getInstance(className: String = ""): ImpressionTaskAlert? {
            if (instance == null) {
                instance = ImpressionTaskAlert(className)
            }
            return instance
        }
    }

}
