package com.tokopedia.shop_widget.thematicwidget.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.shop_widget.thematicwidget.uimodel.ProductCardSeeAllUiModel
import com.tokopedia.shop_widget.thematicwidget.uimodel.ProductCardSpaceUiModel
import com.tokopedia.shop_widget.thematicwidget.uimodel.ProductCardUiModel
import com.tokopedia.shop_widget.thematicwidget.viewholder.ProductCardGridViewHolder
import com.tokopedia.shop_widget.thematicwidget.viewholder.ProductCardListViewHolder
import com.tokopedia.shop_widget.thematicwidget.viewholder.ProductCardSeeAllViewHolder
import com.tokopedia.shop_widget.thematicwidget.viewholder.ProductCardSpaceViewHolder

class ProductCardTypeFactoryImpl(
    private val productCardGridListener: ProductCardGridViewHolder.ProductCardListener,
    private val productCardListListener: ProductCardListViewHolder.ProductCardListener,
    private val productCardSeeAllListener: ProductCardSeeAllViewHolder.ProductCardSeeAllListener,
    private val totalProductSize: Int,
    private val isOverrideWidgetTheme: Boolean
) : BaseAdapterTypeFactory(), ProductCardTypeFactory {
    override fun type(uiModel: ProductCardUiModel): Int {
        return when (totalProductSize) {
            Int.ONE -> {
                ProductCardListViewHolder.LAYOUT
            }
            else -> {
                ProductCardGridViewHolder.LAYOUT
            }
        }
    }

    override fun type(uiModel: ProductCardSeeAllUiModel): Int = ProductCardSeeAllViewHolder.LAYOUT

    override fun type(uiModel: ProductCardSpaceUiModel): Int = ProductCardSpaceViewHolder.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ProductCardListViewHolder.LAYOUT -> ProductCardListViewHolder(itemView = view, listener = productCardListListener, isOverrideWidgetTheme = isOverrideWidgetTheme)
            ProductCardGridViewHolder.LAYOUT -> ProductCardGridViewHolder(itemView = view, listener = productCardGridListener, isOverrideWidgetTheme = isOverrideWidgetTheme)
            ProductCardSeeAllViewHolder.LAYOUT -> ProductCardSeeAllViewHolder(view, productCardSeeAllListener)
            ProductCardSpaceViewHolder.LAYOUT -> ProductCardSpaceViewHolder(view)
            else -> super.createViewHolder(view, type)
        }
    }
}
