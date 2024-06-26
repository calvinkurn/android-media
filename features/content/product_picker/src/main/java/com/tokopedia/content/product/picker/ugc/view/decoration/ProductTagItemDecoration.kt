package com.tokopedia.content.product.picker.ugc.view.decoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.content.product.picker.ugc.view.adapter.viewholder.ProductTagCardViewHolder
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created By : Jonathan Darwin on May 13, 2022
 */
class ProductTagItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val offset16 = context.resources.getDimensionPixelOffset(unifyprinciplesR.dimen.spacing_lvl4)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        when(parent.getChildViewHolder(view)) {
            is ProductTagCardViewHolder.Suggestion,
            is ProductTagCardViewHolder.Ticker -> {
                outRect.bottom = offset16
            }
        }
    }
}
