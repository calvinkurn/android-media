package com.tokopedia.tokopedianow.shoppinglist.presentation.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.tokopedianow.common.viewholder.TokoNowDividerViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowTitleViewHolder
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.common.ShoppingListHorizontalProductCardItemViewHolder
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.main.ShoppingListTopCheckAllViewHolder
import com.tokopedia.unifycomponents.R as unifycomponentsR

class ShoppingListDecoration: RecyclerView.ItemDecoration() {
    companion object {
        const val INVALID_VIEW_TYPE = -1
        const val ADAPTER_START_INDEX = 0
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val absolutePos = parent.getChildAdapterPosition(view)
        when {
            isTopAllAddToCart(parent, absolutePos) -> {
                outRect.top = view.getDimens(unifycomponentsR.dimen.spacing_lvl2)
                outRect.bottom = view.getDimens(unifycomponentsR.dimen.unify_space_12)
                outRect.right = view.getDimens(unifycomponentsR.dimen.unify_space_16)
                outRect.left = view.getDimens(unifycomponentsR.dimen.unify_space_16)
            }
            isProductCard(parent, absolutePos) -> {
                outRect.bottom = view.getDimens(unifycomponentsR.dimen.unify_space_16)
                outRect.right = view.getDimens(unifycomponentsR.dimen.unify_space_16)
                outRect.left = view.getDimens(unifycomponentsR.dimen.unify_space_16)
            }
            isTitle(parent, absolutePos) -> {
                outRect.top = view.getDimens(unifycomponentsR.dimen.spacing_lvl2)
                outRect.bottom = view.getDimens(unifycomponentsR.dimen.unify_space_12)
                outRect.right = view.getDimens(unifycomponentsR.dimen.unify_space_16)
                outRect.left = view.getDimens(unifycomponentsR.dimen.unify_space_16)
            }
            isDivider(parent, absolutePos) -> {
                outRect.bottom = view.getDimens(unifycomponentsR.dimen.unify_space_12)
            }
        }
    }

    private fun isTopAllAddToCart(parent: RecyclerView, viewPosition: Int): Boolean = ShoppingListTopCheckAllViewHolder.LAYOUT == getRecyclerViewViewType(parent, viewPosition)

    private fun isProductCard(parent: RecyclerView, viewPosition: Int): Boolean = ShoppingListHorizontalProductCardItemViewHolder.LAYOUT == getRecyclerViewViewType(parent, viewPosition)

    private fun isTitle(parent: RecyclerView, viewPosition: Int): Boolean = TokoNowTitleViewHolder.LAYOUT == getRecyclerViewViewType(parent, viewPosition)

    private fun isDivider(parent: RecyclerView, viewPosition: Int): Boolean = TokoNowDividerViewHolder.LAYOUT == getRecyclerViewViewType(parent, viewPosition)

    private fun getRecyclerViewViewType(parent: RecyclerView, viewPosition: Int): Int {
        val adapter = parent.adapter ?: return INVALID_VIEW_TYPE
        val isInvalidPosition = viewPosition !in ADAPTER_START_INDEX until adapter.itemCount
        return if (isInvalidPosition) INVALID_VIEW_TYPE else adapter.getItemViewType(viewPosition)
    }
}
