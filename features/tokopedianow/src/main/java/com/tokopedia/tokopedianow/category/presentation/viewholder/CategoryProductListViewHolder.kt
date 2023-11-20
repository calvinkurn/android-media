package com.tokopedia.tokopedianow.category.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryProductListUiModel
import com.tokopedia.tokopedianow.R

class CategoryProductListViewHolder(
    itemView: View
): AbstractViewHolder<CategoryProductListUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_category_product_list_placeholder
    }

    override fun bind(element: CategoryProductListUiModel?) {

    }
}
