package com.tokopedia.product.addedit.common.util

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewItemDecoration(private val context: Context) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.left = context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4).toInt()
        }
    }

}