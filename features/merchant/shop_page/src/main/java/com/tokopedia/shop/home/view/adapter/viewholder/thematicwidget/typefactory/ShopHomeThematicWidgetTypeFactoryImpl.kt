package com.tokopedia.shop.home.view.adapter.viewholder.thematicwidget.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.shop.home.view.model.thematicwidget.ProductCardSeeAllUiModel
import com.tokopedia.shop.home.view.model.thematicwidget.ProductCardSpaceUiModel
import com.tokopedia.shop.home.view.adapter.viewholder.thematicwidget.viewholder.ProductCardGridViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.thematicwidget.viewholder.ProductCardListViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.thematicwidget.viewholder.ProductCardSeeAllViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.thematicwidget.viewholder.ProductCardSpaceViewHolder
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel
import com.tokopedia.shop.home.view.model.thematicwidget.ThematicWidgetUiModel

class ShopHomeThematicWidgetTypeFactoryImpl(
        private val productCardGridListener: ProductCardGridViewHolder.ProductCardListener,
        private val productCardListListener: ProductCardListViewHolder.ProductCardListener,
        private val productCardSeeAllListener: ProductCardSeeAllViewHolder.ProductCardSeeAllListener,
        private val totalProductSize: Int,
        private val isOverrideWidgetTheme: Boolean,
        private val thematicWidgetUiModel: ThematicWidgetUiModel,
        private val isFestivity: Boolean
) : BaseAdapterTypeFactory(), ShopHomeThematicWidgetTypeFactory {
    override fun type(uiModel: ShopHomeProductUiModel): Int {
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
            ProductCardListViewHolder.LAYOUT -> ProductCardListViewHolder(itemView = view, listener = productCardListListener, isOverrideWidgetTheme = isOverrideWidgetTheme, thematicWidgetUiModel = thematicWidgetUiModel, isFestivity = isFestivity)
            ProductCardGridViewHolder.LAYOUT -> ProductCardGridViewHolder(itemView = view, listener = productCardGridListener, isOverrideWidgetTheme = isOverrideWidgetTheme, thematicWidgetUiModel = thematicWidgetUiModel, isFestivity = isFestivity)
            ProductCardSeeAllViewHolder.LAYOUT -> ProductCardSeeAllViewHolder(view, productCardSeeAllListener)
            ProductCardSpaceViewHolder.LAYOUT -> ProductCardSpaceViewHolder(view)
            else -> super.createViewHolder(view, type)
        }
    }
}
