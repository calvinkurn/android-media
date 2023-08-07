package com.tokopedia.shop.home.view.model

import com.tokopedia.shop.home.view.adapter.ShopWidgetTypeFactory

class ShopHomeV4TerlarisUiModel (
    override val widgetId: String,
    override val layoutOrder: Int,
    override val name: String,
    override val type: String,
    override val header: Header,
    override val isFestivity: Boolean,
    val productList: List<ShopHomeV4TerlarisItemUiModel>
    ): BaseShopHomeWidgetUiModel() {

    override fun type(typeFactory: ShopWidgetTypeFactory?): Int {
        TODO("Not yet implemented")
    }

}
