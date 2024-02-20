package com.tokopedia.tokopoints.view.util

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.widget.ImageView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.design.R as designR

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
                setPlaceHolder(designR.drawable.ic_loading_image)
                setErrorDrawable(designR.drawable.ic_loading_image)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
