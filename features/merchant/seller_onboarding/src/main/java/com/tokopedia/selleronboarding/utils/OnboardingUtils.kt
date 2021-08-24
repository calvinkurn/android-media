package com.tokopedia.selleronboarding.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.media.loader.module.GlideApp

/**
 * Created By @ilhamsuaib on 09/04/20
 */

object OnboardingUtils {

    private const val STATUS_BAR_DEFAULT_HEIGHT = 24

    fun getStatusBarHeight(context: Context): Int {
        var height = context.dpToPx(STATUS_BAR_DEFAULT_HEIGHT).toInt()
        val resId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resId > 0) {
            height = context.resources.getDimensionPixelSize(resId)
        }
        return height
    }

    fun loadImageAsBitmap(
        context: Context,
        url: String,
        rotation: Float,
        callback: (Bitmap) -> Unit
    ) {
        GlideApp.with(context)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    val matrix = Matrix()
                    matrix.postRotate(rotation)
                    val rotatedBitmap = Bitmap.createBitmap(
                        resource,
                        0,
                        0,
                        resource.width,
                        resource.height,
                        matrix,
                        true
                    )
                    callback(rotatedBitmap)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }
}