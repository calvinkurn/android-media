package com.tokopedia.play_common.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.Size
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

fun ImageView.loadImage(url: String, listener: ImageHandler.ImageLoaderStateListener? = null){
    Glide.with(context)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    listener?.failedLoad()
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    listener?.successLoad()
                    return false
                }
            })
            .into(this)
}

fun View.setGradientBackground(colorArray: List<String>) {
    if (colorArray.size > 1) {
        val colors = IntArray(colorArray.size)
        for (i in colorArray.indices) {
            colors[i] = parseColor(colorArray[i])
        }
        val gradient = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors)
        gradient.cornerRadius = 0f
        this.background = gradient
    } else if (colorArray.isNotEmpty()) {
        this.setBackgroundColor(parseColor(colorArray.first()))
    }
}

private fun parseColor(colorHex: String): Int {
    return try {
        Color.parseColor(colorHex)
    } catch (e: Throwable) {
        0
    }
}

suspend fun Context.getBitmapFromUrl(
    url: String,
    size: Size? = null,
    cacheStrategy: DiskCacheStrategy = DiskCacheStrategy.NONE,
): Bitmap? = suspendCancellableCoroutine { cont ->
    Glide.with(this)
        .asBitmap()
        .load(url)
        .diskCacheStrategy(cacheStrategy)
        .listener(object : RequestListener<Bitmap> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Bitmap>?,
                isFirstResource: Boolean
            ): Boolean {
                cont.resume(null)
                return false
            }

            override fun onResourceReady(
                resource: Bitmap?,
                model: Any?,
                target: Target<Bitmap>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                cont.resume(resource)
                return false
            }
        }).let {
            if (size != null) {
                it.apply(
                    RequestOptions()
                        .override(size.width, size.height)
                )
            } else it
        }.submit()
}

fun View.setGradientAnimBackground(colorArray: List<String>) {
    if (colorArray.size > 1) {
        val colors = IntArray(colorArray.size)
        for (i in colorArray.indices) {
            colors[i] = parseColor(colorArray[i])
        }
        val gradient = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors)
        gradient.cornerRadii = floatArrayOf(
            // top left
            0f,
            0f,
            // top right
            20f,
            20f,
            // bottom right
            20f,
            20f,
            // bottom left
            0f,
            0f
        )
        this.background = gradient
    } else if (colorArray.isNotEmpty()) {
        this.setBackgroundColor(parseColor(colorArray.first()))
    }
}
