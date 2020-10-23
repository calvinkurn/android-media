package com.tokopedia.media.loader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.media.loader.common.MediaDataSource.Companion.mapToDataSource
import com.tokopedia.media.loader.common.Properties
import com.tokopedia.media.loader.module.GlideApp
import com.tokopedia.media.loader.target.ImageViewTarget
import com.tokopedia.media.loader.target.MediaTarget
import com.tokopedia.media.loader.transform.BlurHashDecoder
import com.tokopedia.media.loader.transform.CircleCrop
import com.tokopedia.media.loader.utils.BLUR_HASH_QUERY
import com.tokopedia.media.loader.utils.toUri
import com.tokopedia.media.loader.wrapper.MediaCacheStrategy.Companion.mapToDiskCacheStrategy
import com.tokopedia.media.loader.wrapper.MediaDecodeFormat.Companion.mapToDecodeFormat

object GlideBuilder {

    private fun glideListener(
            listener: MediaTarget?
    ) = object : RequestListener<Drawable> {
        override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
        ): Boolean {
            listener?.failedLoad(e)
            return false
        }

        override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
        ): Boolean {
            listener?.successLoad(resource, mapToDataSource(dataSource))
            return false
        }
    }

    @JvmOverloads
    fun loadImage(properties: Properties) {
        val localTransform = mutableListOf<Transformation<Bitmap>>()

        with(properties) {
            if (target is ImageViewTarget) {
                val imageView = (target as ImageViewTarget).view
                val context = (target as ImageViewTarget).view.context

                if (data is String) {
                    if ((data as String).toEmptyStringIfNull().isEmpty()) {
                        imageView.setImageDrawable(getDrawable(context, placeHolder))
                        return@with
                    }
                }

                if (data == null) {
                    imageView.setImageDrawable(getDrawable(context, error))
                } else {
                    GlideApp.with(context).load(data).apply {
                        if (thumbnailUrl.isNotEmpty()) {
                            thumbnail(thumbnailLoader(context, thumbnailUrl))
                        } else {
                            if (data is String) {
                                (data as String).toUri()?.let {
                                    if (it.getQueryParameters(BLUR_HASH_QUERY).isNotEmpty()) {
                                        val blurHash = it.getQueryParameter(BLUR_HASH_QUERY)
                                        thumbnail(thumbnailLoader(imageView.context, blurring(imageView, blurHash)))
                                    }
                                }
                            }
                        }

                        when (imageView.scaleType) {
                            ImageView.ScaleType.FIT_CENTER -> fitCenter()
                            ImageView.ScaleType.CENTER_CROP -> centerCrop()
                            ImageView.ScaleType.CENTER_INSIDE -> centerInside()
                            else -> {}
                        }

                        if (size != null) override(size.width, size.height)
                        if (decode != null) format(mapToDecodeFormat(decode))
                        if (roundedRadius != 0f) transform(RoundedCorners(roundedRadius.toInt()))
                        if (singleTransform != null) localTransform.add(singleTransform)
                        if (customSignature != null) signature(customSignature)
                        if (isCircular) localTransform.add(CircleCrop())
                        if (!isAnimate) dontAnimate()

                        cacheStrategy?.let { diskCacheStrategy(mapToDiskCacheStrategy(it)) }
                        transforms?.let { localTransform.addAll(it) }

                        if (localTransform.isNotEmpty()) {
                            transform(MultiTransformation(localTransform))
                        }

                        listener(glideListener(target))

                        into(imageView)
                    }
                }
            }
        }
    }

    fun blurring(imageView: ImageView, blurHash: String?): Bitmap? {
        return BlurHashDecoder.decode(
                blurHash = blurHash,
                width = imageView.maxWidth,
                height = imageView.maxHeight
        )
    }

    private fun thumbnailLoader(context: Context, resource: Any?): RequestBuilder<Drawable> {
        return GlideApp.with(context).load(resource).fitCenter().diskCacheStrategy(DiskCacheStrategy.DATA)
    }

    fun loadGifImage(imageView: ImageView, url: String) {
        with(imageView) {
            GlideApp.with(context)
                    .asGif()
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .transform(RoundedCorners(10))
                    .into(this)
        }
    }

}