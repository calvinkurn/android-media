package com.tokopedia.shop.pageheader.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.shop.pageheader.presentation.adapter.typefactory.component.ShopPageHeaderBadgeTextValueComponentTypeFactory
import com.tokopedia.shop.pageheader.presentation.adapter.typefactory.component.ShopPageHeaderImageOnlyComponentTypeFactory
import com.tokopedia.shop.pageheader.presentation.adapter.typefactory.component.ShopPageHeaderImageTextComponentTypeFactory
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.component.ShopPageHeaderPerformanceWidgetBadgeTextValueComponentViewHolder
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.component.ShopPageHeaderPerformanceWidgetImageOnlyComponentViewHolder
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.component.ShopPageHeaderPerformanceWidgetImageTextComponentViewHolder
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopPageHeaderBadgeTextValueComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopPageHeaderImageOnlyComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopPageHeaderImageTextComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopPageHeaderWidgetUiModel

class ShopPageHeaderPerformanceWidgetAdapterTypeFactory(
    private val shopPageHeaderWidgetUiModel: ShopPageHeaderWidgetUiModel,
    private val shopPageHeaderPerformanceWidgetBadgeTextValueListener: ShopPageHeaderPerformanceWidgetBadgeTextValueComponentViewHolder.Listener,
    private val shopPageHeaderPerformanceWidgetImageOnlyListener: ShopPageHeaderPerformanceWidgetImageOnlyComponentViewHolder.Listener,
    private val shopPageHeaderPerformanceWidgetImageTextListener: ShopPageHeaderPerformanceWidgetImageTextComponentViewHolder.Listener
) :
    BaseAdapterTypeFactory(),
    ShopPageHeaderImageOnlyComponentTypeFactory,
    ShopPageHeaderBadgeTextValueComponentTypeFactory,
    ShopPageHeaderImageTextComponentTypeFactory {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ShopPageHeaderPerformanceWidgetBadgeTextValueComponentViewHolder.LAYOUT -> ShopPageHeaderPerformanceWidgetBadgeTextValueComponentViewHolder(
                parent,
                shopPageHeaderWidgetUiModel,
                shopPageHeaderPerformanceWidgetBadgeTextValueListener
            )
            ShopPageHeaderPerformanceWidgetImageOnlyComponentViewHolder.LAYOUT -> ShopPageHeaderPerformanceWidgetImageOnlyComponentViewHolder(
                parent,
                shopPageHeaderWidgetUiModel,
                shopPageHeaderPerformanceWidgetImageOnlyListener
            )
            ShopPageHeaderPerformanceWidgetImageTextComponentViewHolder.LAYOUT -> ShopPageHeaderPerformanceWidgetImageTextComponentViewHolder(
                parent,
                shopPageHeaderWidgetUiModel,
                shopPageHeaderPerformanceWidgetImageTextListener
            )
            -1 -> HideViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

    override fun type(model: ShopPageHeaderBadgeTextValueComponentUiModel): Int {
        return ShopPageHeaderPerformanceWidgetBadgeTextValueComponentViewHolder.LAYOUT
    }

    override fun type(model: ShopPageHeaderImageOnlyComponentUiModel): Int {
        return ShopPageHeaderPerformanceWidgetImageOnlyComponentViewHolder.LAYOUT
    }

    override fun type(model: ShopPageHeaderImageTextComponentUiModel): Int {
        return ShopPageHeaderPerformanceWidgetImageTextComponentViewHolder.LAYOUT
    }
}
