package com.tokopedia.homenav.category.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.category.view.adapter.model.CategoryListLoadingDataModel

class CategoryListLoadingViewHolder(
        itemView: View
): AbstractViewHolder<CategoryListLoadingDataModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_category_list_loading
    }

    override fun bind(element: CategoryListLoadingDataModel) {
    }
}