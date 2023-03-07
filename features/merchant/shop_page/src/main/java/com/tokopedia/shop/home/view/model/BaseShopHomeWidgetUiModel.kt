package com.tokopedia.shop.home.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapterTypeFactory
import com.tokopedia.shop_widget.common.util.WidgetState

abstract class BaseShopHomeWidgetUiModel : Visitable<ShopHomeAdapterTypeFactory> {
    abstract val widgetId: String
    abstract val layoutOrder: Int
    abstract val name: String
    abstract val type: String
    abstract val header: Header
    var widgetState: WidgetState = WidgetState.INIT
    var isNewData: Boolean = false
    var widgetMasterId = ""
    data class Header(
        val title: String = "",
        val subtitle: String = "",
        val ctaText: String = "",
        val ctaLink: String = "",
        val cover: String = "",
        val ratio: String = "",
        val isATC: Int = 0,
        val etalaseId: String = ""
    )
}
