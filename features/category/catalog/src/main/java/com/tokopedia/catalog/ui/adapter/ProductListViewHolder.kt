package com.tokopedia.catalog.ui.adapter

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog.R
import com.tokopedia.catalog.databinding.ItemCatalogPrefferedProductBinding
import com.tokopedia.catalog.domain.model.CatalogProductItem
import com.tokopedia.catalog.ui.mapper.ProductListMapper.Companion.mapperToCatalogProductModel
import com.tokopedia.utils.view.binding.viewBinding

class ProductListViewHolder(itemView: View) : AbstractViewHolder<CatalogProductItem>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_catalog_preffered_product
    }

    private val binding by viewBinding<ItemCatalogPrefferedProductBinding>()

    override fun bind(element: CatalogProductItem?) {
        val data = mapperToCatalogProductModel(element)
        binding?.productCard?.apply {
            setProductModel(data)
            setAddToCartOnClickListener {

            }
            
        }
    }
}
