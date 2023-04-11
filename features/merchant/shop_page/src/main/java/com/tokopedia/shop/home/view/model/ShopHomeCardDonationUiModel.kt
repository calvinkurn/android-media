package com.tokopedia.shop.home.view.model

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapterTypeFactory

data class ShopHomeCardDonationUiModel(
    override val widgetId: String,
    override val layoutOrder: Int,
    override val name: String,
    override val type: String,
    override val header: Header,
    override val isFestivity: Boolean = false
) : BaseShopHomeWidgetUiModel() {

    val impressHolder = ImpressHolder()

    override fun type(typeFactory: ShopHomeAdapterTypeFactory): Int =
        typeFactory.type(this)
}
