package com.tokopedia.media.loader

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.tokopedia.media.loader.data.IMAGE_SOURCE_EMPTY_STRING
import com.tokopedia.media.loader.data.IMAGE_SOURCE_NULL
import com.tokopedia.media.loader.data.MediaException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.tokopedia.media.loader.data.Properties
import com.tokopedia.media.loader.listener.MediaListenerBuilder
import com.tokopedia.media.loader.module.GlideApp
import com.tokopedia.media.loader.utils.delayInto
import com.tokopedia.media.loader.utils.isValidUrl
import com.tokopedia.media.loader.utils.loadLookup
import com.tokopedia.media.loader.utils.mediaLoad
import com.tokopedia.media.loader.utils.timeout
import com.tokopedia.media.loader.utils.transition

internal object MediaLoaderApi {

    fun loadImage(imageView: ImageView, properties: Properties) {
        val source = properties.data
        val context = imageView.context

        // startTimeRequest will use for performance tracking
        val startTimeRequest = System.currentTimeMillis()

        // handling empty url
        if (properties.data is String && source.toString().isEmpty()) {
            imageView.setImageDrawable(properties.getErrorDrawable(context))
            properties.loaderListener?.onFailed(getErrorException(IMAGE_SOURCE_EMPTY_STRING))
            return
        }

        // if the data source is null, the image will be render the error drawable
        if (properties.data == null) {
            imageView.setImageDrawable(properties.getErrorDrawable(context))
            properties.loaderListener?.onFailed(getErrorException(IMAGE_SOURCE_NULL))
            return
        }

        // set the imageView's size only if the consumer needs a blur-hash as a placeholder
        if (properties.blurHash && source is String && source.toString().isValidUrl()) {
            properties.setImageSize(
                width = imageView.measuredWidth,
                height = imageView.measuredHeight
            )
        }

        GlideApp
            .with(context)
            .asBitmap()
            .transform(properties)
            .commonOptions(properties)
            .dynamicPlaceHolder(context, properties)
            .thumbnail(setThumbnailUrl(context, properties))
            .transition(properties)
            .timeout(properties)
            .listener(
                MediaListenerBuilder<Bitmap>(
                    context,
                    properties,
                    startTimeRequest
                )
            )
            .mediaLoad(properties)
            .delayInto(imageView, properties)
    }

    fun loadGifImage(imageView: ImageView, source: String, properties: Properties) {
        val context = imageView.context

        if (context.isValid()) {
            GlideApp
                .with(context)
                .asGif()
                .addListener(MediaListenerBuilder<GifDrawable>(context, properties, 0L))
                .load(source)
                .delayInto(imageView, properties)
        }
    }

    fun loadGifImage(imageView: ImageView, source: Int, properties: Properties) {
        // local asset especially on DF cannot load resource using appContext
        val context = imageView.context

        if (context.isValid()) {
            GlideApp
                .with(context)
                .asGif()
                .addListener(MediaListenerBuilder<GifDrawable>(context, properties, 0L))
                .loadLookup(context, source)
                .delayInto(imageView, properties)
        }
    }

    fun setThumbnailUrl(context: Context, properties: Properties): RequestBuilder<Bitmap>? {
        if (properties.thumbnailUrl.isEmpty()) return null

        return GlideApp
            .with(context)
            .asBitmap()
            .fitCenter()
            .load(properties.thumbnailUrl)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
    }

    private fun getErrorException(msg: String): MediaException {
        return MediaException(msg)
    }
}
