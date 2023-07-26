package com.tokopedia.buy_more_get_more.presentation.sort.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buy_more_get_more.presentation.sort.adapter.viewholder.ShopProductSortViewHolder
import com.tokopedia.buy_more_get_more.presentation.sort.model.ShopProductSortModel



class ShopProductSortAdapterTypeFactory : BaseAdapterTypeFactory() {
    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*> {
        return if (viewType == ShopProductSortViewHolder.LAYOUT) {
            ShopProductSortViewHolder(view)
        } else {
            super.createViewHolder(view, viewType)
        }
    }

    fun type(shopProductFilterModel: ShopProductSortModel?): Int {
        return ShopProductSortViewHolder.LAYOUT
    }
}
