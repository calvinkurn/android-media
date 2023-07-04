package com.tokopedia.shop.campaign.view.model

import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.shop.campaign.view.adapter.ShopCampaignTabAdapterTypeFactory
import com.tokopedia.shop.home.view.adapter.ShopWidgetTypeFactory
import com.tokopedia.shop.home.view.model.BaseShopHomeWidgetUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel
import com.tokopedia.shop.home.view.model.ShopWidgetDisplayBannerTimerUiModel

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

    companion object{
        private const val LINK_TYPE_CAMPAIGN_ID = "campaign"
    }

    val impressHolder = ImpressHolder()

    override fun type(typeFactory: ShopWidgetTypeFactory): Int {
        return if (typeFactory is ShopCampaignTabAdapterTypeFactory) {
            typeFactory.type(this)
        } else {
            Int.ZERO
        }
    }

    fun getCampaignId(): String {
        return header.data.firstOrNull { it.linkType == LINK_TYPE_CAMPAIGN_ID }?.linkId?.toString().orEmpty()
    }
}
