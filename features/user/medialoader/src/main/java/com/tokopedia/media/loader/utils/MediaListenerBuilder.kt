package com.tokopedia.media.loader.utils

import android.content.Context
import android.graphics.Bitmap
import androidx.core.graphics.BitmapCompat
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.media.common.Loader
import com.tokopedia.media.common.data.toUri
import com.tokopedia.media.loader.BuildConfig
import com.tokopedia.media.loader.common.MediaListener
import com.tokopedia.media.loader.common.Properties
import com.tokopedia.media.loader.tracker.PerformanceTracker
import java.lang.Exception
import com.tokopedia.media.loader.common.MediaDataSource.Companion.mapToDataSource as dataSource

object MediaListenerBuilder {

    fun callback(
            context: Context,
            properties: Properties,
            startTime: Long,
            listener: MediaListener?,
            performanceMonitoring: PerformanceMonitoring?
    ) = object : RequestListener<Bitmap> {
        override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Bitmap>?,
                isFirstResource: Boolean
        ): Boolean {
            listener?.onFailed(e)
            return false
        }

        override fun onResourceReady(
                resource: Bitmap?,
                model: Any?,
                target: Target<Bitmap>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
        ): Boolean {
            val pageName = context.javaClass.name.split(".").last()
            val fileSize = resource?.let { BitmapCompat.getAllocationByteCount(it).toString() }?: "0"
            val loadTime = (System.currentTimeMillis() - startTime).toString()

            PerformanceTracker.postRender(
                    performanceMonitoring,
                    pageName,
                    loadTime,
                    fileSize
            )

            // TODO: Remove
            if (BuildConfig.DEBUG && properties.data is String) {
                try {
                    val urlBuilder = Loader.urlBuilder(properties.data.toString())
                    val qualitySettings = Loader.settings.getQualitySetting(Loader.settings.qualitySettings())
                    val ect = urlBuilder.toUri()?.getQueryParameter("ect")?: "4g"

                    println("MediaLoader => $pageName, ${properties.data.toString()}, $qualitySettings, $ect, $loadTime, $fileSize")
                    println("MediaLoader => properties: $properties")
                } catch (e: Exception) {}
            }

            listener?.onLoaded(resource, dataSource(dataSource))
            return false
        }
    }

}