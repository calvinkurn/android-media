package com.tokopedia.media.loader.listener

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.media.loader.common.Properties
import com.tokopedia.media.loader.tracker.IsIcon
import com.tokopedia.media.loader.tracker.MediaLoaderTracker
import com.tokopedia.media.loader.utils.adaptiveSizeImageRequest
import com.tokopedia.media.loader.wrapper.MediaDataSource.Companion.mapTo as dataSource

object MediaListenerBuilder {


    fun callback(
            context: Context,
            properties: Properties,
            startTime: Long,
            listener: MediaListener?,
    ) = object : RequestListener<Bitmap> {
        override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Bitmap>?,
                isFirstResource: Boolean
        ): Boolean {
            listener?.onFailed(e)

            val loadTime = (System.currentTimeMillis() - startTime).toString()
            MediaLoaderTracker.trackLoadFailed(
                context = context.applicationContext,
                url = properties.data.toString(),
                loadTime = loadTime,
                exception = e
            )
            return false
        }

        override fun onResourceReady(
                resource: Bitmap?,
                model: Any?,
                target: Target<Bitmap>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
        ): Boolean {
            val loadTime = (System.currentTimeMillis() - startTime).toString()

            // tracker
            if (properties.data is String) {
                MediaLoaderTracker.simpleTrack(
                    context = context.applicationContext,
                    bitmap = resource,
                    url = properties.data.toString(),
                    isIcon = IsIcon(properties.isIcon),
                    loadTime = loadTime
                )
            }

            // override the load time into properties
            properties.loadTime = loadTime

            // override target size with adaptive (dynamic)
            if (properties.isAdaptiveSizeImageRequest) {
                resource?.adaptiveSizeImageRequest(target)
            }

            listener?.onLoaded(resource, dataSource(dataSource))
            return false
        }
    }

}
