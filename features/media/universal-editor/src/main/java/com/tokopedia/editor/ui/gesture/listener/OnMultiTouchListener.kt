package com.tokopedia.editor.ui.gesture.listener

import android.view.View
import com.tokopedia.editor.ui.widget.DynamicTextCanvasView

interface OnMultiTouchListener {
    fun parentView(): DynamicTextCanvasView
    fun onRemoveView(view: View)
}
