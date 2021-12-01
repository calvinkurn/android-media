package com.tokopedia.media.loader.listener

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.kotlin.extensions.view.formattedToMB
import com.tokopedia.media.common.Loader
import com.tokopedia.media.loader.common.Properties
import com.tokopedia.media.loader.tracker.MediaLoaderTracker
import com.tokopedia.media.loader.tracker.MediaLoaderTrackerParam
import com.tokopedia.media.loader.utils.adaptiveSizeImageRequest
import com.tokopedia.media.loader.wrapper.MediaDataSource.Companion.mapTo as dataSource

object MediaListenerBuilder {

    private const val PAGE_NAME_NOT_FOUND = "None"

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
            return false
        }

        override fun onResourceReady(
                resource: Bitmap?,
                model: Any?,
                target: Target<Bitmap>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
        ): Boolean {
            val pageName = try {
                context.javaClass.name.split(".").last()
            } catch (e: Throwable) {
                PAGE_NAME_NOT_FOUND
            }

            val fileSize = resource?.allocationByteCount?.toString() ?: "0"
            val fileSizeInMb = fileSize.toLong().formattedToMB()

            val loadTime = (System.currentTimeMillis() - startTime).toString()

            // save for an accumulative bitmap size in local
            Loader.bitmapSize()?.saveSize(fileSize)

            // tracker
            if (properties.data is String && !properties.isIcon) {
                MediaLoaderTracker.track(
                    context = context,
                    data = MediaLoaderTrackerParam(
                        url = properties.data.toString(),
                        pageName = pageName,
                        loadTime = loadTime,
                        fileSize = fileSizeInMb
                    )
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
