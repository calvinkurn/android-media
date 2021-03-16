package com.tokopedia.shop.pageheader.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.shop.pageheader.presentation.adapter.typefactory.component.ShopHeaderBadgeTextValueComponentTypeFactory
import com.tokopedia.shop.pageheader.presentation.adapter.typefactory.component.ShopHeaderImageOnlyComponentTypeFactory
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.component.ShopPerformanceWidgetBadgeTextValueComponentViewHolder
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.component.ShopPerformanceWidgetImageOnlyComponentViewHolder
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopHeaderBadgeTextValueComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopHeaderImageOnlyComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopHeaderWidgetUiModel

class ShopHeaderPerformanceWidgetAdapterTypeFactory(
        private val shopHeaderWidgetUiModel: ShopHeaderWidgetUiModel,
        private val shopPerformanceWidgetBadgeTextValueListener: ShopPerformanceWidgetBadgeTextValueComponentViewHolder.Listener,
        private val shopPerformanceWidgetImageOnlyListener: ShopPerformanceWidgetImageOnlyComponentViewHolder.Listener

) :
        BaseAdapterTypeFactory(),
        ShopHeaderImageOnlyComponentTypeFactory,
        ShopHeaderBadgeTextValueComponentTypeFactory {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ShopPerformanceWidgetBadgeTextValueComponentViewHolder.LAYOUT -> ShopPerformanceWidgetBadgeTextValueComponentViewHolder(
                    parent,
                    shopHeaderWidgetUiModel,
                    shopPerformanceWidgetBadgeTextValueListener
            )
            ShopPerformanceWidgetImageOnlyComponentViewHolder.LAYOUT -> ShopPerformanceWidgetImageOnlyComponentViewHolder(
                    parent,
                    shopHeaderWidgetUiModel,
                    shopPerformanceWidgetImageOnlyListener
            )
            -1 -> HideViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

    override fun type(model: ShopHeaderBadgeTextValueComponentUiModel): Int {
        return ShopPerformanceWidgetBadgeTextValueComponentViewHolder.LAYOUT
    }

    override fun type(model: ShopHeaderImageOnlyComponentUiModel): Int {
        return ShopPerformanceWidgetImageOnlyComponentViewHolder.LAYOUT
    }
}