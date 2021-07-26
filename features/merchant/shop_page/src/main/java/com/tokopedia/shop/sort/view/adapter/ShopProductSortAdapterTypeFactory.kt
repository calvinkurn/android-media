package com.tokopedia.shop.sort.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.sort.view.adapter.viewholder.ShopProductSortViewHolder
import com.tokopedia.shop.sort.view.model.ShopProductSortModel

/**
 * Created by alvarisi on 12/7/17.
 */
class ShopProductSortAdapterTypeFactory : BaseAdapterTypeFactory() {
    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*> {
        return if (viewType == ShopProductSortViewHolder.Companion.LAYOUT) {
            ShopProductSortViewHolder(view)
        } else {
            super.createViewHolder(view, viewType)
        }
    }

    fun type(shopProductFilterModel: ShopProductSortModel?): Int {
        return ShopProductSortViewHolder.Companion.LAYOUT
    }
}