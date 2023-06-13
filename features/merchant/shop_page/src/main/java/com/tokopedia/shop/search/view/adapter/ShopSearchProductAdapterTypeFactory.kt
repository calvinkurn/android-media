package com.tokopedia.shop.search.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.search.view.adapter.model.ShopSearchProductDynamicResultDataModel
import com.tokopedia.shop.search.view.adapter.model.ShopSearchProductFixedResultDataModel
import com.tokopedia.shop.search.view.adapter.viewholder.ShopSearchProductDynamicResultViewHolder
import com.tokopedia.shop.search.view.adapter.viewholder.ShopSearchProductFixResultViewHolder

class ShopSearchProductAdapterTypeFactory : BaseAdapterTypeFactory() {

    fun type(searchProductFixedResultDataModel: ShopSearchProductFixedResultDataModel): Int {
        return ShopSearchProductFixResultViewHolder.LAYOUT
    }

    fun type(searchProductDynamicResultDataModel: ShopSearchProductDynamicResultDataModel): Int {
        return ShopSearchProductDynamicResultViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ShopSearchProductFixResultViewHolder.LAYOUT -> {
                ShopSearchProductFixResultViewHolder(parent)
            }
            ShopSearchProductDynamicResultViewHolder.LAYOUT -> {
                ShopSearchProductDynamicResultViewHolder(parent)
            }
            else ->
                super.createViewHolder(parent, type)
        }
    }
}
