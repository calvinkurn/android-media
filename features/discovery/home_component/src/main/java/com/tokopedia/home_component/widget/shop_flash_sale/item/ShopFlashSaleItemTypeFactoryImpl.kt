package com.tokopedia.home_component.widget.shop_flash_sale.item

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactoryImpl
import com.tokopedia.home_component.widget.shop_flash_sale.tab.ShopFlashSaleShopListener
import com.tokopedia.home_component.widget.shop_flash_sale.ShopFlashSaleWidgetListener

internal open class ShopFlashSaleItemTypeFactoryImpl(
    listener: ShopFlashSaleWidgetListener,
    private val internalListener: ShopFlashSaleShopListener,
): ShopFlashSaleItemTypeFactory, CommonCarouselProductCardTypeFactoryImpl(listener = listener) {

    override fun type(dataModel: ShopFlashSaleProductGridShimmerDataModel): Int {
        return ShopFlashSaleProductGridShimmerViewHolder.LAYOUT
    }

    override fun type(dataModel: ShopFlashSaleErrorDataModel): Int {
        return ShopFlashSaleErrorViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*> {
        return when(viewType) {
            ShopFlashSaleProductGridShimmerViewHolder.LAYOUT -> ShopFlashSaleProductGridShimmerViewHolder(view)
            ShopFlashSaleErrorViewHolder.LAYOUT -> ShopFlashSaleErrorViewHolder(view, internalListener)
            else -> super.createViewHolder(view, viewType)
        }
    }
}
