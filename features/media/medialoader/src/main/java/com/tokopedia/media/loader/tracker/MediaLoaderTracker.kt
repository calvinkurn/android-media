package com.tokopedia.media.loader.tracker

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.load.engine.GlideException
import com.tokopedia.dev_monitoring_tools.userjourney.UserJourney
import com.tokopedia.kotlin.extensions.view.formattedToMB
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.media.loader.internal.MediaSettingPreferences
import com.tokopedia.media.loader.internal.NetworkManager
import com.tokopedia.media.loader.utils.RemoteCdnUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

internal object MediaLoaderTracker : CoroutineScope {

    private const val CDN_URL = "https://images.tokopedia.net/img/"
    private const val TAG_ANALYTIC = "MEDIALOADER_ANALYTIC"
    private const val TAG_CDN_MONITORING = "DEV_CDN_MONITORING"

    private const val PAGE_NAME_NOT_FOUND = "None"
    private const val NOT_AVAILABLE = "not available"
    private const val CDN_IP_MAP_KEY = "remote_server_ip"
    private const val CDN_HOST_NAME_MAP_KEY = "remote_host_name"
    private const val CDN_NAME_KEY = "remote_cdn_name"
    private const val CDN_ERROR_DETAIL = "error_detail"
    private const val CDN_PAGE_SOURCE_KEY = "page_source"
    private const val CDN_JOURNEY_KEY = "journey"
    private const val CDN_IMG_SIZE_NOT_AVAILABLE = "n/a"

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.IO

    fun succeed(
        context: Context,
        url: String,
        isIcon: IsIcon = IsIcon(false),
        bitmap: Bitmap? = null,
        loadTime: String = ""
    ) {
        if (isIcon.value) return
        if (!url.contains(CDN_URL)) return

        val fileSize = bitmap?.allocationByteCount?.toString() ?: "0"
        val fileSizeInMb = fileSize.toLong().formattedToMB()
        val pageName = context.pageName()

        // tracker
        ServerLogger.log(
            priority = Priority.P2,
            tag = TAG_ANALYTIC,
            message = MediaLoaderTrackerParam(
                url = url,
                pageName = pageName,
                loadTime = loadTime,
                fileSize = fileSizeInMb
            ).toMap(context.applicationContext)
        )
    }

    fun failed(
        context: Context,
        url: String,
        loadTime: String = "",
        exception: GlideException?
    ) {
        if (!RemoteCdnUtil.isValidUrl(url)) return
        val pageName = context.pageName()

        val data = MediaLoaderTrackerParam(
            url = url,
            pageName = pageName,
            loadTime = loadTime,
            fileSize = CDN_IMG_SIZE_NOT_AVAILABLE // as this is for failed case, then size will not available.
        )

        val map = data
            .toMap(context.applicationContext)
            .toMutableMap()

        launch {
            var ipInfo = NOT_AVAILABLE
            var hostName = NOT_AVAILABLE
            var cdnName = ""

            try {
                val remoteInfo = RemoteCdnUtil.fetchServerInfo(url)
                cdnName = RemoteCdnUtil.getCdnName(url)
                ipInfo = remoteInfo.hostAddress ?: ""
                hostName = remoteInfo.hostName
            } catch (ignored: Exception) {}  // no-op

            withContext(Dispatchers.Main) {
                map[CDN_IP_MAP_KEY] = ipInfo
                map[CDN_HOST_NAME_MAP_KEY] = hostName
                map[CDN_NAME_KEY] = cdnName
                map[CDN_PAGE_SOURCE_KEY] = getCdnPageSource(context)
                map[CDN_JOURNEY_KEY] = UserJourney.getReadableJourneyActivity()
                map[CDN_ERROR_DETAIL] = """
                    localizedMessage=${exception?.localizedMessage}, 
                    cause=${exception?.cause}, 
                    rootCauses=${exception?.rootCauses}
                """.replace("\n\\s+".toRegex(), "").trimIndent()

                ServerLogger.log(
                    priority = Priority.P1,
                    tag = TAG_CDN_MONITORING,
                    message = map
                )
            }
        }
    }

    private fun getCdnPageSource(context: Context): String {
        return context.javaClass.canonicalName.orEmpty()
    }

    private fun Context.pageName(): String {
        return try {
            javaClass.name.split(".").last()
        } catch (ignored: Throwable) {
            PAGE_NAME_NOT_FOUND
        }
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
        return when (index) {
            0 -> "Automatic"
            1 -> "Low"
            2 -> "High"
            else -> "Unknown"
        }
    }
}

internal data class IsIcon(val value: Boolean)

internal data class MediaLoaderTrackerParam(
    val url: String,
    val pageName: String,
    val loadTime: String,
    val fileSize: String
)
