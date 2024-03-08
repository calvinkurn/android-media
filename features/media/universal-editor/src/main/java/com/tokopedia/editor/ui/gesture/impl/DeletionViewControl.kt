package com.tokopedia.editor.ui.gesture.impl

import android.view.View
import com.tokopedia.editor.ui.widget.DynamicTextCanvasLayout
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show

interface DeletionViewControl {

    fun setDeletionView(view: View)
    fun showDeletionViewButton()
    fun hideDeletionViewButton()
    fun hasPointerLocationWithinView(x: Int, y: Int): Boolean
}

internal class DeletionViewControlImpl : DeletionViewControl {

    private var deletionView: View? = null

    override fun setDeletionView(view: View) {
        if (deletionView == null) {
            deletionView = (view.parent as View)
                .findViewById(DynamicTextCanvasLayout.VIEW_DELETION_BUTTON_ID)
        }
    }

    override fun showDeletionViewButton() {
        deletionView?.show()
    }

    override fun hideDeletionViewButton() {
        deletionView?.hide()
    }

    override fun hasPointerLocationWithinView(x: Int, y: Int): Boolean {
        val location = IntArray(2)
        deletionView?.getLocationOnScreen(location)

        val width = deletionView?.width.orZero()
        val height = deletionView?.height.orZero()

        return ((x >= location[0]) && (x <= location[0] + width)) &&
            ((y >= location[1]) && (y <= location[1] + height))
    }
}
