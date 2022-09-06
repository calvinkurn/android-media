package com.tokopedia.autocompletecomponent.universal.presentation.widget.carousel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocompletecomponent.universal.presentation.BaseUniversalDataView
import com.tokopedia.autocompletecomponent.universal.presentation.typefactory.UniversalSearchTypeFactory
import com.tokopedia.discovery.common.analytics.SearchComponentTracking
import com.tokopedia.discovery.common.analytics.searchComponentTracking
import com.tokopedia.kotlin.model.ImpressHolder

class CarouselDataView(
    val data: BaseUniversalDataView = BaseUniversalDataView(),
    val product: List<Product> = listOf(),
): Visitable<UniversalSearchTypeFactory>,
    SearchComponentTracking by searchComponentTracking(
        trackingOption = data.trackingOption,
        keyword = data.keyword,
        valueName = data.title,
        componentId = data.componentId,
        applink = data.applink,
        dimension90 = data.dimension90,
    ) {

    override fun type(typeFactory: UniversalSearchTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }

    class Product(
        val id: String = "",
        val applink: String = "",
        val imageUrl: String = "",
        val title: String = "",
        val componentId: String = "",
        val trackingOption: Int = 0,
        val price: String = "",
        val originalPrice: String = "",
        val discountPercentage: String = "",
        val ratingAverage: String = "",
        val countSold: String = "",
        val shop: Shop = Shop(),
        val badge: List<Badge> = listOf(),
        val freeOngkir: FreeOngkir = FreeOngkir(),
        val keyword: String = "",
        val dimension90: String = "",
    ): ImpressHolder(),
        SearchComponentTracking by searchComponentTracking(
        trackingOption = trackingOption,
        keyword = keyword,
        valueName = title,
        componentId = componentId,
        applink = applink,
        dimension90 = dimension90,
    ) {
        class Shop(
            val name: String = "",
            val city: String = "",
        )

        class Badge(
            val title: String = "",
            val imageUrl: String = "",
            val show: Boolean = false,
        )

        class FreeOngkir(
            val imgUrl: String = "",
            val isActive: Boolean = false,
        )
    }
}