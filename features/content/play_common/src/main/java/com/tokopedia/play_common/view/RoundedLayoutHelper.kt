package com.tokopedia.play_common.view

import android.graphics.Outline
import android.graphics.Path
import android.graphics.RectF
import android.os.Build
import android.view.View
import android.view.ViewOutlineProvider
import androidx.annotation.RequiresApi

/**
 * Created by jegul on 30/06/21
 */
class RoundedLayoutHelper {

    val cornerPath = Path()
    var cornerRadii = FloatArray(CORNER_RADII_SIZE) { NO_RADIUS }
    private set

    fun setCornerRadius(radius: Float) {
        setCornerRadius(radius, radius, radius, radius)
    }

    fun setCornerRadius(
        topLeft: Float = cornerRadii.getOrElse(0) { NO_RADIUS },
        topRight: Float = cornerRadii.getOrElse(2) { NO_RADIUS },
        bottomLeft: Float = cornerRadii.getOrElse(4) { NO_RADIUS },
        bottomRight: Float = cornerRadii.getOrElse(6) { NO_RADIUS },
    ) {
        this.cornerRadii = floatArrayOf(
            topLeft,
            topLeft,
            topRight,
            topRight,
            bottomLeft,
            bottomLeft,
            bottomRight,
            bottomRight
        )
    }

    fun setupCorner(width: Float, height: Float) {
        cornerPath.reset()
        cornerPath.addRoundRect(
                RectF(0.0f, 0.0f, width, height),
                cornerRadii,
                Path.Direction.CW
        )
        cornerPath.close()
    }

    fun getOutlineProvider(): ViewOutlineProvider {
        return RoundedViewOutlineProvider(
            if (cornerRadii.any { it == NO_RADIUS }) NO_RADIUS
            else cornerRadii.minOrNull() ?: NO_RADIUS
        )
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    class RoundedViewOutlineProvider(private val cornerRadius: Float) : ViewOutlineProvider() {

        override fun getOutline(view: View, outline: Outline) {
            outline.setRoundRect(0, 0, view.width, view.height, cornerRadius)
        }
    }

    companion object {
        private const val NO_RADIUS = 0f

        private const val CORNER_RADII_SIZE = 8
    }
}