package com.tokopedia.media.loader.listener

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.media.loader.data.Properties
import com.tokopedia.media.loader.internal.NetworkResponseManager
import com.tokopedia.media.loader.tracker.IsIcon
import com.tokopedia.media.loader.tracker.MediaLoaderTracker
import com.tokopedia.media.loader.utils.adaptiveSizeImageRequest
import com.tokopedia.media.loader.wrapper.MediaDataSource.Companion.mapTo as dataSource

internal object MediaListenerBuilder {

    operator fun invoke(
        context: Context,
        properties: Properties,
        startTime: Long
    ) = object : RequestListener<Bitmap> {

        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Bitmap>?,
            isFirstResource: Boolean
        ) = onLoadFailed(context, properties, startTime, e)

        override fun onResourceReady(
            resource: Bitmap?,
            model: Any?,
            target: Target<Bitmap>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ) = onResourceReady(context, properties, startTime, resource, target, dataSource)
    }

    private fun onLoadFailed(
        context: Context,
        properties: Properties,
        startTime: Long,
        e: GlideException?
    ): Boolean {
        val loadTime = (System.currentTimeMillis() - startTime).toString()

        MediaLoaderTracker.failed(
            context = context.applicationContext,
            url = properties.data.toString(),
            loadTime = loadTime,
            exception = e
        )

        properties.loaderListener?.onFailed(e)
        return false
    }

    private fun onResourceReady(
        context: Context,
        properties: Properties,
        startTime: Long,
        resource: Bitmap?,
        target: Target<Bitmap>?,
        dataSource: DataSource?
    ): Boolean {
        val loadTime = (System.currentTimeMillis() - startTime).toString()

        // tracker
        if (properties.data is String) {
            MediaLoaderTracker.succeed(
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

        properties.loaderListener?.onLoaded(resource, dataSource(dataSource))

        if (properties.shouldTrackNetwork) {
            val result = NetworkResponseManager.getInstance(context)
            properties.setNetworkResponse?.header(result.header())
        }
        return false
    }
}
