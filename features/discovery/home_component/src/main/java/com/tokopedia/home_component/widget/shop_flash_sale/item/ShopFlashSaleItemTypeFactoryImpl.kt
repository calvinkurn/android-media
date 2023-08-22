package com.tokopedia.home_component.widget.shop_flash_sale.item

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactoryImpl
import com.tokopedia.home_component.widget.shop_flash_sale.ShopFlashSaleListener

open class ShopFlashSaleItemTypeFactoryImpl(
    listener: ShopFlashSaleListener,
): ShopFlashSaleItemTypeFactory, CommonCarouselProductCardTypeFactoryImpl(listener = listener) {

    override fun type(dataModel: ProductCardGridShimmerDataModel): Int {
        return ProductCardGridShimmerViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*> {
        return if(viewType == ProductCardGridShimmerViewHolder.LAYOUT) {
            ProductCardGridShimmerViewHolder(view)
        } else super.createViewHolder(view, viewType)
    }
}
