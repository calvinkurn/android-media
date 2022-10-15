package com.tokopedia.tokopedianow.common.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.tokopedia.media.loader.loadImageWithTarget
import com.tokopedia.media.loader.utils.MediaTarget

object ImageUtil {
    private const val LEFT_BOUND = 0
    private const val TOP_BOUND = 0

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

    fun convertVectorToDrawable(
        context: Context,
        drawableId: Int
    ): BitmapDrawable? {
        ContextCompat.getDrawable(context, drawableId)?.apply {
            colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN900),
                BlendModeCompat.SRC_ATOP
            )
            val bitmap = Bitmap.createBitmap(
                intrinsicWidth,
                intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            setBounds(LEFT_BOUND, TOP_BOUND, canvas.width, canvas.height)
            draw(canvas)
            return BitmapDrawable(context.resources, bitmap)
        }
        return null
    }
}
