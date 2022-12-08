package com.tokopedia.media.loader.tracker

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.load.engine.GlideException
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.formattedToMB
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.media.common.data.MediaSettingPreferences
import com.tokopedia.media.common.util.NetworkManager
import com.tokopedia.media.loader.utils.RemoteCdnService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

data class MediaLoaderTrackerParam(
    val url: String,
    val pageName: String,
    val loadTime: String,
    val fileSize: String
)

object MediaLoaderTracker : CoroutineScope {

    private const val CDN_URL = "https://images.tokopedia.net/img/"
    private const val TAG = "MEDIALOADER_ANALYTIC"
    private const val TAG_CDN_MONITORING = "DEV_CDN_MONITORING"

    private const val PAGE_NAME_NOT_FOUND = "None"
    private const val NOT_AVAILABLE = "not available"
    private const val CDN_IP_MAP_KEY = "remote_server_ip"
    private const val CDN_HOST_NAME_MAP_KEY = "remote_host_name"
    private const val CDN_NAME_KEY = "remote_cdn_name"
    private const val CDN_ERROR_DETAIL = "error_detail"
    private const val CDN_IMG_SIZE_NOT_AVAILBLE = "n/a"

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.IO

    private fun priority() = Priority.P2

    private fun tag() = TAG

    fun simpleTrack(
        context: Context,
        url: String,
        isIcon: IsIcon = IsIcon(false),
        bitmap: Bitmap? = null,
        loadTime: String = ""
    ) {
        if (isIcon.value) return

        val pageName = try {
            context.javaClass.name.split(".").last()
        } catch (e: Throwable) {
            PAGE_NAME_NOT_FOUND
        }

        val fileSize = bitmap?.allocationByteCount?.toString() ?: "0"
        val fileSizeInMb = fileSize.toLong().formattedToMB()

        // tracker
        track(
            context = context.applicationContext,
            data = MediaLoaderTrackerParam(
                url = url,
                pageName = pageName,
                loadTime = loadTime,
                fileSize = fileSizeInMb
            )
        )
    }

    fun track(context: Context, data: MediaLoaderTrackerParam) {
        if (!data.url.contains(CDN_URL)) return

        ServerLogger.log(
            priority = priority(),
            tag = tag(),
            message = data.toMap(context)
        )
    }

    fun trackLoadFailed(
        context: Context,
        url: String,
        loadTime: String = "",
        exception: GlideException?
    ) {
        if (!RemoteCdnService.isValidUrl(url)) return

        val pageName = try {
            context.javaClass.name.split(".").last()
        } catch (e: Throwable) {
            PAGE_NAME_NOT_FOUND
        }

        val data = MediaLoaderTrackerParam(
            url = url,
            pageName = pageName,
            loadTime = loadTime,
            fileSize = CDN_IMG_SIZE_NOT_AVAILBLE // as this is for failed case, then size will not available.
        )

        val map = data.toMap(context.applicationContext).toMutableMap()

        launchCatchError(block = {
            var ipInfo = NOT_AVAILABLE
            var hostName = NOT_AVAILABLE
            var cdnName = ""
            try {
                val remoteInfo = RemoteCdnService.fetchServerInfo(url)
                cdnName = RemoteCdnService.getCdnName(url)
                ipInfo = remoteInfo.hostAddress
                hostName = remoteInfo.hostName
            } catch (ignored: Exception) { /* no-op */ }

            map[CDN_IP_MAP_KEY] = ipInfo
            map[CDN_HOST_NAME_MAP_KEY] = hostName
            map[CDN_NAME_KEY] = cdnName
            map[CDN_ERROR_DETAIL] = "localizedMessage=${exception?.localizedMessage}, cause=${exception?.cause}, rootCauses=${exception?.rootCauses}"

            ServerLogger.log(
                priority = Priority.P1,
                tag = TAG_CDN_MONITORING,
                message = map
            )
        }, onError = {})
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

data class IsIcon(val value: Boolean)
