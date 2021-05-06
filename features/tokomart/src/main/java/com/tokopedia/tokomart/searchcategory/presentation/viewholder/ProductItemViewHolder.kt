package com.tokopedia.tokomart.searchcategory.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomart.searchcategory.presentation.model.ProductItemDataView

class ProductItemViewHolder(itemView: View): AbstractViewHolder<ProductItemDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = 0
    }

    override fun bind(element: ProductItemDataView?) {
        element ?: return
    }
}