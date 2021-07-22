package com.tokopedia.media.loader.listener

import android.content.Context
import android.graphics.Bitmap
import androidx.core.graphics.BitmapCompat
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.media.common.Loader
import com.tokopedia.media.loader.common.Properties
import com.tokopedia.media.loader.tracker.PerformanceTracker.postRender as trackPerformancePostRender
import com.tokopedia.media.loader.wrapper.MediaDataSource.Companion.mapTo as dataSource

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

            // save for an accumulative bitmap size in local
            Loader.bitmapSize()?.save(fileSize.toInt())

            // only track if the URL from CDN service
            trackPerformancePostRender(
                performanceMonitoring,
                pageName,
                loadTime,
                fileSize
            )

            // override the load time into properties
            properties.loadTime = loadTime

            listener?.onLoaded(resource, dataSource(dataSource))
            return false
        }
    }

}