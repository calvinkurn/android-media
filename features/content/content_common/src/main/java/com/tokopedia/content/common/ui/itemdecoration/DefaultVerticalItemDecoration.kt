package com.tokopedia.content.common.ui.itemdecoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.orZero

/**
 * Created by jegul on 04/10/21
 */
class DefaultVerticalItemDecoration(
    private val spacing: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildLayoutPosition(view)

        if (position < parent.adapter?.itemCount.orZero()) outRect.top = spacing
        else super.getItemOffsets(outRect, view, parent, state)
    }
}