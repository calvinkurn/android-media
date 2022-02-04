package com.tokopedia.play.broadcaster.setup.product.view.itemdecoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifyprinciples.R as unifyR
import androidx.recyclerview.widget.StaggeredGridLayoutManager




/**
 * Created by kenny.hadisaputra on 28/01/22
 */
class ProductListItemDecoration(
    context: Context,
) : RecyclerView.ItemDecoration() {

    private val offset8 = context.resources.getDimensionPixelOffset(unifyR.dimen.spacing_lvl3)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val lp = view.layoutParams as StaggeredGridLayoutManager.LayoutParams
        val spanIndex = lp.spanIndex

        if (spanIndex == 0) {
            outRect.right = offset8
        } else super.getItemOffsets(outRect, view, parent, state)

        outRect.bottom = offset8
    }
}