package com.tokopedia.catalogcommon.uimodel

import com.tokopedia.catalogcommon.adapter.CatalogAdapterFactory

data class PriceCtaSellerOfferingUiModel(
    override var idWidget: String = "",
    override var widgetType: String = "",
    override var widgetName: String = "",
    override var widgetBackgroundColor: Int? = null,
    override var widgetTextColor: Int? = null,
    override var darkMode: Boolean = false,
    var price: String = "",
    var productId:String = "",
    var shopId:String = "",
    var textTitleColor: Int? = null,
    var textPriceColor: Int? = null,
    var iconColor: Int? = null
) : BaseCatalogUiModel(
    idWidget, widgetType, widgetName, widgetBackgroundColor, widgetTextColor,
    darkMode
) {

    override fun type(typeFactory: CatalogAdapterFactory): Int {
        return typeFactory.type(this)
    }
}
