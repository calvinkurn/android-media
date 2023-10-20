package com.tokopedia.tokopedianow.category.presentation.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryQuickFilterViewHolder
import com.tokopedia.tokopedianow.common.util.ViewUtil
import com.tokopedia.tokopedianow.common.viewholder.TokoNowTickerViewHolder
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.ProductItemViewHolder
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class CategoryL2SpacingDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val firstItemViewHolder = parent.findViewHolderForLayoutPosition(0)
        val secondItemViewHolder = parent.findViewHolderForLayoutPosition(1)

        if(firstItemViewHolder is CategoryQuickFilterViewHolder && secondItemViewHolder !is ProductItemViewHolder) {
            secondItemViewHolder?.setLayoutParams {
                val spacing = ViewUtil.getDpFromDimen(
                    parent.context, unifyprinciplesR.dimen.unify_space_12).toIntSafely()
                it.bottomMargin = spacing
                it.topMargin = 0
            }
        }

        if(firstItemViewHolder is TokoNowTickerViewHolder) {
            firstItemViewHolder.setLayoutParams {
                val spacing = ViewUtil.getDpFromDimen(
                    parent.context, unifyprinciplesR.dimen.unify_space_12).toIntSafely()
                it.topMargin = spacing
            }
        }
    }

    private fun RecyclerView.ViewHolder?.setLayoutParams(
        block: (GridLayoutManager.LayoutParams) -> Unit
    ) {
        this?.let {
            val layoutParams = it.itemView.layoutParams
            val gridLayoutParams = layoutParams as? GridLayoutManager.LayoutParams

            it.itemView.layoutParams = gridLayoutParams?.apply {
                block.invoke(this)
            }
        }
    }
}
