package com.tokopedia.createpost.producttag.view.decoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.createpost.producttag.view.adapter.viewholder.ProductTagCardViewHolder

/**
 * Created By : Jonathan Darwin on May 13, 2022
 */
class ProductTagItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val offset16 = context.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val viewHolder = parent.getChildViewHolder(view)

        when(viewHolder) {
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