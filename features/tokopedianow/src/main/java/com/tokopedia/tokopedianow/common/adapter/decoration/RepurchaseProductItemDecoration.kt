package com.tokopedia.tokopedianow.common.adapter.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.tokopedianow.R

class RepurchaseProductItemDecoration: RecyclerView.ItemDecoration() {

    companion object {
        private const val FIRST_POSITION_INDEX = 0
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val itemPosition = parent.getChildAdapterPosition(view)
        if (itemPosition == RecyclerView.NO_POSITION) return

        val itemCount = parent.adapter?.itemCount.orZero()
        val lastItemIndex = itemCount - 1

        when (itemPosition) {
            FIRST_POSITION_INDEX -> {
                outRect.left = view.context.resources
                    .getDimensionPixelSize(R.dimen.tokopedianow_repurchase_product_card_spacing)
                outRect.right = view.context.resources
                    .getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)
            }
            lastItemIndex -> {
                outRect.right = view.context.resources
                    .getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)
            }
            else -> {
                outRect.right = view.context.resources
                    .getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)
            }
        }
    }
}
