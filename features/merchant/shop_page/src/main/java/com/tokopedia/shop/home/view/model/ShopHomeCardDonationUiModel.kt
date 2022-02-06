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

    override fun equals(other: Any?): Boolean {
        return other?.let {
            it is ShopHomeCardDonationUiModel &&
                    this.widgetId == it.widgetId &&
                    this.type == it.type &&
                    !this.isNewData &&
                    this.widgetState == it.widgetState
        } ?: false
    }

    override fun hashCode(): Int {
        var result = widgetId.hashCode()
        result = 31 * result + layoutOrder
        result = 31 * result + name.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + header.hashCode()
        return result
    }

}