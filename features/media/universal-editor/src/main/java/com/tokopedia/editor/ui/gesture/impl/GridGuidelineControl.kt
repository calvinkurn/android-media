package com.tokopedia.editor.ui.gesture.impl

import android.view.View
import com.tokopedia.editor.ui.widget.DynamicTextCanvasLayout
import com.tokopedia.editor.ui.widget.GridGuidelineView

interface GridGuidelineControl {

    fun setGridView(view: View)
    fun shouldShowVerticalLine(visible: Boolean)
    fun shouldShowHorizontalLine(visible: Boolean)
}

internal class GridGuidelineControlImpl : GridGuidelineControl {

    private var gridGuidelineView: GridGuidelineView? = null

    override fun setGridView(view: View) {
        if (gridGuidelineView == null) {
            gridGuidelineView = (view.parent as View)
                .findViewById(DynamicTextCanvasLayout.VIEW_GRID_GUIDELINE_ID)
        }
    }

    override fun shouldShowVerticalLine(visible: Boolean) {
        gridGuidelineView?.setShowVerticalLine(visible)
    }

    override fun shouldShowHorizontalLine(visible: Boolean) {
        gridGuidelineView?.setShowHorizontalLine(visible)
    }
}
