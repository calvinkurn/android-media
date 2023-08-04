@file:SuppressLint("ClickableViewAccessibility")

package com.tokopedia.editor.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.widget.FrameLayout
import com.tokopedia.editor.ui.gesture.java.MultiTouchListener
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.image.ImageProcessingUtil

class DynamicTextCanvasView @JvmOverloads constructor(
    private val context: Context,
    attributeSet: AttributeSet? = null
) : FrameLayout(context, attributeSet) {

    private val guideline = GridGuidelineView(context)

    init {
        addGridGuidelineView()
    }

    fun addText(content: String) {
        val textView = createTextView(content)

        val layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )

        layoutParams.gravity = Gravity.CENTER

        addView(textView, layoutParams)
    }

    // TODO: just for POC
    fun generateAsFile(): String {
        return ImageProcessingUtil
            .writeImageToTkpdPath(generateCanvas(), Bitmap.CompressFormat.PNG)
            ?.path ?: ""
    }

    // TODO: just for POC
    private fun generateCanvas(): Bitmap {
        val bitmap = Bitmap.createBitmap(
            layoutParams.width,
            layoutParams.height,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        layout(left, top, right, bottom)
        draw(canvas)
        return bitmap
    }

    private fun createTextView(content: String): Typography {
        val text = Typography(context)

        text.setWeight(Typography.DISPLAY_3)
        text.setTextColor(Color.WHITE)
        text.isFocusable = false
        text.isFocusableInTouchMode = false
        text.gravity = Gravity.CENTER
        text.text = content

        val touchListener = MultiTouchListener(context, guideline)
        text.setOnTouchListener(touchListener)

        return text
    }

    private fun addGridGuidelineView() {
        val layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )

        addView(guideline, layoutParams)
    }

}
