package com.tokopedia.brandlist.brandlist_page.presentation.adapter.widget

import android.graphics.Canvas
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class StickyHeaderItemDecoration : RecyclerView.ItemDecoration() {

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

        val topChild = parent.findChildViewUnder(parent.paddingLeft.toFloat(), parent.paddingTop.toFloat())
                ?: return

        val topChildPosition = parent.getChildAdapterPosition(topChild)

        if (topChildPosition == RecyclerView.NO_POSITION) return
    }

    private fun getHeaderViewforItem() {

    }

    private fun drawHeader(canvas: Canvas, header: View, paddingTop: Int) {
        canvas.save()
        canvas.translate(0f, paddingTop.toFloat())
        header.draw(canvas)
        canvas.restore()
    }
}