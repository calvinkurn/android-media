package com.tokopedia.shop.home.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapterTypeFactory

interface BaseShopHomeWidgetUiModel: Visitable<ShopHomeAdapterTypeFactory>{
    val widgetId : Int
    val layoutOrder :Int
    val name :String
    val type :String
    val header: Header
    data class Header(
            val title: String = "",
            val ctaText: String = "",
            val ctaLink: String = "",
            val cover: String = ""
    )
}