package com.tokopedia.shop_widget.thematicwidget.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop_widget.thematicwidget.uimodel.ProductCardSeeAllUiModel
import com.tokopedia.shop_widget.thematicwidget.uimodel.ProductCardSpaceUiModel
import com.tokopedia.shop_widget.thematicwidget.uimodel.ProductCardUiModel
import com.tokopedia.shop_widget.thematicwidget.viewholder.ProductCardSeeAllViewHolder
import com.tokopedia.shop_widget.thematicwidget.viewholder.ProductCardSpaceViewHolder
import com.tokopedia.shop_widget.thematicwidget.viewholder.ProductCardViewHolder

class ProductCardTypeFactoryImpl(
    private val productCardListener: ProductCardViewHolder.ProductCardListener,
    private val productCardSeeAllListener: ProductCardSeeAllViewHolder.ProductCardSeeAllListener,
):  BaseAdapterTypeFactory(), ProductCardTypeFactory {
    override fun type(uiModel: ProductCardUiModel): Int = ProductCardViewHolder.LAYOUT

    override fun type(uiModel: ProductCardSeeAllUiModel): Int = ProductCardSeeAllViewHolder.LAYOUT

    override fun type(uiModel: ProductCardSpaceUiModel): Int = ProductCardSpaceViewHolder.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ProductCardViewHolder.LAYOUT -> ProductCardViewHolder(view, productCardListener)
            ProductCardSeeAllViewHolder.LAYOUT -> ProductCardSeeAllViewHolder(view, productCardSeeAllListener)
            ProductCardSpaceViewHolder.LAYOUT -> ProductCardSpaceViewHolder(view)
            else -> super.createViewHolder(view, type)
        }
    }
}