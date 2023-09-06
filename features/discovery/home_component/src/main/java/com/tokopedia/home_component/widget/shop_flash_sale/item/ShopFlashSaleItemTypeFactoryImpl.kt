package com.tokopedia.home_component.widget.shop_flash_sale.item

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactory
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactoryImpl
import com.tokopedia.home_component.widget.shop_flash_sale.ShopFlashSaleWidgetListener

internal open class ShopFlashSaleItemTypeFactoryImpl(
    listener: ShopFlashSaleWidgetListener,
    private val errorListener: ShopFlashSaleErrorListener,
    private val commonCarouselTypeFactory: CommonCarouselProductCardTypeFactory = CommonCarouselProductCardTypeFactoryImpl(listener = listener)
): ShopFlashSaleItemTypeFactory,
    CommonCarouselProductCardTypeFactory by commonCarouselTypeFactory {

    override fun type(dataModel: ShopFlashSaleProductGridShimmerDataModel): Int {
        return ShopFlashSaleProductGridShimmerViewHolder.LAYOUT
    }

    override fun type(dataModel: ShopFlashSaleErrorDataModel): Int {
        return ShopFlashSaleErrorViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*> {
        return when(viewType) {
            ShopFlashSaleProductGridShimmerViewHolder.LAYOUT -> ShopFlashSaleProductGridShimmerViewHolder(view)
            ShopFlashSaleErrorViewHolder.LAYOUT -> ShopFlashSaleErrorViewHolder(view, errorListener)
            else -> commonCarouselTypeFactory.createViewHolder(view, viewType)
        }
    }
}
