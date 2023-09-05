package com.tokopedia.shop.home.view.model

import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapterTypeFactory
import com.tokopedia.shop.home.view.adapter.ShopWidgetTypeFactory

data class ShopWidgetDisplayBannerProductHotspotUiModel(
    override val widgetId: String = "",
    override val layoutOrder: Int = -1,
    override val name: String = "",
    override val type: String = "",
    override val header: Header = Header(),
    override val isFestivity: Boolean = false,
    val data: List<Data> = listOf()
) : BaseShopHomeWidgetUiModel() {

    val impressHolder = ImpressHolder()

    data class Data(
        val appLink: String = "",
        val imageUrl: String = "",
        val linkType: String = "",
        val bannerId: String = "",
        val listProductHotspot: List<ProductHotspot> = listOf(),
    ) : ImpressHolder() {
        data class ProductHotspot(
            val productId: String = "",
            val name: String = "",
            val imageUrl: String = "",
            val productUrl: String = "",
            val displayedPrice: String = "",
            val isSoldOut: Boolean = false,
            val hotspotCoordinate: Coordinate = Coordinate()
        ) {
            data class Coordinate(
                val x: Float = 0f,
                val y: Float = 0f
            )
        }
    }


    override fun type(typeFactory: ShopWidgetTypeFactory): Int {
        return when (typeFactory) {
            is ShopHomeAdapterTypeFactory -> {
                typeFactory.type(this)
            }

            else -> {
                Int.ZERO
            }
        }
    }

}
