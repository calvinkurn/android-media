package com.tokopedia.tokopedianow.common.util

import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.widget.ImageView
import com.tokopedia.media.loader.loadImageWithTarget
import com.tokopedia.media.loader.utils.MediaTarget

object ImageUtil {
    fun setBackgroundImage(
        context: Context,
        url: String,
        view: View?
    ) {
        view?.let {
            loadImageWithTarget(context = context, url = url, {
                useBlurHash(true)
            }, MediaTarget(view, onReady = { _, resource ->
                it.background = BitmapDrawable(it.resources, resource)
            }))
        }
    }

    fun ImageView.applyBrightnessFilter(brightnessFactor: Float) {
        val colorMatrix = ColorMatrix().apply {
            set(floatArrayOf(
                brightnessFactor, 0f, 0f, 0f, 0f,
                0f, brightnessFactor, 0f, 0f, 0f,
                0f, 0f, brightnessFactor, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            ))
        }
        colorFilter = ColorMatrixColorFilter(colorMatrix)
    }
}
