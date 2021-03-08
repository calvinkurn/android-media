package com.tokopedia.shop.pageheader.presentation.adapter.typefactory.widget

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.component.ShopActionButtonWidgetChatButtonComponentViewHolder
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.component.ShopPerformanceWidgetBadgeTextValueComponentViewHolder
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.widget.ShopHeaderBasicInfoWidgetViewHolder
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.widget.ShopHeaderActionButtonWidgetViewHolder
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.widget.ShopHeaderPerformanceWidgetViewHolder
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopHeaderWidgetUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopHeaderWidgetUiModel.WidgetType.SHOP_ACTION
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopHeaderWidgetUiModel.WidgetType.SHOP_BASIC_INFO
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopHeaderWidgetUiModel.WidgetType.SHOP_PERFORMANCE

class ShopPageHeaderAdapterTypeFactory(
        private val shopHeaderBasicInfoWidgetListener: ShopHeaderBasicInfoWidgetViewHolder.Listener,
        private val shopPerformanceWidgetBadgeTextValueListener: ShopPerformanceWidgetBadgeTextValueComponentViewHolder.Listener,
        private val shopActionButtonWidgetChatButtonComponentListener: ShopActionButtonWidgetChatButtonComponentViewHolder.Listener
) : BaseAdapterTypeFactory() {

    fun type(model: ShopHeaderWidgetUiModel): Int {
        return when (model.type.toLowerCase()) {
            SHOP_BASIC_INFO.toLowerCase() -> ShopHeaderBasicInfoWidgetViewHolder.LAYOUT
            SHOP_PERFORMANCE.toLowerCase() -> ShopHeaderPerformanceWidgetViewHolder.LAYOUT
            SHOP_ACTION.toLowerCase() -> ShopHeaderActionButtonWidgetViewHolder.LAYOUT
            else -> HideViewHolder.LAYOUT
        }

    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ShopHeaderBasicInfoWidgetViewHolder.LAYOUT -> ShopHeaderBasicInfoWidgetViewHolder(parent, shopHeaderBasicInfoWidgetListener)
            ShopHeaderPerformanceWidgetViewHolder.LAYOUT -> ShopHeaderPerformanceWidgetViewHolder(parent, shopPerformanceWidgetBadgeTextValueListener)
            ShopHeaderActionButtonWidgetViewHolder.LAYOUT -> ShopHeaderActionButtonWidgetViewHolder(parent, shopActionButtonWidgetChatButtonComponentListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}