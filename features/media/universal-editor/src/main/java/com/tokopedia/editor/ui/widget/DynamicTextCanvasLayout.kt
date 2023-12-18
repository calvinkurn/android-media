@file:SuppressLint("ClickableViewAccessibility")

package com.tokopedia.editor.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import com.tokopedia.editor.R
import com.tokopedia.editor.ui.gesture.api.V1MultiTouchListener
import com.tokopedia.editor.ui.gesture.impl.MultiGestureListener
import com.tokopedia.editor.ui.gesture.listener.MultiTouchListener
import com.tokopedia.editor.ui.model.InputTextModel
import com.tokopedia.editor.util.FontAlignment
import com.tokopedia.editor.util.FontAlignment.Companion.toGravity
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.showWithCondition

class DynamicTextCanvasLayout @JvmOverloads constructor(
    private val context: Context,
    attributeSet: AttributeSet? = null
) : FrameLayout(context, attributeSet), MultiTouchListener {

    /**
     * Create a grid guidelines and snap effect threshold.
     *
     * This grid guideline only provides 2 alignment:
     * 1. Center vertical alignment
     * 2. Center horizontal alignment
     *
     * The grid visibility controlled by TouchListener of textView.
     */
    private val gridGuidelineView = GridGuidelineView(context)

    /**
     * Deletion Button uses for textView removal.
     *
     * The deletion visibility also handled by TouchListener of textView.
     * There's no action listener needed to delete. instead, the view removal
     * will be executed if the pointer position in this button area.
     */
    private val deletionButtonView = LayoutInflater
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

    fun hasTextAdded() = models.isNotEmpty()

    fun addOrEditText(viewId: Int, model: InputTextModel) {
        if (viewId != -1) {
            editText(viewId, model)
            return
        }

        addText(model)
    }

    fun setTextVisibility(viewId: Int, isShown: Boolean) {
        try {
            findViewById<EditorEditTextView>(viewId)
                .showWithCondition(isShown)
        } catch (ignored: Throwable) {}
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    fun exportAsBitmap(): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        draw(canvas)
        return bitmap
    }

    fun viewsCleanUp() {
        val gridView = findViewById<GridGuidelineView>(VIEW_GRID_GUIDELINE_ID)
        val deleteView = findViewById<View>(VIEW_DELETION_BUTTON_ID)

        gridView.hide()
        deleteView.hide()
    }

    override fun onRemoveView(view: View) {
        models.remove(view.id)
        removeView(view)
    }

    override fun onViewClick(view: View) {
        listener?.onTextViewClick(view, models[view.id])
    }

    override fun startViewDrag() {
        listener?.startViewDrag()
    }

    override fun endViewDrag() {
        listener?.endViewDrag()
    }

    private fun editText(id: Int, model: InputTextModel) {
        val view = findViewById<EditorEditTextView>(id) ?: return

        // If there's an alignment changes, we have to force update the text view
        if (models[view.id]?.textAlign != model.textAlign) {
            removeView(view)
            addText(model)
            return
        }

        view.styleInsets(model)
        id.updateModel(model)
    }

    private fun addText(model: InputTextModel) {
        val layoutParams = defaultLayoutParamEditTextView(model.textAlign)
        val textView = EditorEditTextView(context)

        textView.setViewId()
        textView.default()
        textView.styleInsets(model)
        textView.setAsTextView()

        if (newApiEnabled()) {
            newGestureListener(textView)
        } else {
            oldGestureListener(textView)
        }

        addView(textView, layoutParams)
        textView.id.updateModel(model)
    }

    // hansel-able
    private fun newApiEnabled() = true

    private fun newGestureListener(textView: EditorEditTextView) {
        val listener = MultiGestureListener(textView, this)
        textView.setOnTouchListener(listener)
    }

    // keep the old one for backward compatibility nor fallback mitigation for a new one.
    private fun oldGestureListener(textView: EditorEditTextView) {
        val touchListener = V1MultiTouchListener(context, textView)

        touchListener.setOnMultiTouchListener(this)
        touchListener.setOnGestureControl {
            listener?.onTextViewClick(textView, models[textView.id])
        }

        textView.setOnTouchListener(touchListener)
    }

    private fun createGridGuidelineView() {
        gridGuidelineView.id = VIEW_GRID_GUIDELINE_ID
        addView(gridGuidelineView, matchContentLayoutParams())
    }

    private fun createDeletionButtonView() {
        val layoutParams = wrapContentLayoutParams().also {
            it.gravity = Gravity.BOTTOM or Gravity.CENTER
            it.bottomMargin = DELETION_BOTTOM_MARGIN
        }

        deletionButtonView.id = VIEW_DELETION_BUTTON_ID
        addView(deletionButtonView, layoutParams)
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
        fun onTextViewClick(text: View, model: InputTextModel?)
        fun startViewDrag()
        fun endViewDrag()
    }

    companion object {
        @LayoutRes
        val DELETION_LAYOUT = R.layout.view_deletion_button

        const val VIEW_GRID_GUIDELINE_ID = 123
        const val VIEW_DELETION_BUTTON_ID = 456

        private const val DEFAULT_TEXT_MARGIN = 20
        private const val DELETION_BOTTOM_MARGIN = 32
    }
}
