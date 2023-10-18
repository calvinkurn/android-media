package com.tokopedia.shop.home.view.model

import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapterTypeFactory
import com.tokopedia.shop.home.view.adapter.ShopWidgetTypeFactory

data class ShopHomeFlashSaleUiModel(
    override val widgetId: String,
    override val layoutOrder: Int,
    override val name: String,
    override val type: String,
    override val header: Header,
    override val isFestivity: Boolean = false,
    val data: List<FlashSaleItem>? = null
) : BaseShopHomeWidgetUiModel() {

    val impressHolder = ImpressHolder()

    override fun type(typeFactory: ShopWidgetTypeFactory): Int {
        return if (typeFactory is ShopHomeAdapterTypeFactory) {
            typeFactory.type(this)
        } else {
            Int.ZERO
        }
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
        var totalProduct: Int = 0,
        val totalProductWording: String = "",
        val productList: List<ShopHomeProductUiModel> = listOf(),
        var isRemindMe: Boolean = false,
        val firstBackgroundColor: String = "",
        val secondBackgroundColor: String = ""
    )
}
