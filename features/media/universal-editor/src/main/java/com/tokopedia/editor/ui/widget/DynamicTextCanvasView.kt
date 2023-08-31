@file:SuppressLint("ClickableViewAccessibility")

package com.tokopedia.editor.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.editor.ui.gesture.api.MultiTouchListener
import com.tokopedia.editor.ui.gesture.listener.OnGestureControl
import com.tokopedia.editor.ui.gesture.listener.OnMultiTouchListener
import com.tokopedia.editor.ui.main.uimodel.InputTextUiModel
import com.tokopedia.editor.ui.model.InputTextModel
import com.tokopedia.editor.util.FontAlignment
import com.tokopedia.iconunify.IconUnify

class DynamicTextCanvasView @JvmOverloads constructor(
    private val context: Context,
    attributeSet: AttributeSet? = null
) : FrameLayout(context, attributeSet), OnMultiTouchListener {

    private val guideline = GridGuidelineView(context)
    private lateinit var buttonView: IconUnify
    private var listener: Listener? = null

    private val models = mutableMapOf<Int, InputTextModel>()

    init {
        addGridGuidelineView()
    }

    fun editText(model: InputTextUiModel) {
        model.model?.let {
            findViewById<EditorEditTextView>(model.typographyId)
                .styleInsets(it)

            models[model.typographyId] = it
        }
    }

    fun addText(model: InputTextModel) {
        val textView = EditorEditTextView(context)
        textView.setViewId()
        textView.styleInsets(model)
        textView.setAsTextView()

        val touchListener = MultiTouchListener(context, textView, buttonView).apply {
            setOnMultiTouchListener(this@DynamicTextCanvasView)
        }

        touchListener.setOnGestureControl(object : OnGestureControl {
            override fun onClick() {
                listener?.onTextClick(textView, models[textView.id])
            }
        })

        textView.setOnTouchListener(touchListener)

        val layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        layoutParams.gravity = textView.textAlignment(model.textAlign)

        addView(textView, layoutParams)
        models[textView.id] = model
    }

    fun addButtonView(view: IconUnify) {
        buttonView = view
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    private fun addGridGuidelineView() {
        guideline.id = GRID_ID

        val layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )

        addView(guideline, layoutParams)
    }

    override fun onRemoveView(view: View) {
        removeView(view)
    }

    interface Listener {
        fun onTextClick(text: View, model: InputTextModel?)
    }

    companion object {
        const val GRID_ID = 123
    }
}
