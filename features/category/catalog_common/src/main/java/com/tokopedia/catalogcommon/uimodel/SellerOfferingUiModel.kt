package com.tokopedia.catalogcommon.uimodel

import com.tokopedia.catalogcommon.adapter.CatalogAdapterFactory

data class SellerOfferingUiModel(
    override var idWidget: String = "",
    override var widgetType: String = "",
    override var widgetName: String = "",
    override var widgetBackgroundColor: Int? = null,
    override var widgetTextColor: Int? = null,
    override var darkMode: Boolean = false,
    val productId: String = "",
    var productImage: String = "",
    var shopBadge: String = "",
    var shopName: String = "",
    var stockBar: Stock = Stock(),
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
    val paymentOption: PaymentOption = PaymentOption(),
    val cardColor: String = "#212121",
    val additionalService: String = "",
    var variantsName: String = "",
    val courier: String = ""
) : BaseCatalogUiModel(
    idWidget,
    widgetType,
    widgetName,
    widgetBackgroundColor,
    widgetTextColor,
    darkMode
) {

    data class PaymentOption(
        val desc: String = "",
        val icon: String = ""
    )

    data class Stock(
        val stockRemaining: Int = 0,
        val stockAlertText: String = "",
        val isHidden: Boolean = false
    )

    override fun type(typeFactory: CatalogAdapterFactory): Int {
        return typeFactory.type(this)
    }
}
