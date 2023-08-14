package com.tokopedia.home_component.widget.shop_flash_sale.item

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactoryImpl

open class ShopFlashSaleItemTypeFactoryImpl: ShopFlashSaleItemTypeFactory, CommonCarouselProductCardTypeFactoryImpl() {

    override fun type(dataModel: ShopFlashSaleItemShimmerDataModel): Int {
        return ShopFlashSaleShimmerViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*> {
        return if(viewType == ShopFlashSaleShimmerViewHolder.LAYOUT) {
            ShopFlashSaleShimmerViewHolder(view)
        } else super.createViewHolder(view, viewType)
    }
}
