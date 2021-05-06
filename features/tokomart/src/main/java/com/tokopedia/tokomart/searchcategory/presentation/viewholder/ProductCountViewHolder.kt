package com.tokopedia.tokomart.searchcategory.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomart.searchcategory.presentation.model.ProductCountDataView

class ProductCountViewHolder(itemView: View): AbstractViewHolder<ProductCountDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = 0
    }

    override fun bind(element: ProductCountDataView?) {

    }
}