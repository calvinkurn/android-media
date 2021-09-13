package com.tokopedia.tokopedianow.recentpurchase.presentation.view.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.tokopedianow.common.viewholder.TokoNowChooseAddressWidgetViewHolder
import com.tokopedia.tokopedianow.recentpurchase.presentation.viewholder.RepurchaseProductViewHolder

class RepurchaseGridItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val resources = view.context.resources
        val viewHolder = parent.getChildViewHolder(view)
        val itemView = viewHolder.itemView

        when (viewHolder) {
            is TokoNowChooseAddressWidgetViewHolder -> {
                val verticalSpacing = resources.getDimensionPixelSize(
                    com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3
                )
                outRect.top = verticalSpacing
                outRect.bottom = verticalSpacing
            }
            is RepurchaseProductViewHolder -> {
                val horizontalSpacing = resources.getDimensionPixelSize(
                    com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4
                )
                val lp = view.layoutParams as? StaggeredGridLayoutManager.LayoutParams
                val spanIndex = lp?.spanIndex.orZero()
                val positionLeft = spanIndex == 0

                if (positionLeft) {
                    lp?.marginStart = horizontalSpacing
                } else {
                    lp?.marginEnd = horizontalSpacing
                }

                itemView.layoutParams = lp
            }
        }
    }
}