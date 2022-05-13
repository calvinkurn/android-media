package com.tokopedia.createpost.producttag.view.decoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.createpost.producttag.view.adapter.viewholder.ProductTagCardViewHolder

/**
 * Created By : Jonathan Darwin on May 13, 2022
 */
class ProductTagItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val offset16 = context.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val viewHolder = parent.getChildViewHolder(view)

        if(viewHolder is ProductTagCardViewHolder.Suggestion ||
            viewHolder is ProductTagCardViewHolder.Ticker) {
            outRect.bottom = offset16
        } else super.getItemOffsets(outRect, view, parent, state)
    }
}