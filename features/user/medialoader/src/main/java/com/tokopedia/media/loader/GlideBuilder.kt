package com.tokopedia.media.loader

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.*
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.media.loader.common.LoaderStateListener
import com.tokopedia.media.loader.data.Resize
import com.tokopedia.media.loader.module.GlideApp
import timber.log.Timber

object GlideBuilder {

    private fun glideListener(listener: LoaderStateListener?, onSuccess: () -> Unit) = object : RequestListener<Drawable> {
        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
            listener?.failedLoad(e)
            return false
        }

        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
            listener?.successLoad(resource, dataSource)
            onSuccess()
            return false
        }
    }

    @JvmOverloads
    fun loadImage(
            imageView: ImageView,
            thumbnailUrl: String,
            url: GlideUrl?,
            radius: Float = 0f,
            signatureKey: Key?,
            cacheStrategy: DiskCacheStrategy?,
            @DrawableRes placeHolder: Int = 0,
            @DrawableRes resOnError: Int,
            isAnimate: Boolean = false,
            isCircular: Boolean = false,
            overrideSize: Resize? = null,
            decodeFormat: DecodeFormat? = null,
            listener: LoaderStateListener? = null,
            transform: Transformation<Bitmap>? = null,
            transforms: List<Transformation<Bitmap>>? = null
    ) {
        val localTransform = mutableListOf<Transformation<Bitmap>>()
        val startTime = System.currentTimeMillis()

        val drawableError = if (resOnError != 0) {
            getDrawable(imageView.context, resOnError)
        } else {
            getDrawable(imageView.context, R.drawable.ic_media_default_error)
        }

        if (url == null) {
            imageView.setImageDrawable(drawableError)
        } else {
            GlideApp.with(imageView).load(url).apply {
                if (thumbnailUrl.isNotEmpty()) thumbnail(imageView.thumbnailLoader(thumbnailUrl))
                if (overrideSize != null) override(overrideSize.width, overrideSize.height)
                if (radius != 0f) transform(RoundedCorners(radius.toInt()))
                if (transform != null) localTransform.add(transform)
                if (signatureKey != null) signature(signatureKey)
                if (placeHolder != 0) placeholder(placeHolder)
                if (decodeFormat != null) format(decodeFormat)
                if (isCircular) localTransform.add(CircleCrop())
                if (!isAnimate) dontAnimate()

                drawableError?.let { drawable -> error(drawable) }
                cacheStrategy?.let { diskCacheStrategy(it) }
                transforms?.let { localTransform.addAll(it) }

                if (localTransform.isNotEmpty()) {
                    transform(MultiTransformation(localTransform))
                }

                when (imageView.scaleType) {
                    ImageView.ScaleType.FIT_CENTER -> fitCenter()
                    ImageView.ScaleType.CENTER_CROP -> centerCrop()
                    ImageView.ScaleType.CENTER_INSIDE -> centerInside()
                    else -> {}
                }

                if (listener != null) {
                    listener(glideListener(listener) {
                        val endTime = System.currentTimeMillis()
                        val requestTime = endTime - startTime

                        Timber.d("MediaLoader#URL=${url.toStringUrl()}#requestTime=$requestTime")
                    })
                }

                into(imageView)
            }
        }
    }

    private fun ImageView.thumbnailLoader(url: String): RequestBuilder<Drawable> {
        return Glide.with(this.context)
                .load(url)
                .dontAnimate()
                .dontTransform()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.DATA)
    }

    fun loadGifImage(imageView: ImageView, url: String) {
        with(imageView) {
            Glide.with(context)
                    .asGif()
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .transform(RoundedCorners(10))
                    .into(this)
        }
    }

}