package com.tokopedia.createpost.producttag.view.decoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.createpost.producttag.view.adapter.viewholder.ProductTagCardViewHolder
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * Created By : Jonathan Darwin on May 13, 2022
 */
class ProductTagItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val offset16 = context.resources.getDimensionPixelOffset(unifyR.dimen.spacing_lvl4)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        when(parent.getChildViewHolder(view)) {
            is ProductTagCardViewHolder.Suggestion,
            is ProductTagCardViewHolder.Ticker,
            is ProductTagCardViewHolder.RecommendationTitle -> {
                outRect.left = offset16
                outRect.right = offset16
                outRect.bottom = offset16
            }
            is ProductTagCardViewHolder.Divider -> {
                outRect.bottom = offset16
            }
            is ProductTagCardViewHolder.Product -> {
                val layoutParams = view.layoutParams as StaggeredGridLayoutManager.LayoutParams

                when(layoutParams.spanIndex) {
                    0 -> outRect.left = offset16
                    1 -> outRect.right = offset16
                }
            }
        }
    }
}