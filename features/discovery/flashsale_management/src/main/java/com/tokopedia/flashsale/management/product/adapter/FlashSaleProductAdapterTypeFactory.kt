package com.tokopedia.flashsale.management.product.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flashsale.management.product.adapter.viewholder.FlashSaleProductViewHolder
import com.tokopedia.flashsale.management.product.data.FlashSaleProductItem
import com.tokopedia.flashsale.management.product.data.FlashSaleSubmissionProductItem

class FlashSaleProductAdapterTypeFactory: BaseAdapterTypeFactory(){

    fun type(model: FlashSaleProductItem): Int {
        return FlashSaleProductViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type){
            FlashSaleProductViewHolder.LAYOUT -> FlashSaleProductViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}