@file:SuppressLint("ClickableViewAccessibility")

package com.tokopedia.editor.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.widget.FrameLayout.LayoutParams
import com.tokopedia.editor.ui.gesture.java.MultiTouchListener
import com.tokopedia.unifyprinciples.Typography

object AddTypography {

    fun create(context: Context, content: String): Pair<Typography, LayoutParams> {
        val text = Typography(context)

        text.setWeight(Typography.DISPLAY_3)
        text.setTextColor(Color.WHITE)
        text.isFocusable = false
        text.isFocusableInTouchMode = false
        text.gravity = Gravity.CENTER
        text.text = content

        val layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )

        layoutParams.gravity = Gravity.CENTER

        val touchListener = MultiTouchListener(context)
        text.setOnTouchListener(touchListener)

        return Pair(text, layoutParams)
    }
}
