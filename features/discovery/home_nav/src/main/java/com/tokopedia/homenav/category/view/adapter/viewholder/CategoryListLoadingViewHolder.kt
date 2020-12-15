package com.tokopedia.homenav.category.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.category.view.adapter.model.CategoryListLoadingViewModel

class CategoryListLoadingViewHolder(
        itemView: View
): AbstractViewHolder<CategoryListLoadingViewModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_category_list_loading
    }

    override fun bind(element: CategoryListLoadingViewModel) {
    }
}