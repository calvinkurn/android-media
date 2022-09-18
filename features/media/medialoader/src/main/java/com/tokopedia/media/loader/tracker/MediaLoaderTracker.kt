package com.tokopedia.media.loader.tracker

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import com.tokopedia.kotlin.extensions.view.formattedToMB
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.media.common.data.MediaSettingPreferences
import com.tokopedia.media.common.util.NetworkManager
import com.tokopedia.media.loader.utils.ServerIpAddressLocator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

data class MediaLoaderTrackerParam(
    val url: String,
    val pageName: String,
    val loadTime: String,
    val fileSize: String
)

object MediaLoaderTracker {

    private const val CDN_URL = "https://images.tokopedia.net/img/"
    private const val TAG = "MEDIALOADER_ANALYTIC"
    private const val TAG_CDN_MONITORING = "DEV_CDN_MONITORING"

    private const val PAGE_NAME_NOT_FOUND = "None"

    private fun priority() = Priority.P1

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

    fun trackCdnDown(
        context: Context,
        url: String,
        loadTime: String = ""
    ) {

        val pageName = try {
            context.javaClass.name.split(".").last()
        } catch (e: Throwable) {
            PAGE_NAME_NOT_FOUND
        }

        val data = MediaLoaderTrackerParam(
            url = url,
            pageName = pageName,
            loadTime = loadTime,
            fileSize = "n/a" // as this is for failed case, then size will not available.
        )

        if (!data.url.contains(CDN_URL)) return

        val map = data.toMap(context.applicationContext).toMutableMap()

        GlobalScope.launch(Dispatchers.IO) {
            val ipInfo: String = try {
                ServerIpAddressLocator.fetchServerInfo(url).hostAddress
            } catch (exp: Exception) {
                "not available"
            }

            map["remote_server_ip"] = ipInfo

            Log.d("Lavekush", "test " + ipInfo)
            Toast.makeText(context, "$ipInfo URL is $url", Toast.LENGTH_LONG).show()

            ServerLogger.log(
                priority = priority(),
                tag = TAG_CDN_MONITORING,
                message = map
            )
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

data class IsIcon(val value: Boolean)