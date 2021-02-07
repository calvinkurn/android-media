package com.tokopedia.media.loader.tracker

import android.content.Context
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.media.common.data.MediaSettingPreferences
import com.tokopedia.media.loader.utils.AttributeUtils

object PerformanceTracker {

    private const val MEDIA_LOADER_TRACE = "mp_medialoader"
    private const val URL_PREFIX = "https://ecs7-p.tokopedia.net/img/cache/"

    fun track(url: String, context: Context): PerformanceMonitoring {
        val urlWithoutPrefix = url.removePrefix(URL_PREFIX)
        val mediaSetting = MediaSettingPreferences(context)
        val mediaSettingIndex = mediaSetting.qualitySettings()

        return PerformanceMonitoring.start(MEDIA_LOADER_TRACE).apply {
            putCustomAttribute("image_url", urlWithoutPrefix)
            putCustomAttribute("image_quality_setting", mediaSetting.getQualitySetting(mediaSettingIndex))
            putCustomAttribute("date_time", AttributeUtils.getDateTime())
        }
    }

    fun postRender(performanceMonitoring: PerformanceMonitoring?, loadTime: String, fileSize: String) {
        performanceMonitoring?.putCustomAttribute("load_time", loadTime)
        performanceMonitoring?.putCustomAttribute("file_size", fileSize)

        performanceMonitoring?.stopTrace()
    }

}