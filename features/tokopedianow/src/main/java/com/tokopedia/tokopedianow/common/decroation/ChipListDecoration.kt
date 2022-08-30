package com.tokopedia.tokopedianow.common.decroation

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.orZero

class ChipListDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val spacing = view.context.resources.getDimensionPixelSize(
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3
        )
        val position = parent.getChildAdapterPosition(view)
        val firstPosition = parent.adapter?.itemCount.orZero() - 1

        if (position != firstPosition) {
            outRect.bottom = spacing
            outRect.right = spacing
        }
    }
}