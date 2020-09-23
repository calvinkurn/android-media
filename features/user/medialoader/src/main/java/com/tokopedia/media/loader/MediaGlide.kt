package com.tokopedia.media.loader

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.tokopedia.media.loader.module.GlideApp
import com.tokopedia.media.loader.viewtarget.RoundedViewTarget

internal data class MediaGlide(val imageView: ImageView?, val url: Any?) {

    private var isRounded: Boolean = false
    private var radius: Float = 0f

    private val glide = imageView?.context?.let {
        GlideApp.with(it).asBitmap().load(url)
    }

    fun isAnimate(isAnimate: Boolean) = apply {
        if (!isAnimate) glide?.dontAnimate()
    }

    fun isRounded(isRounded: Boolean, radius: Float) = apply {
        this.isRounded = isRounded
        this.radius = radius
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

    fun build(): MediaGlide {
        return MediaGlide(imageView, url).apply {
            imageView?.let {
                if (!isRounded) glide?.into(it)
                else glide?.into(RoundedViewTarget(imageView, radius))
            }
        }
    }

}