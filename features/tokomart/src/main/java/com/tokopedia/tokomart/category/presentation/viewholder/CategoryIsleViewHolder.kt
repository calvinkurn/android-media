package com.tokopedia.tokomart.category.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.category.presentation.model.CategoryIsleDataView

class CategoryIsleViewHolder(itemView: View): AbstractViewHolder<CategoryIsleDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokomart_category_isle
    }

    override fun bind(element: CategoryIsleDataView?) {

    }
}