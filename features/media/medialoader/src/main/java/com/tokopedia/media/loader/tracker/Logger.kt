package com.tokopedia.media.loader.tracker

import android.content.Context
import android.graphics.Bitmap
import com.google.gson.Gson
import com.tokopedia.config.GlobalConfig
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.media.loader.internal.MediaSettingPreferences
import com.tokopedia.media.loader.internal.NetworkManager
import com.tokopedia.media.loader.utils.isFromInternalCdnImageUrl
import timber.log.Timber

object Logger {

    // Please order based on priorities.
    private val tags = mapOf(
        /**
         * This tag comes from core team, which they want to track the failure callback from Glide.
         * As of now, the tag name is ambiguity, thus we have to migrate it a proper name next cycle.
         */
        Type.Failure to Pair("DEV_CDN_MONITORING", Priority.P1),

        /**
         * This is the enhancement of previous tag, which "MEDIALOADER_ANALYTIC". The previous tag
         * not comes from us, hence we need to ensure our data streamlined dedicated to Media team.
         * Previous tag comes from New Relic team, which not fit for us.
         *
         * Previous newrelic tag will be deprecated ("MEDIALOADER_ANALYTIC") next cycle.
         */
        Type.Request to Pair("MEDIALOADER_REQUEST", Priority.P2)
    )

    fun request(context: Context, param: Param.() -> Unit) {
        val mParam = Param().apply(param)
        request(context, mParam)
    }

    private fun request(context: Context, param: Param) {
        val (tag, priority) = tags[Type.Request] ?: return
        if (param.shouldEligible().not()) return

        val activityName = context.pageName()
        val qualitySetting = context.getQualitySetting()
        val fileSize = param.imageBitmap?.allocationByteCount?.toString() ?: "n/a"

        val connectionType = NetworkManager.getConnectionType(context)

        val sourceId = getSourceId(param.url)
        if (sourceId.isEmpty()) return

        val data = mapOf(
            "url" to param.url,
            "source_id" to sourceId,
            "page_name" to activityName,
            "file_size" to fileSize,
            "load_time" to param.requestLoadTime,
            "content_type" to param.contentType,
            "quality_setting" to qualitySetting,
            "connection_type" to connectionType,
        )

        if (GlobalConfig.isAllowDebuggingTools()) {
            Timber.d("media-loader: ${Gson().toJson(data)}")
        }

        ServerLogger.log(
            priority = priority,
            tag = tag,
            message = data
        )
    }

    private fun Context.getQualitySetting(): String {
        val preferences = MediaSettingPreferences.instance(this)
        return when (preferences.qualitySettings()) {
            0 -> "Automatic"
            1 -> "Low"
            2 -> "High"
            else -> "Unknown"
        }
    }

    private fun Context.pageName(): String {
        return try {
            javaClass.name.split(".").last()
        } catch (ignored: Throwable) {
            "None"
        }
    }

    private fun getSourceId(url: String): String {
        val baseImgUrl = "https://images.tokopedia.net/img/"
        if (!url.startsWith(baseImgUrl)) return ""

        val segments = url.split("/")
        val startIndex = segments.indexOf("img")

        if (startIndex == -1) return ""

        val sourceIdCachedIndex = 3
        val sourceIdPristineIndex = 1

        return when {
            // cached: /img/cache/{size}/{source_id}
            url.contains("cache") && segments.size > startIndex + sourceIdCachedIndex -> {
                segments[startIndex + sourceIdCachedIndex]
            }

            // pristine: /img/{source_id}
            segments.size > startIndex + sourceIdPristineIndex -> {
                segments[startIndex + sourceIdPristineIndex]
            }

            else -> ""
        }
    }

    data class Param(
        var url: String = "",
        var imageBitmap: Bitmap? = null,
        var requestLoadTime: String = "",
        var contentType: String = "",
    ) {

        /**
         * So the tracker only works if the url comes from our internal CDN, means the images.tokopedia.net,
         * Also we have to ensure that the url is not empty as well as the bitmap result also not-null.
         */
        fun shouldEligible(): Boolean {
            if (url.isEmpty() && imageBitmap == null) return false
            if (url.isFromInternalCdnImageUrl().not()) return false

            return true
        }
    }

    internal sealed class Type {
        object Request : Type()
        object Failure : Type()
    }
}
