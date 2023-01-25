package com.tokopedia.shop.home.view.model

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapterTypeFactory
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
    override val isFestivity: Boolean,
    val data: List<DisplayWidgetItem>? = null
) : BaseShopHomeWidgetUiModel() {
    val impressHolder = ImpressHolder()

    data class DisplayWidgetItem(
        val imageUrl: String = "",
        val appLink: String = "",
        val webLinkL: String = "",
        val videoUrl: String = "",
        var youTubeVideoDetail: YoutubeVideoDetailModel? = null
    ) : ImpressHolder()

    override fun type(typeFactory: ShopHomeAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
