package com.tokopedia.shop.home.view.model

import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.shop.campaign.view.adapter.ShopCampaignTabAdapterTypeFactory
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeProductBundleItemUiModel
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapterTypeFactory
import com.tokopedia.shop.home.view.adapter.ShopWidgetTypeFactory

/**
 * Created by Rafli Syam on 05/01/22
 */

data class ShopHomeProductBundleListUiModel(
    override val widgetId: String = "",
    override val layoutOrder: Int = -1,
    override val name: String = "",
    override val type: String = "",
    override val header: Header = Header(),
    override val isFestivity: Boolean = false,
    val productBundleList: List<ShopHomeProductBundleItemUiModel> = listOf()
) : BaseShopHomeWidgetUiModel() {
    val impressHolder = ImpressHolder()

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
