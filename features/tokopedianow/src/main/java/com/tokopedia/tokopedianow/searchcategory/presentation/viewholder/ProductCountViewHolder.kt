package com.tokopedia.tokopedianow.searchcategory.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowSearchCategoryProductCountBinding
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductCountDataView
import com.tokopedia.utils.view.binding.viewBinding

class ProductCountViewHolder(itemView: View): AbstractViewHolder<ProductCountDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_search_category_product_count
    }

    private var binding: ItemTokopedianowSearchCategoryProductCountBinding? by viewBinding()

    override fun bind(element: ProductCountDataView?) {
        element ?: return

        binding?.tokoNowSearchCategoryProductCount?.text = getString(R.string.tokopedianow_search_category_product_count_template, element.totalDataText)
    }
}