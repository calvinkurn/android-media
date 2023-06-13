package com.tokopedia.media.loader

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.tokopedia.media.loader.data.Properties
import com.tokopedia.media.loader.listener.MediaListenerBuilder
import com.tokopedia.media.loader.module.GlideApp
import com.tokopedia.media.loader.utils.delayInto
import com.tokopedia.media.loader.utils.isValidUrl
import com.tokopedia.media.loader.utils.mediaLoad

internal object MediaLoaderApi {

    fun loadImage(imageView: ImageView, properties: Properties) {
        val source = properties.data
        val context = imageView.context

        // startTimeRequest will use for performance tracking
        val startTimeRequest = System.currentTimeMillis()

        // handling empty url
        if (properties.data is String && source.toString().isEmpty()) {
            return
        }

        // if the data source is null, the image will be render the error drawable
        if (properties.data == null) {
            imageView.setImageDrawable(getDrawable(context, properties.error))
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
            .listener(
                MediaListenerBuilder(
                    context,
                    properties,
                    startTimeRequest
                )
            )
            .mediaLoad(properties)
            .delayInto(imageView, properties)
    }

    fun loadGifImage(imageView: ImageView, source: String, properties: Properties) {
        val context = imageView.context.applicationContext

        if (context.isValid()) {
            GlideApp
                .with(context)
                .asGif()
                .transform(properties)
                .commonOptions(properties)
                .load(source)
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

}
