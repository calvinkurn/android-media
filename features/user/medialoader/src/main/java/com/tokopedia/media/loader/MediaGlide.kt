package com.tokopedia.media.loader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.tokopedia.media.loader.common.Properties
import com.tokopedia.media.loader.module.GlideApp
import com.tokopedia.media.loader.module.GlideRequest

internal class MediaGlide(
        private val imageView: ImageView?,
        val url: Any?,
        private val glide: GlideRequest<Bitmap>? = null
): Properties() {

    private var radius: Float = 0f

    fun isAnimate(isAnimate: Boolean) = apply {
        if (!isAnimate) glide?.dontAnimate()
    }

    fun isRounded(isRounded: Boolean, radius: Float) = apply {
        this.isRounded = isRounded
        this.radius = radius
    }

    fun isCircular(isCircular: Boolean) = apply {
        this.isCircular = isCircular
    }

    fun <T> showError(errorRes: T) = apply {
        if (errorRes is Int && errorRes != 0) {
            glide?.error(errorRes)
        } else if (errorRes is Drawable? && errorRes != null) {
            glide?.error(errorRes)
        }
    }

    fun <T> showPlaceHolder(holder: T) = apply {
        if (holder is Int && holder != 0) {
            glide?.placeholder(holder)
        } else if (holder is Drawable? && holder != null) {
            glide?.placeholder(holder)
        }
    }

    fun cacheStrategy(cacheStrategy: DiskCacheStrategy?) = apply {
        cacheStrategy?.let { glide?.diskCacheStrategy(it) }
    }

    fun signature(key: Key?) = apply {
        key?.let { glide?.signature(it) }
    }

    private fun scaleType() = apply {
        when (imageView?.scaleType) {
            ImageView.ScaleType.FIT_CENTER -> glide?.fitCenter()
            ImageView.ScaleType.CENTER_CROP -> glide?.centerCrop()
            ImageView.ScaleType.CENTER_INSIDE -> glide?.centerInside()
            else -> {}
        }
    }

    fun build(context: Context): MediaGlide {
        val glide = GlideApp.with(context).asBitmap().load(url)
        return MediaGlide(imageView, url, glide).apply {
            scaleType()

            imageView?.let {
                when {
                    isRounded -> glide.transform(RoundedCorners(roundedRadius.toInt()))
                    //isCircular -> glide.into(CircularViewTarget(imageView))
                }
                glide.into(it)
            }
        }
    }

}