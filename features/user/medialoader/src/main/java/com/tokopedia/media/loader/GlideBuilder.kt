package com.tokopedia.media.loader

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.tokopedia.media.loader.module.GlideApp

object GlideBuilder {

    @JvmOverloads
    fun loadImage(
            imageView: ImageView,
            url: GlideUrl?,
            radius: Float = 0f,
            signatureKey: Key?,
            cacheStrategy: DiskCacheStrategy?,
            @DrawableRes placeHolder: Int = 0,
            @DrawableRes resOnError: Int,
            isAnimate: Boolean = false
    ) {

        val drawableError = if (resOnError != 0) {
            getDrawable(imageView.context, resOnError)
        } else {
            getDrawable(imageView.context, R.drawable.ic_media_default_error)
        }

        if (url == null) {
            imageView.setImageDrawable(drawableError)
        } else {
            GlideApp.with(imageView).load(url).apply {
                if (radius != 0f) transform(RoundedCorners(radius.toInt()))
                if (signatureKey != null) signature(signatureKey)
                if (placeHolder != 0) placeholder(placeHolder)
                if (!isAnimate) dontAnimate()

                drawableError?.let { drawable -> error(drawable) }
                cacheStrategy?.let { diskCacheStrategy(it) }

                when (imageView.scaleType) {
                    ImageView.ScaleType.FIT_CENTER -> fitCenter()
                    ImageView.ScaleType.CENTER_CROP -> centerCrop()
                    ImageView.ScaleType.CENTER_INSIDE -> centerInside()
                    else -> {}
                }

                into(imageView)
            }
        }
    }

}