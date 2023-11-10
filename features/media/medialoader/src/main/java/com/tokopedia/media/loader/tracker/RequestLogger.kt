package com.tokopedia.media.loader.tracker

import android.content.Context
import com.tokopedia.config.GlobalConfig
import com.tokopedia.logger.ServerLogger
import com.tokopedia.media.loader.internal.NetworkManager
import com.tokopedia.media.loader.utils.isFromInternalCdnImageUrl
import timber.log.Timber

object RequestLogger : BaseLogger {

    fun request(context: Context, param: Param.() -> Unit) {
        val mParam = Param().apply(param)
        request(context.applicationContext, mParam)
    }

    private fun request(context: Context, param: Param) {
        val (tag, priority) = tags[Type.Request] ?: return

        /**
         * So the tracker only works if the url comes from our internal CDN, means the images.tokopedia.net,
         * Also we have to ensure that the url is not empty as well as the bitmap result also not-null.
         */
        if (param.isEligible().not()) return

        val qualitySetting = context.getQualitySetting()
        val connectionType = NetworkManager.getConnectionType(context)
        val (type, sourceId) = getSourceId(param.url)

        // ensure the url only contain sourceId got captured.
        if (sourceId.isEmpty()) return

        val data = mapOf(
            "url" to param.url,
            "img_type" to type,
            "source_id" to sourceId,
            "file_size" to param.fileSize,
            "load_time" to param.requestLoadTime,
            "content_type" to param.contentType,
            "quality_setting" to qualitySetting,
            "connection_type" to connectionType,
        )

        if (GlobalConfig.isAllowDebuggingTools()) {
            Timber.d("media-loader: $data")
        }

        ServerLogger.log(
            priority = priority,
            tag = tag,
            message = data
        )
    }

    data class Param(
        var url: String = "",
        var fileSize: String = "",
        var requestLoadTime: String = "",
        var contentType: String = "",
    ) {

        fun isEligible() = url.isNotEmpty() && url.isFromInternalCdnImageUrl()
    }
}
