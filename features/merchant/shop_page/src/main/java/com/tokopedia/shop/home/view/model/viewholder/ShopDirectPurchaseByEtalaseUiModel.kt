package com.tokopedia.shop.home.view.model.viewholder

import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapterTypeFactory
import com.tokopedia.shop.home.view.adapter.ShopWidgetTypeFactory
import com.tokopedia.shop.home.view.model.BaseShopHomeWidgetUiModel


data class ShopDirectPurchaseByEtalaseUiModel(
    override val widgetId: String = "",
    override val layoutOrder: Int = -1,
    override val name: String = "",
    override val type: String = "",
    override val header: Header = Header(),
    override val isFestivity: Boolean = false,
    val tabData: List<TabData> = listOf()
) : BaseShopHomeWidgetUiModel() {
    val impressHolder = ImpressHolder()

    data class TabData(
        val ratio: String = "",
        val title: String = "",
        val banner: String = "",
        val listShowcase: List<Showcase> = listOf()
    ) {
        data class Showcase(
            val imageUrl: String = "",
            val desktopImageUrl: String = "",
            val linkType: String = "",
            val linkId: String = "",
            val name: String = "",
        )
    }

    override fun type(typeFactory: ShopWidgetTypeFactory): Int {
        return if (typeFactory is ShopHomeAdapterTypeFactory) {
            typeFactory.type(this)
        } else {
            Int.ZERO
        }
    }
}
