package com.tokopedia.media.loader.tracker

import android.content.Context
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.media.common.data.MediaSettingPreferences

object PerformanceTracker {

    private const val MEDIA_LOADER_TRACE = "mp_medialoader"
    private const val CDN_URL_PREFIX = "https://images.tokopedia.net/img/cache/"

    private const val IMAGE_URL = "image_url"
    private const val QUALITY_SETTING = "image_quality_setting"
    private const val PAGE_NAME = "page_name"
    private const val LOAD_TIME = "load_time"
    private const val FILE_SIZE = "file_size"

    fun preRender(url: String, context: Context): PerformanceMonitoring {
        val urlWithoutPrefix = url.removePrefix(CDN_URL_PREFIX)

        val mediaSetting = MediaSettingPreferences(context)
        val mediaSettingIndex = mediaSetting.qualitySettings()

        return PerformanceMonitoring.start(MEDIA_LOADER_TRACE).apply {
            putCustomAttribute(IMAGE_URL, urlWithoutPrefix)
            putCustomAttribute(QUALITY_SETTING, mediaSetting.getQualitySetting(mediaSettingIndex))
        }
    }

    fun postRender(
            performanceMonitoring: PerformanceMonitoring?,
            pageName: String,
            loadTime: String,
            fileSize: String
    ) {
        performanceMonitoring?.putCustomAttribute(PAGE_NAME, pageName)
        performanceMonitoring?.putCustomAttribute(LOAD_TIME, loadTime)
        performanceMonitoring?.putCustomAttribute(FILE_SIZE, fileSize)

        performanceMonitoring?.stopTrace()
    }

}