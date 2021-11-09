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
    var cornerRadius: Float = 0f
    private set

    fun setCornerRadius(radius: Float) {
        this.cornerRadius = radius
    }

    fun setupCorner(width: Float, height: Float) {
        cornerPath.reset()
        cornerPath.addRoundRect(
                RectF(0.0f, 0.0f, width, height),
                cornerRadius,
                cornerRadius,
                Path.Direction.CW
        )
        cornerPath.close()
    }

    fun getOutlineProvider(cornerRadius: Float): ViewOutlineProvider {
        return RoundedViewOutlineProvider(cornerRadius)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    class RoundedViewOutlineProvider(private val cornerRadius: Float) : ViewOutlineProvider() {

        override fun getOutline(view: View, outline: Outline) {
            outline.setRoundRect(0, 0, view.width, view.height, cornerRadius)
        }
    }
}