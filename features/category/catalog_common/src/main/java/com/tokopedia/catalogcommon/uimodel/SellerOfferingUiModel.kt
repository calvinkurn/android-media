package com.tokopedia.catalogcommon.uimodel

import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.adapter.CatalogAdapterFactory

data class SellerOfferingUiModel(
    override var idWidget: String = "",
    override var widgetType: String = "",
    override var widgetName: String = "",
    override var widgetBackgroundColor: Int? = null,
    override var widgetTextColor: Int? = null,
    override var darkMode: Boolean = false,
    var productImage: String = "",
    var shopBadge: String = "",
    var shopName: String = "",
    var stockBar: Int = 0,
    var productName: String = "",
    var productPrice: String = "",
    var productSlashPrice: String = "",
    var shopLocation: String = "",
    var chatResponseTime: String = "",
    var orderProcessTime: String = "",
    var labelPromo: String = "",
    var labelTotalDisc: String = "",
    var shopRating: String = "",
    var totalShopRating: String = "",
    var totalSold: String = "",
    var freeOngkir: String = "",
    var estimationShipping: String = "",
    var isShopGuarantee: Boolean = false,
    val installment:String = "",
    val cardColor: Int = R.drawable.bg_rounded_border_dark,
    val additionalService:String = "",
) : BaseCatalogUiModel(
    idWidget, widgetType, widgetName, widgetBackgroundColor, widgetTextColor,
    darkMode
) {

    override fun type(typeFactory: CatalogAdapterFactory): Int {
        return typeFactory.type(this)
    }
}
