package com.tokopedia.tokopoints.view.util

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.tokopedia.tokopoints.R

object ImageUtil {
    fun dimImage(imageView: ImageView?) {
        val matrix = ColorMatrix()
        matrix.setSaturation(0f)
        val filter = ColorMatrixColorFilter(matrix)
        imageView?.colorFilter = filter
    }

    fun unDimImage(imageView: ImageView?) {
        val matrix = ColorMatrix()
        matrix.setSaturation(1f)
        val filter = ColorMatrixColorFilter(matrix)
        imageView?.colorFilter = filter
    }

    fun loadImage(imageView: ImageView, url: String?) {
        try {
            Glide.with(imageView.context)
                .load(url)
                .dontAnimate()
                .placeholder(R.drawable.fromtkpddesign_ic_loading_image_tokopoints)
                .error(R.drawable.fromtkpddesign_ic_loading_image_tokopoints)
                .override(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL)
                .into(imageView)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
