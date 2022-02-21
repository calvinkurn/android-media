package com.tokopedia.media.loader.tracker

import android.content.Context
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.media.common.data.MediaSettingPreferences
import com.tokopedia.media.common.util.NetworkManager

data class MediaLoaderTrackerParam(
    val url: String,
    val pageName: String,
    val loadTime: String,
    val fileSize: String
)

object MediaLoaderTracker {

    private const val CDN_URL = "https://images.tokopedia.net/img/"
    private const val TAG = "MEDIALOADER_ANALYTIC"

    private fun priority() = Priority.P2

    private fun tag() = TAG

    fun track(context: Context, data: MediaLoaderTrackerParam) {
        if (!data.url.contains(CDN_URL)) return

        ServerLogger.log(
            priority = priority(),
            tag = tag(),
            message = data.toMap(context)
        )
    }

    private fun MediaLoaderTrackerParam.toMap(context: Context): Map<String, String> {
        val mediaSetting = MediaSettingPreferences(context)
        val mediaSettingIndex = mediaSetting.qualitySettings()
        val qualitySetting = getQualitySetting(mediaSettingIndex)

        val connectionType = NetworkManager.getConnectionType(context)

        return mapOf(
            "image_url" to url,
            "image_quality_setting" to qualitySetting,
            "page_name" to pageName,
            "connection_type" to connectionType,
            "load_time" to loadTime,
            "file_size" to fileSize
        )
    }

    private fun getQualitySetting(index: Int): String {
        return when(index) {
            0 -> "Automatic"
            1 -> "Low"
            2 -> "High"
            else -> "Unknown"
        }
    }

}