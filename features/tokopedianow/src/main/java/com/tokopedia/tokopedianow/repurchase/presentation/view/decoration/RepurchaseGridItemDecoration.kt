package com.tokopedia.tokopedianow.repurchase.presentation.view.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.tokopedianow.common.viewholder.TokoNowChooseAddressWidgetViewHolder
import com.tokopedia.tokopedianow.repurchase.presentation.viewholder.RepurchaseProductViewHolder

class RepurchaseGridItemDecoration : RecyclerView.ItemDecoration() {

    companion object {
        private const val LEFT_SPAN_INDEX = 0
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val resources = view.context.resources

        when (parent.getChildViewHolder(view)) {
            is TokoNowChooseAddressWidgetViewHolder -> {
                val verticalSpacing = resources.getDimensionPixelSize(
                    com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3
                )
                outRect.top = verticalSpacing
                outRect.bottom = verticalSpacing
            }
            is RepurchaseProductViewHolder -> {
                val horizontalSpacing = resources.getDimensionPixelSize(
                    com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3
                )
                val lp = view.layoutParams as? StaggeredGridLayoutManager.LayoutParams
                val spanIndex = lp?.spanIndex.orZero()
                val positionLeft = spanIndex == LEFT_SPAN_INDEX

                if (positionLeft) {
                    outRect.left = horizontalSpacing
                } else {
                    outRect.right = horizontalSpacing
                }
            }
        }
    }
}