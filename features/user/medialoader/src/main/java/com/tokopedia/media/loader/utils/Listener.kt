package com.tokopedia.media.loader.utils

import android.content.Context
import android.graphics.Bitmap
import androidx.core.graphics.BitmapCompat
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.media.loader.common.MediaListener
import com.tokopedia.media.loader.common.Properties
import com.tokopedia.media.loader.tracker.PerformanceTracker
import com.tokopedia.media.loader.common.MediaDataSource.Companion.mapToDataSource as dataSource

object Listener {

    operator fun invoke(
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
            val fileSize = resource?.let { BitmapCompat.getAllocationByteCount(it).toString() }?: "0"
            val loadTime = (System.currentTimeMillis() - startTime).toString()

            PerformanceTracker.postRender(
                    performanceMonitoring,
                    loadTime,
                    fileSize
            )

            listener?.onLoaded(resource, dataSource(dataSource))

            // log
            if (properties.data is String) {
                val pageName = context.javaClass.name
                val pageCanonicalName = context.javaClass.canonicalName

                println("$pageName,$pageCanonicalName,${properties.data.toString()},$loadTime")
            }

            return false
        }
    }

}