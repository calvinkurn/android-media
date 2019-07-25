package com.tokopedia.feedcomponent.view.widget

import android.graphics.Rect
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View

/** for gridLayout
  * space in px
 **/
class ItemOffsetDecoration(val space: Int, val totalItem: Int) : RecyclerView.ItemDecoration(){

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val position= parent.getChildAdapterPosition(view)
        val spanCount = 6 / (parent.layoutManager as GridLayoutManager).spanSizeLookup.getSpanSize(position)
        val column = position % spanCount

        if (totalItem == 5) {
            when (position) {
                0 -> outRect.right = getRightSpace(space, column, spanCount)
                1 -> outRect.left = getLeftSpace(space, column, spanCount)
                2 -> {
                    outRect.top = getTopSpace(space)
                    outRect.right = getRightSpace(space, 0, spanCount)
                }
                3 -> {
                    outRect.top = getTopSpace(space)
                    outRect.left = getLeftSpace(space, 1, spanCount)
                    outRect.right = getRightSpace(space, 1, spanCount)
                }
                4 -> {
                    outRect.top = getTopSpace(space)
                    outRect.left = getLeftSpace(space, 2, spanCount)
                }
            }
        } else {
            outRect.left = space * column / spanCount
            outRect.right = space - (column + 1) * space / spanCount
            if (position >= spanCount) {
                outRect.top = space
            }
        }
    }

    private fun getLeftSpace(space: Int, column: Int, spanCount: Int): Int {
        return space * column / spanCount
    }

    private fun getRightSpace(space: Int, column: Int, spanCount: Int): Int {
        return space - (column + 1) * space / spanCount
    }

    private fun getTopSpace(space: Int): Int {
        return space
    }
}