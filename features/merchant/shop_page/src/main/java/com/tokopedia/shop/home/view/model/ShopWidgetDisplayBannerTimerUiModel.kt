package com.tokopedia.shop.home.view.model

import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.shop.campaign.view.adapter.ShopCampaignTabAdapterTypeFactory
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
    val impressHolder = ImpressHolder()

    data class Data(
        val appLink: String = "",
        val imageUrl: String = "",
        val linkType: String = "",
        val campaignId: String = "",
        val timeDescription: String = "",
        val timeCounter: Long = 0L,
        val startDate: String = "",
        val endDate: String = "",
        val bgColor: List<String> = listOf(),
        val textColor: String = "",
        val status: Int = -1,
        var totalNotify: Int = 0,
        val totalNotifyWording: String = "",
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
}
