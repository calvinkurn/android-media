package com.tokopedia.shop_widget.common.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop_widget.common.uimodel.ProductCardSeeAllUiModel
import com.tokopedia.shop_widget.common.uimodel.ProductCardUiModel
import com.tokopedia.shop_widget.common.viewholder.ProductCardSeeAllViewHolder
import com.tokopedia.shop_widget.common.viewholder.ProductCardViewHolder

class ProductCardTypeFactoryImpl:  BaseAdapterTypeFactory(), ProductCardTypeFactory {
    override fun type(uiModel: ProductCardUiModel): Int = ProductCardViewHolder.LAYOUT

    override fun type(uiModel: ProductCardSeeAllUiModel): Int = ProductCardSeeAllViewHolder.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ProductCardViewHolder.LAYOUT -> ProductCardViewHolder(view)
            ProductCardSeeAllViewHolder.LAYOUT -> ProductCardSeeAllViewHolder(view)
            else -> super.createViewHolder(view, type)
        }
    }
}