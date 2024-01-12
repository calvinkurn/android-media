package com.tokopedia.catalog.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tkpd.atcvariant.view.adapter.AtcVariantVisitable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.catalog.domain.model.CatalogProductItem
import com.tokopedia.productcard.ProductCardModel

class CatalogProductListDiffer : DiffUtil.ItemCallback<Visitable<*>>() {
    override fun areItemsTheSame(oldItem: Visitable<*>, newItem: Visitable<*>): Boolean {
        return (oldItem as CatalogProductItem).name == (newItem as CatalogProductItem).name &&
            oldItem.shop.name == newItem.shop.name
    }

    override fun areContentsTheSame(oldItem: Visitable<*>, newItem: Visitable<*>): Boolean {
        return (oldItem as CatalogProductItem) == (newItem as CatalogProductItem)
    }
}

