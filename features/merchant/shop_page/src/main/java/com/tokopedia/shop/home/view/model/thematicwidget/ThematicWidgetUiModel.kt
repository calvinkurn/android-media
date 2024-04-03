package com.tokopedia.shop.home.view.model.thematicwidget

import android.os.Parcelable
import com.tokopedia.shop.home.view.adapter.ShopWidgetTypeFactory
import com.tokopedia.shop.home.view.model.BaseShopHomeWidgetUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel

data class ThematicWidgetUiModel(
    override val widgetId: String,
    override val layoutOrder: Int,
    override val name: String,
    override val type: String,
    override val header: Header,
    override val isFestivity: Boolean,
    val campaignName: String = "",
    val campaignSubName: String = "",
    val statusCampaign: String = "",
    val endDate: String = "",
    val timerCounter: String = "",
    val productList: List<ShopHomeProductUiModel> = listOf(),
    val firstBackgroundColor: String = "",
    val secondBackgroundColor: String = "",
    val campaignId: String = "",
    var imageBanner: String = "",
    var rvState: Parcelable? = null,
): BaseShopHomeWidgetUiModel() {

    override fun type(typeFactory: ShopWidgetTypeFactory): Int {
        return typeFactory.type(this)
    }
}
