package com.tokopedia.editor.ui.gesture.listener

import android.view.View

interface MultiTouchListener {
    fun onRemoveView(view: View)
    fun onViewClick(view: View)

    fun startViewDrag()
    fun endViewDrag()
}
