package com.tokopedia.feedcomponent.view.widget

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/** for gridLayout
  * space in px
 **/
class ItemOffsetDecoration(val space: Int) : RecyclerView.ItemDecoration(){

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect?.set(space, space, space, space)
    }
}