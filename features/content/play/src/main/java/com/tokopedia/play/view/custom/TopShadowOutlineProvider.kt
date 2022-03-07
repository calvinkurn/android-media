package com.tokopedia.play.view.custom

import android.graphics.Outline
import android.graphics.Rect
import android.os.Build
import android.view.View
import android.view.ViewOutlineProvider
import androidx.annotation.RequiresApi

/**
 * Created by jegul on 11/03/20
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class TopShadowOutlineProvider : ViewOutlineProvider() {

    override fun getOutline(view: View, outline: Outline) {
        val elevation = view.elevation.toInt()

        outline.setRect(
                Rect(-elevation, -elevation*2, view.width + elevation, view.height + elevation)
        )
    }
}