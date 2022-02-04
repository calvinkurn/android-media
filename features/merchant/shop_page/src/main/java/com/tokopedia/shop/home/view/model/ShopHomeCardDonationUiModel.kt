package com.tokopedia.shop.home.view.model

import com.tokopedia.shop.home.view.adapter.ShopHomeAdapterTypeFactory

class ShopHomeCardDonationUiModel(
    override val widgetId: String,
    override val layoutOrder: Int,
    override val name: String,
    override val type: String,
    override val header: Header
): BaseShopHomeWidgetUiModel() {

    override fun type(typeFactory: ShopHomeAdapterTypeFactory): Int =
        typeFactory.type(this)

}