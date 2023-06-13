package com.tokopedia.tokopedianow.common.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

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
        outRect.bottom = spacing
        outRect.right = spacing
    }
}
