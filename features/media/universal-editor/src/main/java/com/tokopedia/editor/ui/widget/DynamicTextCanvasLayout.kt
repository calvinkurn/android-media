@file:SuppressLint("ClickableViewAccessibility")

package com.tokopedia.editor.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import com.tokopedia.editor.R
import com.tokopedia.editor.ui.gesture.api.MultiTouchListener
import com.tokopedia.editor.ui.gesture.listener.OnGestureControl
import com.tokopedia.editor.ui.gesture.listener.OnMultiTouchListener
import com.tokopedia.editor.ui.model.InputTextModel
import com.tokopedia.editor.util.FontAlignment
import com.tokopedia.editor.util.FontAlignment.Companion.toGravity

interface DynamicTextCanvasParent {

    val gridGuidelineView: GridGuidelineView
    val deletionButtonView: View
}

class DynamicTextCanvasLayout @JvmOverloads constructor(
    private val context: Context,
    attributeSet: AttributeSet? = null
) : FrameLayout(context, attributeSet), OnMultiTouchListener, DynamicTextCanvasParent {

    /**
     * Create a grid guidelines and snap effect threshold.
     *
     * This grid guideline only provides 2 alignment:
     * 1. Center vertical alignment
     * 2. Center horizontal alignment
     *
     * The grid visibility controlled by TouchListener of textView.
     */
    override val gridGuidelineView: GridGuidelineView
        get() = GridGuidelineView(context)

    /**
     * Deletion Button uses for textView removal.
     *
     * The deletion visibility also handled by TouchListener of textView.
     * There's no action listener needed to delete. instead, the view removal
     * will be executed if the pointer position in this button area.
     */
    override val deletionButtonView: View
        get() = LayoutInflater
            .from(context)
            .inflate(DELETION_LAYOUT, this, false)

    /**
     * Collect the [InputTextModel] that already added on this container.
     */
    private val models = mutableMapOf<Int, InputTextModel>()

    private var listener: Listener? = null

    init {
        createGridGuidelineView()
        createDeletionButtonView()
    }

    fun modifySelectedText(id: Int, model: InputTextModel) {
        val view = findViewById<EditorEditTextView>(id) ?: return

        // If there's an alignment changes, we have to force update the text view
        if (view.gravity != defaultTextGravityAlignment(model.textAlign)) {
            removeView(view)
            addNewText(model)
            return
        }

        view.styleInsets(model)
        id.updateModel(model)
    }

    fun addNewText(model: InputTextModel) {
        val layoutParams = defaultLayoutParamEditTextView(model.textAlign)
        val textView = onCreateEditorEditTextView(model)

        val touchListener = MultiTouchListener(context, textView)

        // Due the Text that have been added need a deletion and grid component from parent,
        // Hence we have to delegate the parent's contract into [MultiTouchListener].
        touchListener.setParentComponent(this)

        // For now only supported to get the view removal callback
        touchListener.setOnMultiTouchListener(this)

        // Propagate each click listener of any textView were added on this container.
        touchListener.setOnGestureControl(object : OnGestureControl {
            override fun onClick() {
                listener?.onTextClick(textView, models[textView.id])
            }
        })

        textView.setOnTouchListener(touchListener)
        addView(textView, layoutParams)

        textView.id.updateModel(model)
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    override fun onRemoveView(view: View) {
        removeView(view)
    }

    private fun createGridGuidelineView() {
        gridGuidelineView.id = View.generateViewId()
        addView(gridGuidelineView, matchContentLayoutParams())
    }

    private fun createDeletionButtonView() {
        val layoutParams = wrapContentLayoutParams().also {
            it.gravity = Gravity.BOTTOM or Gravity.CENTER
            it.bottomMargin = DELETION_BOTTOM_MARGIN
        }

        deletionButtonView.id = View.generateViewId()
        addView(deletionButtonView, layoutParams)
    }

    private fun onCreateEditorEditTextView(model: InputTextModel) =
        EditorEditTextView(context).apply {
            setViewId()

            default()
            styleInsets(model)
            setAsTextView()
        }

    private fun defaultLayoutParamEditTextView(alignment: FontAlignment) =
        wrapContentLayoutParams().also {
            it.gravity = defaultTextGravityAlignment(alignment)
            it.marginStart = DEFAULT_TEXT_MARGIN
            it.marginEnd = DEFAULT_TEXT_MARGIN
        }

    private fun Int.updateModel(model: InputTextModel) {
        models[this] = model
    }

    private fun defaultTextGravityAlignment(alignment: FontAlignment): Int {
        return alignment.toGravity() or Gravity.CENTER_VERTICAL
    }

    private fun matchContentLayoutParams() =
        LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

    private fun wrapContentLayoutParams() =
        LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

    interface Listener {
        fun onTextClick(text: View, model: InputTextModel?)
    }

    companion object {
        @LayoutRes val DELETION_LAYOUT = R.layout.view_deletion_button

        private const val DEFAULT_TEXT_MARGIN = 20
        private const val DELETION_BOTTOM_MARGIN = 24
    }
}
