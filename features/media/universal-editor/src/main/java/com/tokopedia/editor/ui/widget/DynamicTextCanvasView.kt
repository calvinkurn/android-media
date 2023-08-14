@file:SuppressLint("ClickableViewAccessibility")

package com.tokopedia.editor.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import com.tokopedia.editor.ui.gesture.java.MultiTouchListener
import com.tokopedia.editor.ui.gesture.listener.OnMultiTouchListener
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.image.ImageProcessingUtil

class DynamicTextCanvasView @JvmOverloads constructor(
    private val context: Context,
    attributeSet: AttributeSet? = null
) : FrameLayout(context, attributeSet), OnMultiTouchListener {

    private val guideline = GridGuidelineView(context)
    private lateinit var buttonView: Button

    init {
        addGridGuidelineView()
    }

    fun addText(content: String) {
        val textView = createTextView(content)

        val layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )

        layoutParams.gravity = Gravity.CENTER

        addView(textView, layoutParams)
    }

    fun addButtonView(view: Button) {
        buttonView = view
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

        text.setWeight(Typography.BOLD)
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
        text.setTextColor(Color.WHITE)
        text.isFocusable = false
        text.isFocusableInTouchMode = false
        text.gravity = Gravity.CENTER
        text.text = content

        val touchListener = MultiTouchListener(
            context,
            text,
            guideline,
            buttonView
        ).apply { setOnMultiTouchListener(this@DynamicTextCanvasView) }

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

    override fun parentView() = this

    override fun onRemoveView(view: View) {
        removeView(view)
    }
}
