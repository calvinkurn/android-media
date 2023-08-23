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
import com.tokopedia.editor.ui.gesture.listener.OnGestureControl
import com.tokopedia.editor.ui.gesture.listener.OnMultiTouchListener
import com.tokopedia.editor.ui.model.InputTextModel
import com.tokopedia.editor.util.FontAlignment
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.image.ImageProcessingUtil

class DynamicTextCanvasView @JvmOverloads constructor(
    private val context: Context,
    attributeSet: AttributeSet? = null
) : FrameLayout(context, attributeSet), OnMultiTouchListener {

    private val guideline = GridGuidelineView(context)
    private lateinit var buttonView: IconUnify
    private var listener: Listener? = null

    init {
        addGridGuidelineView()
    }

    fun editText(currentString: String, model: InputTextModel) {
        for (index in 0..childCount) {
            val view = getChildAt(index)
            if (view is Typography) {
                if (view.text == currentString) {
                    view.text = model.text
                }
            }
        }
    }

    fun addText(model: InputTextModel) {
        val textView = createTextView(model)

        val layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )

        layoutParams.gravity = textAlignment(model.textAlign)

        addView(textView, layoutParams)
    }

    fun addButtonView(view: IconUnify) {
        buttonView = view
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    private fun textAlignment(alignment: FontAlignment): Int {
        return when (alignment) {
            FontAlignment.CENTER -> Gravity.CENTER
            FontAlignment.LEFT -> Gravity.START
            FontAlignment.RIGHT -> Gravity.END
        }
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

    private fun createTextView(model: InputTextModel): Typography {
        val text = Typography(context)

        text.setWeight(Typography.BOLD)
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
        text.setTextColor(model.textColor)
        text.isFocusable = false
        text.isFocusableInTouchMode = false
        text.gravity = textAlignment(model.textAlign)
        text.text = model.text

        val touchListener = MultiTouchListener(
            context,
            text,
            guideline,
            buttonView
        ).apply { setOnMultiTouchListener(this@DynamicTextCanvasView) }

        touchListener.setOnGestureControl(object : OnGestureControl {
            override fun onClick() {
                listener?.onTextClick(text, model)
            }

            override fun onDown() {}

            override fun onLongClick() {}
        })

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

    interface Listener {
        fun onTextClick(text: Typography, model: InputTextModel)
    }
}
