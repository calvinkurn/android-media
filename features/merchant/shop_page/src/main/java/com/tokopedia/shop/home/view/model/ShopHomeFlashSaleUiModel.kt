package com.tokopedia.shop.home.view.model

import com.tokopedia.shop.home.view.adapter.ShopHomeAdapterTypeFactory

class ShopHomeFlashSaleUiModel(
    override val widgetId: String,
    override val layoutOrder: Int,
    override val name: String,
    override val type: String,
    override val header: Header,
    val data: List<FlashSaleItem>? = null
) : BaseShopHomeWidgetUiModel() {
    override fun type(typeFactory: ShopHomeAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
    data class FlashSaleItem(
        val campaignId: String = "",
        val name: String = "",
        val description: String = "",
        val startDate: String = "",
        val endDate: String = "",
        val statusCampaign: String = "",
        val timeDescription: String = "",
        val timeCounter: String = "",
        var totalNotify: Int = 0,
        val totalNotifyWording: String = "",
        val productList: List<ShopHomeProductUiModel> = listOf(),
        var isRemindMe: Boolean = false
    )
}