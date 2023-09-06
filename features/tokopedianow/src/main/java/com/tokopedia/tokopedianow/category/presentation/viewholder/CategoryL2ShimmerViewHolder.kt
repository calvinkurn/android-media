package com.tokopedia.tokopedianow.category.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryL2ShimmerUiModel
import com.tokopedia.tokopedianow.R

class CategoryL2ShimmerViewHolder(
    itemView: View
): AbstractViewHolder<CategoryL2ShimmerUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_category_l2_shimmering
    }

    override fun bind(element: CategoryL2ShimmerUiModel) {
    }
}
