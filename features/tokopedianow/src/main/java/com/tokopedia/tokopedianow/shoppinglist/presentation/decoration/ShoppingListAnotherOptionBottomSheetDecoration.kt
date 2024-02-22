package com.tokopedia.tokopedianow.shoppinglist.presentation.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.tokopedianow.common.viewholder.TokoNowErrorViewHolder
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.common.ShoppingListHorizontalProductCardItemViewHolder
import com.tokopedia.tokopedianow.shoppinglist.util.Constant.ADAPTER_START_INDEX
import com.tokopedia.tokopedianow.shoppinglist.util.Constant.INVALID_INDEX
import com.tokopedia.unifycomponents.R as unifycomponentsR

class ShoppingListAnotherOptionBottomSheetDecoration: RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val absolutePos = parent.getChildAdapterPosition(view)
        when {
            isProductCard(parent, absolutePos) -> {
                outRect.bottom = view.getDimens(unifycomponentsR.dimen.unify_space_16)
                outRect.right = view.getDimens(unifycomponentsR.dimen.unify_space_16)
                outRect.left = view.getDimens(unifycomponentsR.dimen.unify_space_16)
            }
            isError(parent, absolutePos) -> {
                outRect.bottom = view.getDimens(unifycomponentsR.dimen.unify_space_16)
            }
        }
    }

    private fun isProductCard(parent: RecyclerView, viewPosition: Int): Boolean = ShoppingListHorizontalProductCardItemViewHolder.LAYOUT == getRecyclerViewViewType(parent, viewPosition)

    private fun isError(parent: RecyclerView, viewPosition: Int): Boolean = TokoNowErrorViewHolder.LAYOUT == getRecyclerViewViewType(parent, viewPosition)

    private fun getRecyclerViewViewType(parent: RecyclerView, viewPosition: Int): Int {
        val adapter = parent.adapter ?: return INVALID_INDEX
        val isInvalidPosition = viewPosition !in ADAPTER_START_INDEX until adapter.itemCount
        return if (isInvalidPosition) INVALID_INDEX else adapter.getItemViewType(viewPosition)
    }
}
