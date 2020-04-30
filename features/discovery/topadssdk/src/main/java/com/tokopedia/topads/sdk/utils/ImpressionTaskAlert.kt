package com.tokopedia.entertainment.home.alert

import timber.log.Timber

/**
 * Author errysuprayogi on 30,March,2020
 */
class ImpressionTaskAlert(private val className: String) {
    private var lastImpression = 0L
    private var lastClick = 0L
    private val impressionTreshold = 500L
    private val clickTreshold = 1000L

    private fun trackClick() {
        val currentTime = System.currentTimeMillis()
        val timeSpan = currentTime - lastClick
        if (timeSpan < clickTreshold) {
            Timber.w("P2#TOPADS_TRACKING#Alert click;class=$className;diff_time=" + timeSpan)
        }
        lastClick = currentTime
    }

    private fun trackImpression() {
        val currentTime = System.currentTimeMillis()
        val timeSpan = currentTime - lastImpression
        if (timeSpan > impressionTreshold) {
            Timber.w("P2#TOPADS_TRACKING#Alert impression;class=$className;diff_time=" + timeSpan)
        }
        lastImpression = currentTime
    }

    fun track(url: String) {
        if (url.contains("view")) {
            trackImpression()
        } else if (url.contains("click")) {
            trackClick()
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