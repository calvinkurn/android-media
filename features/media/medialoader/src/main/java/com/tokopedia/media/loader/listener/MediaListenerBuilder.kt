package com.tokopedia.media.loader.listener

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.media.loader.data.MediaException
import com.tokopedia.media.loader.data.Properties
import com.tokopedia.media.loader.data.getFailureType
import com.tokopedia.media.loader.internal.NetworkResponseManager
import com.tokopedia.media.loader.tracker.old.MediaLoaderTracker
import com.tokopedia.media.loader.utils.adaptiveSizeImageRequest
import java.io.File
import com.tokopedia.media.loader.wrapper.MediaDataSource.Companion.mapTo as dataSource

internal object MediaListenerBuilder {

    inline operator fun <reified T> invoke(
        context: Context,
        properties: Properties,
        startTime: Long
    ) = object : RequestListener<T> {

        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<T>?,
            isFirstResource: Boolean
        ): Boolean {
            onLoadFailed(context, properties, startTime, e)
            return false
        }

        override fun onResourceReady(
            resource: T?,
            model: Any?,
            target: Target<T>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            try {
                when (T::class){
                    Bitmap::class -> onResourceReady(context, properties, startTime, (resource as Bitmap), (target as Target<Bitmap>), dataSource, isFirstResource)
                    GifDrawable::class -> properties.loaderListener?.onLoaded((resource as GifDrawable), dataSource(dataSource))
                    File::class -> properties.loaderListener?.onDownload(resource as File, dataSource(dataSource))
                }
            }catch (e: Exception) {
                properties.loaderListener?.onFailed(MediaException(e.message))
            }
            return false
        }
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
        dataSource: DataSource?,
        isFirstResource: Boolean
    ): Boolean {
        val loadTime = (System.currentTimeMillis() - startTime).toString()

        if (properties.data is String) {
            val shouldAbleToExposeResponseHeader = properties.featureToggle
                ?.shouldAbleToExposeResponseHeader(context)

            if (properties.shouldTrackNetwork && shouldAbleToExposeResponseHeader == true) {
                val networkResponseManager = NetworkResponseManager.instance(context)
                val headers = networkResponseManager.header(properties.data.toString())

                properties.setNetworkResponse?.header(
                    headers, // get all header responses
                    headers.getFailureType() // get failure type (if any)
                )

                if (properties.isForceClearHeaderCache) {
                    networkResponseManager.forceResetCache()
                }
            }
        }

        // override the load time into properties
        properties.loadTime = loadTime

        // override target size with adaptive (dynamic)
        if (properties.isAdaptiveSizeImageRequest) {
            resource?.adaptiveSizeImageRequest(target)
        }

        properties.loaderListener?.onLoaded(resource, dataSource(dataSource), isFirstResource )
        return false
    }
}
