package com.tokopedia.play_common.view

import android.graphics.LinearGradient
import android.graphics.Shader
import android.widget.TextView

/**
 * @author by astidhiyaa on 12/04/22
 */

fun TextView.setTextGradient(colors: IntArray) {
    val width = paint.measureText(text.toString())
    setTextColor(colors.first())
    paint.shader = LinearGradient(
        0f, 0f, width, textSize, colors, null, Shader.TileMode.REPEAT
    )
}
