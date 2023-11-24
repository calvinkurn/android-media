package com.tokopedia.shop.home.view.model

import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.shop.campaign.view.adapter.ShopCampaignTabAdapterTypeFactory
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapterTypeFactory
import com.tokopedia.shop.home.view.adapter.ShopWidgetTypeFactory
import com.tokopedia.youtube_common.data.model.YoutubeVideoDetailModel

/**
 * Created by rizqiaryansa on 2020-02-21.
 */
data class ShopHomeDisplayWidgetUiModel(
    override val widgetId: String = "",
    override val layoutOrder: Int = -1,
    override val name: String = "",
    override val type: String = "",
    override val header: BaseShopHomeWidgetUiModel.Header = BaseShopHomeWidgetUiModel.Header(),
    override val isFestivity: Boolean = false,
    val data: List<DisplayWidgetItem>? = null
) : BaseShopHomeWidgetUiModel() {
    val impressHolder = ImpressHolder()

    data class DisplayWidgetItem(
        val imageUrl: String = "",
        val appLink: String = "",
        val webLinkL: String = "",
        val videoUrl: String = "",
        val bannerId: String = "",
        var youTubeVideoDetail: YoutubeVideoDetailModel? = null
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
