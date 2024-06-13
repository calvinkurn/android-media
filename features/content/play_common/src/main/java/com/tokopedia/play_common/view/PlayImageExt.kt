package com.tokopedia.play_common.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Size
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.tokopedia.media.loader.data.Resize
import com.tokopedia.media.loader.getBitmapFromUrl
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.wrapper.MediaCacheStrategy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

fun ImageView.loadImage(url: String, listener: ImageLoaderStateListener? = null) {
    this.loadImage(url) {
        listener(
            onSuccess = { _, _ ->
                listener?.successLoad()
            },
            onError = { _ ->
                listener?.failedLoad()
            }
        )
    }
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
    cacheStrategy: DiskCacheStrategy = DiskCacheStrategy.NONE
): Bitmap? = withContext(Dispatchers.IO) {
    suspendCancellableCoroutine { cont ->
        url.getBitmapFromUrl(this@getBitmapFromUrl, properties = {
            size?.let {
                overrideSize(Resize(size.width, size.height))
            }
            setCacheStrategy(MediaCacheStrategy.Companion.mapTo(cacheStrategy))
            listener(
                onSuccess = { bitmap, _ ->
                    cont.resume(bitmap)
                },
                onError = {
                    cont.resume(null)
                }
            )
        })
    }
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

interface ImageLoaderStateListener {
    fun successLoad()
    fun failedLoad()
}
