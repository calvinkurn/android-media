package com.tokopedia.shop.campaign.view.model

import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.shop.campaign.view.adapter.ShopCampaignTabAdapterTypeFactory
import com.tokopedia.shop.home.view.adapter.ShopWidgetTypeFactory
import com.tokopedia.shop.home.view.model.BaseShopHomeWidgetUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel

data class ShopCampaignWidgetCarouselProductUiModel(
    override val widgetId: String = "",
    override val layoutOrder: Int = -1,
    override val name: String = "",
    override val type: String = "",
    override val header: Header = Header(),
    override val isFestivity: Boolean = false,
    val productList: List<ShopHomeProductUiModel> = listOf(),
    val statusCampaign: String = "",
) : BaseShopHomeWidgetUiModel() {

    val impressHolder = ImpressHolder()

    override fun type(typeFactory: ShopWidgetTypeFactory): Int {
        return if (typeFactory is ShopCampaignTabAdapterTypeFactory) {
            typeFactory.type(this)
        } else {
            Int.ZERO
        }
    }
}
