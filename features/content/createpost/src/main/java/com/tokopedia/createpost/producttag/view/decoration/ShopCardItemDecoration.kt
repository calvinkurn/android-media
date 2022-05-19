package com.tokopedia.createpost.producttag.view.decoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.createpost.producttag.view.adapter.viewholder.ShopCardViewHolder
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * Created By : Jonathan Darwin on May 19, 2022
 */
class ShopCardItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val offset16 = context.resources.getDimensionPixelOffset(unifyR.dimen.spacing_lvl4)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        when(parent.getChildViewHolder(view)) {
            is ShopCardViewHolder.RecommendationTitle -> {
                outRect.left = offset16
                outRect.right = offset16
                outRect.bottom = offset16
            }
            is ShopCardViewHolder.Divider -> {
                outRect.bottom = offset16
            }
            is ShopCardViewHolder.Shop -> {
                outRect.left = offset16
                outRect.right = offset16
            }
            else -> super.getItemOffsets(outRect, view, parent, state)
        }
    }
}