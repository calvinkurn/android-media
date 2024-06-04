package com.tokopedia.tokopoints.view.util

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.widget.ImageView
import com.bumptech.glide.request.target.Target
import com.tokopedia.media.loader.data.Resize
import com.tokopedia.media.loader.loadImage
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
            imageView.loadImage(url){
                isAnimate(false)
                setPlaceHolder(R.drawable.fromtkpddesign_ic_loading_image_tokopoints)
                setErrorDrawable(R.drawable.fromtkpddesign_ic_loading_image_tokopoints)
                overrideSize(Resize(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
