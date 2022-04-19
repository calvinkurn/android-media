package com.tokopedia.selleronboarding.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.media.loader.module.GlideApp
import timber.log.Timber

/**
 * Created By @ilhamsuaib on 09/04/20
 */

object OnboardingUtils {

    private const val STATUS_BAR_DEFAULT_HEIGHT = 24
    private const val KEY_STATUS_BAR_HEIGHT = "status_bar_height"
    private const val KEY_DIMEN = "dimen"
    private const val KEY_ANDROID = "android"
    private const val ROTATION_DEF_VALUE = 0f

    fun getStatusBarHeight(context: Context): Int {
        var height = context.dpToPx(STATUS_BAR_DEFAULT_HEIGHT).toInt()
        val resId = context.resources.getIdentifier(KEY_STATUS_BAR_HEIGHT, KEY_DIMEN, KEY_ANDROID)
        if (resId > Int.ZERO) {
            height = context.resources.getDimensionPixelSize(resId)
        }
        return height
    }

    fun loadImageAsBitmap(
        context: Context,
        url: String,
        rotation: Float = ROTATION_DEF_VALUE,
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
                    try {
                        val matrix = Matrix()
                        matrix.postRotate(rotation)
                        val rotatedBitmap = Bitmap.createBitmap(
                            resource,
                            ROTATION_DEF_VALUE.toInt(),
                            ROTATION_DEF_VALUE.toInt(),
                            resource.width,
                            resource.height,
                            matrix,
                            true
                        )
                        callback(rotatedBitmap)
                    } catch (e: Exception) {
                        Timber.e(e)
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }
}