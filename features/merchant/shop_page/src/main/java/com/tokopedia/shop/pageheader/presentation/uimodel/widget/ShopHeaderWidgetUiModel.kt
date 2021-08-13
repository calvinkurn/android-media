package com.tokopedia.shop.pageheader.presentation.uimodel.widget

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop.pageheader.presentation.adapter.typefactory.widget.ShopPageHeaderAdapterTypeFactory
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopHeaderComponentUiModel

data class ShopHeaderWidgetUiModel(
        val widgetId: String = "",
        val name: String = "",
        val type: String = "",
        val components: List<BaseShopHeaderComponentUiModel> = listOf()
) : Visitable<ShopPageHeaderAdapterTypeFactory> {
    object WidgetType {
        const val SHOP_BASIC_INFO = "shop_basic_info"
        const val SHOP_PERFORMANCE = "shop_performance"
        const val SHOP_ACTION = "action_button"
        const val SHOP_PLAY = "play"
    }

    override fun type(typeFactory: ShopPageHeaderAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}