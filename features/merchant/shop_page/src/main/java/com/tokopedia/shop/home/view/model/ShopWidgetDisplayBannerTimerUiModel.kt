package com.tokopedia.shop.home.view.model

import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.shop.campaign.view.adapter.ShopCampaignTabAdapterTypeFactory
import com.tokopedia.shop.common.data.model.DynamicRule
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapterTypeFactory
import com.tokopedia.shop.home.view.adapter.ShopWidgetTypeFactory

data class ShopWidgetDisplayBannerTimerUiModel(
    override val widgetId: String = "",
    override val layoutOrder: Int = -1,
    override val name: String = "",
    override val type: String = "",
    override val header: Header = Header(),
    override val isFestivity: Boolean = false,
    val data: Data? = null
) : BaseShopHomeWidgetUiModel() {

    companion object{
        private const val LINK_TYPE_CAMPAIGN_ID = "promo-team_cron_active-ready"
    }
    val impressHolder = ImpressHolder()

    data class Data(
        val appLink: String = "",
        val imageUrl: String = "",
        val linkType: String = "",
        val timeDescription: String = "",
        val timeCounter: Long = 0L,
        val startDate: String = "",
        val endDate: String = "",
        val bgColor: List<String> = listOf(),
        val textColor: String = "",
        val status: StatusCampaign = StatusCampaign.UPCOMING,
        var totalNotify: Int = 0,
        val totalNotifyWording: String = "",
        val dynamicRule: DynamicRule = DynamicRule(),
        var isRemindMe: Boolean? = null,
        var showRemindMeLoading : Boolean = false,
        var isHideRemindMeTextAfterXSeconds: Boolean = false
    ) : ImpressHolder()


    override fun type(typeFactory: ShopWidgetTypeFactory): Int {
        return when (typeFactory) {
            is ShopHomeAdapterTypeFactory -> {
                typeFactory.type(this)
            }

            is ShopCampaignTabAdapterTypeFactory -> {
                typeFactory.type(this)
            }

            else -> {
                Int.ZERO
            }
        }
    }

    fun getCampaignId(): String {
        return header.data.firstOrNull { it.linkType == LINK_TYPE_CAMPAIGN_ID }?.linkId?.toString().orEmpty()
    }
}
